# Lab 02: Introduction to Java Generics

Plain Java sources (no build tool needed) under `src/`, one package per exercise.

- `exercise1.PrintableList<T>` — generic list wrapper with a constructor taking an array and a `printAll()` method.
- `exercise2.NumberBox<T extends Number>` — bounded generic wrapper with `setItem`/`getItem`, an `add` method, and a static `sum(List<? extends Number>)`.
- `exercise3` — `Transformer<T, R>` functional interface plus a `Pipeline<T, R>` that composes transformers (same-type or type-changing) and returns a new pipeline on each `addTransformer` call.
- `exercise4.WildcardDemo` — `printList(List<?>)` and `sumNumbers(List<? extends Number>)` wildcard examples.

## Run

```
javac -d out $(find src -name "*.java")
java -cp out exercise1.PrintableList
java -cp out exercise2.NumberBox
java -cp out exercise3.PipelineDemo
java -cp out exercise4.WildcardDemo
```
