# Lab 14: Spring CRUD

A Course web API with all CRUD operations on Spring Boot 4.0.5 (Spring Web, Spring Data JDBC, H2 in-memory database, validation).

Run: `mvn clean spring-boot:run`. The API is at http://localhost:8080/api/v1/courses

`data.sql` loads 7 courses (ids 1–7) at startup. The database is in memory, so every restart begins with the same 7 courses.

## API design

| DB operation | Route | Method | Response codes |
|---|---|---|---|
| Create a course | `/api/v1/courses` + JSON body | POST | **201** Created + `Location` header · **400** invalid data · **422** id already exists |
| Retrieve all courses | `/api/v1/courses` | GET | **200** |
| Retrieve one course | `/api/v1/courses/{id}` | GET | **200** · **404** |
| Update | `/api/v1/courses/{id}` + JSON body | PUT | **204** No Content · **404** |
| Delete | `/api/v1/courses/{id}` | DELETE | **204** No Content · **404** |

## Validation (`spring-boot-starter-validation`)

The rules are annotations on the `Course` record. They're checked by `@Valid` in the controller on POST, and also on PUT so an update can't store invalid data.

| Field | Rule | Annotation |
|---|---|---|
| id | cannot be negative (optional on POST) | `@PositiveOrZero` |
| code | cannot be empty | `@NotBlank` |
| name | at least 2 characters | `@NotNull @Size(min = 2)` |
| credits | from 1 to 10 | `@NotNull @Min(1) @Max(10)` |

An invalid POST returns 400 with one message per field:
```json
{"status":400,"title":"Bad Request","detail":"Invalid course data",
 "errors":{"id":"cannot be negative","code":"cannot be empty",
           "name":"must be at least 2 characters long","credits":"must be between 1 and 10"}}
```

## Postman test requests

For POST and PUT, set Body → raw → JSON. Run them in this order on a freshly started server.

| # | Request | Body | Expected |
|---|---|---|---|
| 1 | `GET /api/v1/courses` | | 200, 7 courses |
| 2 | `GET /api/v1/courses/5` | | 200, SE411 |
| 3 | `GET /api/v1/courses/99` | | 404 |
| 4 | `POST /api/v1/courses` | `{"code":"SE412","name":"Software Testing","description":"Testing techniques","credits":3}` | 201, `Location: /api/v1/courses/8` |
| 5 | `POST /api/v1/courses` | `{"id":50,"code":"SE499","name":"Capstone","credits":6}` | 201, `Location: /api/v1/courses/50` |
| 6 | `POST /api/v1/courses` | `{"id":5,"code":"X1","name":"Duplicate","credits":3}` | 422, id 5 already exists |
| 7 | `POST /api/v1/courses` | `{"id":-3,"code":"","name":"A","credits":11}` | 400, all four validation errors |
| 8 | `PUT /api/v1/courses/5` | `{"code":"SE411","name":"Software Construction II","description":"Updated","credits":4}` | 204 |
| 9 | `PUT /api/v1/courses/99` | `{"code":"X","name":"Nope","credits":3}` | 404 |
| 10 | `DELETE /api/v1/courses/7` | | 204 |
| 11 | `DELETE /api/v1/courses/7` | | 404 (already deleted) |

## Design notes

- **POST with an id:** Spring Data's `save()` treats any course with an id as an existing row and tries an UPDATE. So when the client sends a new id, `CourseService.create` checks whether it exists (422 if it does) and inserts it explicitly with `JdbcAggregateTemplate.insert`.
- **PUT:** the id in the URL is used, even if the body contains a different one.
- **Errors:** all error responses use Spring's standard `ProblemDetail` JSON format, built in `ApiExceptionHandler`.
