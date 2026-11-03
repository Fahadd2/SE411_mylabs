# Lab 12: Threads and locks

Built on the lab 11 solution (the double-spending demo). Run with Maven:

| Program | Command |
|---|---|
| Fixed thread pool | `mvn clean package exec:java` |
| Cached thread pool | `mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.CachedPoolApp` |
| Deadlock vs resource ordering | `mvn clean package exec:java -Dexec.mainClass=edu.psu.se411.lab12_threads.DeadlockDemo` |

The shared simulation code (creating accounts, random transactions, waiting, checking the total money) lives in `Simulation.java`, so the three programs only choose a thread pool and a transaction type.

## Part 1: fixed vs cached thread pool

The starter created `newFixedThreadPool(nbTransactions)`, which means one thread per transaction (a million threads for this lab). The fixed pool now has one thread per CPU core (12 on the test machine).

Results for 1,000,000 transactions (3 runs each, 12-core machine):

| Case | Fixed pool (12 threads) | Cached pool | Cached pool peak threads |
|---|---|---|---|
| 1000 accounts | ~1.3–1.4 s | ~2.2 s | ~25 |
| 10 accounts | ~1.4–1.6 s | ~2.7 s | 270–740 |

**What do you observe?**
- **The fixed pool is faster in both cases.** Each transaction is tiny, so the work is CPU-bound. More threads than cores can't run at the same time; they just add thread creation and context-switching costs.
- **The cached pool creates a thread whenever no thread is idle**, with no upper limit. With 1000 accounts, transactions rarely wait on each other, so threads free up quickly and the pool stays small (~25 threads).
- **With 10 accounts, almost every transaction competes for the same few locks.** Threads block while waiting, the cached pool sees no idle thread and creates more, and those also block. It reached hundreds of threads, and the run was the slowest of the four.
- **The fixed pool behaves the same in both cases.** It never has more than 12 threads; extra tasks simply wait in its queue.
- **Conclusion:** use a fixed pool sized to the CPU for many short CPU-bound tasks. A cached pool suits short tasks that mostly wait on I/O, and it can grow out of control under lock contention.
- **Money is conserved** in every run: the total before equals the total after.

## Part 2: `synchronized` replaced by locks

`CryptoAccount` now uses a `ReentrantLock` instead of `synchronized`. A transfer must hold **both** accounts' locks at once. Otherwise the balance check and the two updates are separate steps, and another thread can spend the same money between them (the original double-spending problem).

**What could go wrong? Deadlock.** If each transaction locks the sender first, then the receiver:
- Thread 1 runs A → B: it locks **A**, then waits for **B**.
- Thread 2 runs B → A: it locks **B**, then waits for **A**.

Each waits for the other forever. Running `DeadlockDemo` with `UnorderedTransaction` shows it: within a few seconds the JVM reports a cycle of threads waiting on each other's locks, and every other thread in the pool gets stuck behind them. Output, shortened:

```
DEADLOCK detected between 3 threads:
  pool-1-thread-7 waits for ReentrantLock@e745d71 held by pool-1-thread-5
  pool-1-thread-5 waits for ReentrantLock@567f0571 held by pool-1-thread-7
```

The program can still stop cleanly because the locks are taken with `lockInterruptibly()`. `shutdownNow()` interrupts the stuck threads, and their transactions are cancelled before any money moves.

## Solution: resource ordering

`Transaction.firstToLock()` defines a **total order on accounts by id**, and every transaction locks the smaller id first, whichever account is the sender. All threads then request locks in the same order, so a circular wait, and therefore a deadlock, cannot happen. The same 10-account run then completes in about 1.5 s with the money conserved.

(Account ids are compared as strings, so "acc_10" sorts before "acc_9". That's fine: any order works as long as every thread uses the same one.)
