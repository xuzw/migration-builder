# Migration Builder


[![CircleCI](https://circleci.com/gh/beekeeperdata/migration-builder.svg?style=svg)](https://circleci.com/gh/beekeeperdata/migration-builder)

Migration Builder lets you build SQL migrations using a cross-database java dsl. So your migrations work across all supported databases without any extra work. 

Migration builder is inspired by [Active Record Migrations](http://edgeguides.rubyonrails.org/active_record_migrations.html) from the Ruby on Rails project. I think it is one of the killer features of working in the Ruby on Rails stack.

Migration Builder is designed to be used with [Flyway](https://flywaydb.org/) for migration versioning. So you build your migrations with Migration Builder, and run them with Flyway which will keep track of your database state and make sure you don't run a migration more than once.

Migration Builder works with Flyway 3+. I suggest you use it with the latest version.

## Simple Migration Example

```java

Migration m = new Migration();

m.createTable('example')
  .addPKColumn('id')
  .addColumn('name', C.STRING)
  .addColumn('email', C.STRING, true)
  .addColumn('birthday', C.DATE)
  .addColumn('notes', C.TEXT)

 m.addIndex('example', 'email')

 m.run(this.getConnection())

```

This generates the below SQL, notice the platform specific differences are accounted for:

### H2 / MySQL

```sql
CREATE TABLE example(
 id BIGINT NOT NULL AUTO_INCREMENT,
 name VARCHAR(255),
 email VARCHAR(255) NOT NULL,
 birthday DATETIME,
 notes TEXT,
 PRIMARY KEY(id)
);

CREATE INDEX idx_example_email ON example(email);

```

### POSTGRES

```sql
CREATE TABLE example(
 id SERIAL,
 name VARCHAR(255),
 email VARCHAR(255) NOT NULL,
 birthday TIMESTAMP,
 notes TEXT,
 PRIMARY KEY(id)
);

CREATE INDEX idx_example_email ON example(email);

```

## Supported Databases

- PostgreSQL
- MySQL
- H2
- more coming soon (especially if you help!)

## Getting Started

### Installation

Migration Builder is available through Maven Central. https://mvnrepository.com/artifact/com.beekeeperdata/migrationbuilder. To Include the library as a dependency simply drop the dependency text into your pom.xml, build.scala, or gradle file as per the instructions on Maven Central.

```
Group Name: com.beekeeperdata
Artifact Name: migrationbuilder
```
### Integrating with Flyway

The easiest way to integrate with Flyway it so use a standard [Flyway Java migration](https://flywaydb.org/documentation/migration/java), but instead of typing some raw SQL, just use migration builder to make a migration instead, then pass it the connection object to run it.

Here's the example copy-pasted from the Flyway docs, but with Migration Builder used instead of raw SQL

```java
/**
 * Example of a Java-based migration.
 */
public class V1_2__Create_User implements JdbcMigration {
    public void migrate(Connection connection) throws Exception {
        Migration m = new Migration()
        m.createTable("users")
          .addBasics() // adds id, created_at, updated_at columns
          .addColumn("name", C.STRING)
          .addColumn("email", C.STRING)
        m.run(connection)
    }
}


```

I also recommend implementing the Flyway `MigrationChecksumProvider` to uniquely identify each migration. That way Flyway can detect if you have already run a migration or not. I find a simple way to do this is to provide a date value that represents when you added the file, although you do have to remember to update this value if you change your migration:

```scala
// this is in scala, import org.joda.time.DateTime
override def getChecksum = (new DateTime(2017, 01, 01, 01, 01).getMillis / 1000).toInt
```

## Who Should use Migration Builder?

MB is perfect for those of you building Java, Scala, or Kotlin web apps. Most tables and indexes in this situation are fairly simple and can be easily covered by MB. Using MB doesn't stop you from doing something more complicated in 'proper SQL', so it's a great addition to your toolbelt either way.

### Benefits of using Migration Builder

With Flyway you write migrations in SQL. That means that to support multiple databases you need to rewrite all of your migrations for each specific database. This is tedious, error prone, and a maintenance nightmare.

Migration builder allows you to write migrations ONCE, in Java, and have them run everywhere.

Even with a small team, MB migrations significantly reduce the overhead of writing migrations and iterating on database tables.


## Developer Zone

### Deploying

- make a version branch
- update the maven version to the desired version
- deploy snapshots with `mvn clean deploy`
- deploy releases with `mvn clean deploy -P release` (make sure to update the version first)
