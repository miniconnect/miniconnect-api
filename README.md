# MiniConnect API

Minimalistic database API.

## Getting started

This API is an alternative to JDBC.
The philosophy is, that a minimalistic database access API should
do two things and nothing more:

- send SQL queries and input data to the server
- accept the results

That's exactly what MiniConnect session API provides.
No odd abstractions like `startTransaction()` or `setCatalog()`.
No JDBC freaks like `nativeSQL()` or `setTypeMap()`.
Just a lightweight, REPL-able SQL interpreter.

Here is a minimal example:

```java
try (MiniSession session = connectionFactory.connect()) {
    MiniResult result = session.execute("SELECT name FROM employees");
    try (MiniResultSet resultSet = result.resultSet()) {
        ImmutableList<MiniValue> row;
        while ((row = resultSet.fetch()) != null) {
            String name = row.get(0).contentAccess().get().toString();
            System.out.println("name: " + name);
        }
    }
}
```

To tell the truth, in practice there is a third one:

- sending large data in an efficient way

For this the `putLargeData()` method can be used:

```java
// ...

session.putLargeData("mylargedata", 20000L, myDataInputStream);

// now, your large data is stored in the @mylargedata SQL variable
```
