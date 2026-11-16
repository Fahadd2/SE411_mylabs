# Lab 16: Spring Security introduction

My lab 15 Course API (MySQL), protected with Spring Security, plus three HTML pages from the starting code: `/` (public), `/dashboard` (role USER) and `/admindashboard` (role ADMIN).

## Setup

Same database setup as lab 15: copy `src/main/resources/db.properties.example` to `db.properties` and fill in your MySQL root password (the file is in `.gitignore`). It uses the local MySQL server by default; for Aiven, use the commented lines instead. Run with `mvn clean spring-boot:run` or Run As → Spring Boot App.

## What was added

- **Dependencies:** `spring-boot-devtools`, `spring-boot-starter-security`, `spring-security-test`, and Thymeleaf with its Spring Security extras for the pages.
- **`security/WebSecurityConfig`** (`@Configuration`, `@EnableWebSecurity`) with three beans:
  - `securityFilterChain`: the access rules below, form login and logout.
  - `userDetailsService`: two in-memory users.
  - `passwordEncoder`: BCrypt, so the passwords are stored hashed.
- **`controllers/PagesController`** and the templates `index.html`, `dashboard.html`, `adminDashboard.html`.

| User | Password | Role |
|---|---|---|
| `usera` | `pwd123` | USER |
| `admin` | `admin123` | ADMIN |

| Route | Who can access |
|---|---|
| `/` | everyone, no login |
| `/dashboard/**` | role USER |
| `/admindashboard/**` | role ADMIN |
| everything else, including `/api/v1/courses/**` | any logged-in user |

Not logged in → redirect (302) to `/login`. Logged in with the wrong role → 403 Forbidden.

## Step 1: before the config (default security)

With only the security dependency added (no `WebSecurityConfig`), every endpoint needs a login. The user is `user` and the password is printed in the console at startup (`Using generated security password: ...`). In PutMan, use Basic Auth with those values.

Once `WebSecurityConfig` exists, that generated user is gone and the users above replace it.

## Step 2: logging in from PutMan (with CSRF)

1. `GET http://localhost:8080/api/v1/courses` → you are redirected to `/login`.
2. `GET http://localhost:8080/login` → the HTML form contains a hidden input:
   `<input name="_csrf" type="hidden" value="...">`. Copy the value.
3. `POST http://localhost:8080/login` with body type **x-www-form-urlencoded**:
   `username=usera`, `password=pwd123`, `_csrf=<the value>`.
   Response: 302 back to the page you asked for. Without `_csrf` the login is refused with 403.
4. `GET http://localhost:8080/api/v1/courses` → 200 with the courses.

The login is kept by the `JSESSIONID` cookie. If PutMan does not keep cookies between requests, copy `JSESSIONID` from the `Set-Cookie` header of the step 2 response and send it in a `Cookie: JSESSIONID=...` header on steps 3 and 4.

**POST, PUT and DELETE** on the API also need the CSRF token (CSRF protection covers every request that changes data). The token changes when you log in, so first `GET /login` again (while logged in), copy the new `_csrf` value, and send it as the header `X-CSRF-TOKEN: <value>`. Without it the response is 403.

## Step 3: browser

- Open http://localhost:8080/ in a new private window: it opens without logging in.
- Click "Go to dashboard" → login page → `usera` / `pwd123` → "Hello usera!".
- Click "Sign Out", then go to http://localhost:8080/admindashboard as `usera` → 403. As `admin` / `admin123` → "Hello admin!".

## Tested results

| Request | Result |
|---|---|
| `GET /api/v1/courses`, not logged in | 302 → `/login` |
| `GET /`, not logged in | 200 |
| `POST /login` without `_csrf` | 403 |
| `POST /login` wrong password | 302 → `/login?error` |
| `POST /login` usera + `_csrf` | 302, logged in |
| `GET /api/v1/courses` as usera | 200 |
| `GET /dashboard` as usera | 200, "Hello usera!" |
| `GET /admindashboard` as usera | 403 |
| `GET /admindashboard` as admin | 200, "Hello admin!" |
| `GET /dashboard` as admin | 403 |
| `POST /api/v1/courses` without `X-CSRF-TOKEN` | 403 |
| `POST` / `PUT` / `DELETE` with `X-CSRF-TOKEN` | 201 / 204 / 204 |
| `POST /logout`, then `GET /api/v1/courses` | 302 → `/login` |

`/error` is also public, so error responses (403, 404) are shown as they are instead of sending you to the login page.
