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
  .addColumn('name', C.String)
  .addColumn('email', C.String, true)
  .addColumn('birthday', C.Date)
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
### Using Migration Builder with Flyway

TBD

### Who Should use Migration Builder?

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
