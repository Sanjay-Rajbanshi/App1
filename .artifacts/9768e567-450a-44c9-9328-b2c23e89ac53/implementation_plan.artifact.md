# Fix warnings and errors in Transaction.java and related Room data classes

The user reported warnings and errors in `Transaction.java`. Research revealed that while `Transaction.java` itself mostly has "unused method" warnings, `AppData.java` has a critical error where it imports the wrong `Transaction` class (the Room annotation instead of the Entity).

## Proposed Changes

### [roomdata component](file:///E:/MyFirstApplication/app/src/main/java/com/example/myfirstapplication/data/roomdata)

#### [MODIFY] [Transaction.java](file:///E:/MyFirstApplication/app/src/main/java/com/example/myfirstapplication/data/roomdata/Transaction.java)
- Change fields from `public` to `private` for better encapsulation.
- Keep getters and setters (now required due to private fields).
- Add a secondary constructor without the `tid` parameter to facilitate creating new instances (since `tid` is auto-generated).
- Fix indentation and formatting.

#### [MODIFY] [TransactionDao.java](file:///E:/MyFirstApplication/app/src/main/java/com/example/myfirstapplication/data/roomdata/TransactionDao.java)
- Rename `InsertTransaction` to `insertTransaction` (Java naming convention).
- Rename `deleteAllTransaction` to `deleteAllTransactions` for consistency.
- Ensure SQL queries are correct (this should be resolved once `AppData.java` is fixed).

## Verification Plan

### Automated Tests
- Run `gradle_build` to ensure the project compiles successfully after the changes.
- Specifically, check if Room annotation processor still works correctly with the `private` fields and getters/setters.

### Manual Verification
- None required as these are structural and compile-time fixes.
