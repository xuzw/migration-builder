package com.beekeeperdata.flywaybuilder;


import com.beekeeperdata.flywaybuilder.serializers.H2Serializer;
import com.beekeeperdata.flywaybuilder.serializers.PostgresSerializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Migration {

  private final MigrationType migrationType;
  private final List<Table> createdTables = new ArrayList<Table>();
  private final List<Index> createdIndexes = new ArrayList<Index>();


  private Serializer getSerializer() {
    switch(this.migrationType) {
      case POSTGRES:
        return new PostgresSerializer();
      default:
        return new H2Serializer();
    }
  }

  public Migration(MigrationType migrationType) {
    this.migrationType = migrationType;
  }

  public Table createTable(String name) {
    Table table = new Table(name);
    this.createdTables.add(table);
    return table;
  }

  public void run(Connection connection) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      statement.execute(this.toSql());
    } finally {
      statement.close();
    }
  }

  public String toSql() {
    Serializer s = getSerializer();
    return s.serialize(this);
  }

  public List<Table> getCreatedTables() {
    return createdTables;
  }

  //public Table updateTable(String name);
  //public Index createIndex(name);

}



