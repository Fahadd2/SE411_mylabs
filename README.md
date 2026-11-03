# Introduction
This is a repository for my solutions to SE411 labs

# Chapters

## Chapter 01: Git
The first chapter is an introduction to git.

## Lab 05: Exception handling
Custom exceptions (`InvalidAgeException`, `InsufficientFundsException`) demonstrated with an age validator and an online wallet.

## Lab 06: Logging with SLF4J
The wallet starter code with SLF4J logging (log4j12 backend) at INFO, DEBUG, WARN and ERROR levels, written to a log file. See `lab06/README.md` for the logging strategy. Run with `mvn clean package exec:java` inside `lab06/`.

## Lab 07: Polymorphism
A travel agency booking system: flight, train and car rental bookings share an abstract `Booking` and each computes its own price. Custom `MissingInformationException` / `InvalidArgumentException`, a global `Config` class and file logging. Run with `mvn clean package exec:java` inside `lab07/`.

## Lab 08: Observer pattern
Temperature and humidity sensors (subjects) notify a dashboard and a logger (observers) when their readings change. Sensors are cloneable; a clone starts with its own, empty observer list. Run with `mvn clean package exec:java` inside `lab08/`.

## Lab 09: Inventory with the strategy pattern
A generic `Inventory<T>` holding books and electronic devices, searched with `SearchStrategy` implementations (by id, name, author, category) and lambdas. `displayInventory(Inventory<?>)` prints any inventory. Run with `mvn clean package exec:java` inside `lab09/`.

## Lab 11: Threads
The double-spending problem: random money transfers between crypto accounts on a thread pool, with `synchronized` keeping the total money constant. Code is in `lab11_threads/`.

## Lab 12: Threads and locks
Fixed vs cached thread pool benchmark (1000 and 10 accounts, one million transactions), `synchronized` replaced by `ReentrantLock`, a deadlock demo, and the fix with resource ordering. Answers to the lab questions are in `lab12/README.md`. Run with `mvn clean package exec:java` inside `lab12/`.

## Lab 13: Spring introduction
A Product web API with Spring Boot 4.1.2, Spring Data JDBC and an H2 in-memory database: `GET /api/v1/products` and `GET /api/v1/products/{id}`, `BigDecimal` prices and input validation. Run with `mvn clean spring-boot:run` inside `lab13/`.

## Lab 14: Spring CRUD
A Course web API with Spring Boot 4.0.5 and all CRUD operations (POST, GET, PUT, DELETE) with the designed status codes (201, 200, 204, 400, 404, 422) and POST validation. See `lab14/README.md` for the API table and Postman test requests. Run with `mvn clean spring-boot:run` inside `lab14/`.
