# Migration Builder

Migration Builder lets you build SQL migrations using a cross-database java dsl. So your migrations work across all supported databases without any extra work.

Migration Builder is designed to be used with [Flyway](https://flywaydb.org/) for migration versioning.

## Example

```java

Migration m = new Migration();

m.createTable('example')
  .addPKColumn('id')
  .addColumn('name', C.String)
  .addColumn('email', C.String)
  .addColumn('birthday', C.Date)

 m.addIndex('example', 'email')

 m.run(this.getConnection())

```

This generates the below SQL, notice the platform specific differences are accounted for:

**TODO**


## Supported Databases

- PostgreSQL
- MySQL
- H2