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
