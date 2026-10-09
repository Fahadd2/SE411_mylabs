# Lab 06: Logging with SLF4J

SLF4J is the logging facade; `slf4j-log4j12` is the concrete library behind it. Logs go to `logs/App/log4j/log.out`.

Run: `mvn clean package exec:java`

## Logging strategy

| Event | Level | Where |
|---|---|---|
| Application starts / ends | INFO | `App.main` (end is in a `finally`, so it is logged even if something crashes) |
| Wallet account created | DEBUG | `WalletAccount` constructor |
| Deposit / withdraw | DEBUG | `WalletAccount.deposit` / `withdraw` (request and result) |
| `InsufficientFundsException` created | WARN | `InsufficientFundsException` constructor |
| Thrown exception | ERROR | the `catch` blocks in `App` |

Changing `log4j.rootLogger` in `log4j.properties`:
- `ERROR`: only the two caught exceptions
- `INFO`: plus application start and end
- `DEBUG`: everything

## AI review: recommendations applied

1. **Parameterized messages** (`logger.debug("amount={}", amount)`) instead of string concatenation, so the message is only built when that level is enabled.
2. **Pass the exception object to `logger.error(msg, e)`**, so the stack trace is logged, not just the message.
3. **Log each exception once, where it is handled** (the `catch` in `App`), not also where it is thrown. This avoids duplicate entries for the same error.
4. **Include context** (amount and balance) in deposit and withdraw messages, so a log line explains itself without the code.
5. **One `private static final` logger per class**, so each line shows which class wrote it (`%C`).
