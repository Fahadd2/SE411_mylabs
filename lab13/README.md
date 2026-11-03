# Lab 13: Spring introduction

A Product web API on Spring Boot 4.1.2 (Spring Web, Spring Data JDBC, H2 in-memory database, validation).

Run: `mvn clean spring-boot:run`, then open http://localhost:8080/api/v1/products

## Structure

| Layer | Class | Role |
|---|---|---|
| Model | `model/Product` | record with `@Id`; price is `BigDecimal` |
| Repository | `repositories/ProductRepository` | extends `CrudRepository` + `PagingAndSortingRepository`, so Spring implements CRUD, paging and sorting |
| Service | `services/ProductService` | business logic; gets the repository by constructor injection |
| Controller | `controllers/ProductController` | REST endpoints under `/api/v1/products`; gets the service by constructor injection |
| Errors | `controllers/ApiExceptionHandler` | turns validation errors into a readable 400 response |

`schema.sql` creates the table and `data.sql` loads 17 products at startup.

## Endpoints

| Request | Result |
|---|---|
| `GET /api/v1/products` | 200, all products as JSON |
| `GET /api/v1/products/10` | 200, product 10 |
| `GET /api/v1/products/99` | 404 Not Found (valid id, but no such product) |
| `GET /api/v1/products/-5` | **400 Bad Request**: `"id must be greater than 0"` |
| `GET /api/v1/products/abc` | 400 Bad Request: `"id must be a number, got 'abc'"` |

## Questions

**Why BigDecimal instead of Double?** A `Double` is stored in binary, so most decimal values are only approximations (0.1 + 0.2 = 0.30000000000000004). For money that means rounding errors. `BigDecimal` stores the exact decimal value, and the column is `DECIMAL(10,2)` (10 digits, 2 after the point) instead of `NUMBER`.

**Validation (`spring-boot-starter-validation`):** the id path variable is annotated `@Positive`. Spring validates it before the method runs.

**Which status code do you get with a negative id?** **400 Bad Request.** Without validation, `/products/-5` would just search the database and return 404 Not Found. With `@Positive`, the request is rejected as invalid input before any database access, and the response says why.
