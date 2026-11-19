# Lab 17: Spring Security with users in a database and JWT

My lab 15 Course API (MySQL, layered controller → service → repository), secured so that only authenticated requests get through: users are stored in a MySQL `users` table with BCrypt-hashed passwords, log in once at `/login`, and then call the API with a signed JWT (`Authorization: Bearer <token>`). HTTP Basic also still works.

## Setup

Same database setup as lab 15: copy `src/main/resources/db.properties.example` to `db.properties` and fill in your MySQL password (the file is in `.gitignore`). Run with `mvn clean spring-boot:run` or Run As → Spring Boot App.

At startup `schema.sql` recreates the `course` table (7 courses) and creates the `users` table only if it does not exist, so registered users survive restarts. `data.sql` inserts `usera` / `pwd12345` with `INSERT IGNORE`.

## What was added

| Layer | Class | Role |
|---|---|---|
| Security | `security/SecurityConfig` | Stateless filter chain: CSRF off, `/register` and `/login` open, everything else authenticated, HTTP Basic, `JwtFilter` before the username/password filter. Beans: `AuthenticationProvider` (`DaoAuthenticationProvider`), `AuthenticationManager`, `PasswordEncoder` (BCrypt, cost 12) |
| Security | `security/JwtFilter` | Reads `Authorization: Bearer …`, validates the token through `JwtService`, and marks the request as authenticated. A bad token is caught, so the client gets 401, not 500 |
| Controller | `controllers/UserController` | `POST /register` and `POST /login`, HTTP only |
| Service | `services/UserService` | Registers users (hashes the password) and verifies logins (returns a JWT) |
| Service | `services/MyUserDetailsService` | Loads a user by username for Spring Security |
| Service | `services/JwtService` | All token logic: create, parse, validate (HS256, 1 hour expiry, new key at every restart) |
| Repository | `repositories/UserRepository` | `findByUsername`, `existsByUsername` |
| Model | `model/User` | `@Table("users")` record `(id, username, password)` |
| Model | `model/UserPrincipal` | Adapter from `User` to Spring Security's `UserDetails` |

`POST /register` with a username that already exists returns **409 Conflict** instead of a database error.

## Test results

Run against a local MySQL 8 server. Course requests use `GET /api/v1/courses` (the course API from lab 15, in place of the starter's products).

**Step 5: HTTP Basic with the user from `data.sql`**

| Request | Auth | Expected | Result |
|---|---|---|---|
| `GET /api/v1/courses` | none | 401 | 401 |
| `GET /api/v1/courses` | Basic usera / pwd12345 | 200, 7 courses | 200 |
| `GET /api/v1/courses` | Basic usera / wrong | 401 | 401 |
| `GET /api/v1/courses` | Basic nobody / pwd12345 | 401 | 401 |

**Step 7: JWT**

| # | Request | Body / Auth | Expected | Result |
|---|---|---|---|---|
| 1 | `POST /register` | `{"username":"userb","password":"secret99"}` | 200, `{"id":2,"username":"userb","password":""}` | same |
| 2 | `POST /login` | `{"username":"userb","password":"secret99"}` | 200, a token `eyJhbGciOiJIUzI1NiJ9.eyJzdWIi…` | same |
| 3 | `POST /login` | wrong password | 401, `Invalid username or password` | same |
| 4 | `GET /api/v1/courses` | Bearer, token from #2 | 200 | 200 |
| 5 | `GET /api/v1/courses` | Bearer, one character changed | 401 | 401 |
| 6 | `GET /api/v1/courses` | Basic userb / secret99 | 200 | 200 |
| 7 | restart, repeat #4 | same token | 401, the signing key changed | 401 |

Decoded token payload: `{"sub":"userb","iat":1795074737,"exp":1795078337}`, so `exp − iat = 3600` seconds (1 hour).

`SELECT * FROM users;` shows both passwords as 60-character BCrypt hashes starting with `$2a$12$`.

**Step 8: layering**

- `UserController` uses `UserService` only; searching the `controllers` package for `Repository` finds nothing.
- `UserService` and `MyUserDetailsService` are the only classes that use `UserRepository`.
- `Jwts` is used only in `JwtService`; `JwtFilter` calls `JwtService`.
- No controller returns a password or a hash (`/register` returns `"password":""`).

## Questions

**1. Why can CSRF protection be disabled for this API but not for a classic form-login web site?**

A CSRF attack makes the victim's browser send a request to the site, and the browser attaches the site's session cookie automatically, so the request runs as the victim. A form-login site authenticates with exactly that cookie, so it needs a CSRF token to tell its own forms apart from forged ones. This API is stateless: it creates no session and no cookie, and every request must carry a `Bearer` token or Basic credentials in the `Authorization` header. A browser never adds that header by itself, so a forged cross-site request arrives unauthenticated and gets 401. There is nothing for CSRF to abuse.

**2. A token is stolen. How long can the attacker use it, and what two server-side changes would shorten that window?**

Until it expires: up to 1 hour after it was issued (`EXPIRATION_MS`), because the server only checks the signature and the expiry and cannot tell the attacker from the user. (Here a server restart also ends it early, since the key changes.) To shorten the window:
- Use a much shorter expiry (for example 5–15 minutes), with a refresh token so users do not have to log in again each time.
- Make tokens revocable: keep a deny-list of revoked token ids (`jti` claim) or a per-user "tokens valid after" timestamp that `JwtFilter` checks, so a stolen token can be cancelled immediately (for example on logout or a password change).

**3. The key in JwtService is regenerated at every restart. What breaks if you run two instances of the API behind a load balancer, and how would you fix it?**

Each instance generates its own random key, so a token signed by instance A fails the signature check on instance B. A user who logs in on A gets 401 whenever the load balancer sends a request to B, so requests fail at random. Restarting any instance also logs out everyone it signed for. The fix is one shared, stable secret: load it from configuration (an environment variable or a secret manager) instead of generating it in the constructor, so all instances sign and verify with the same key and tokens survive restarts. (Sticky sessions would only hide the problem.)
