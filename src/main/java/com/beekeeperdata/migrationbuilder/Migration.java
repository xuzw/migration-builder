package com.beekeeperdata.migrationbuilder;


import com.beekeeperdata.migrationbuilder.serializers.H2Serializer;
import com.beekeeperdata.migrationbuilder.serializers.PostgresSerializer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Migration {

  private final List<Table> createdTables = new ArrayList<Table>();
  private final List<Index> createdIndexes = new ArrayList<Index>();

  public Migration() {

  }


  private Serializer getSerializer(Connection c) throws SQLException {
    MigrationType migrationType = parseMigrationType(c.getMetaData().getDriverName());
    switch(migrationType) {
      case POSTGRES:
        return new PostgresSerializer();
      default:
        return new H2Serializer();
    }
  }

  private MigrationType parseMigrationType(String driverName) {

    if(driverName.equals("com.mysql.jdbc.Driver")) return MigrationType.MYSQL;
    if(driverName.equals("org.postgresql.Driver")) return MigrationType.POSTGRES;
    if(driverName.equals("org.h2.Driver")) return MigrationType.H2;
    throw new RuntimeException("unrecognized connection type");
  }

  public Table createTable(String name) {
    Table table = new Table(name);
    this.createdTables.add(table);
    return table;
  }

  public Index createIndex(String table, String... columns) {
      String[] columnArray = columns;
      Index index = new Index(table, columns);
      this.createdIndexes.add(index);
      return index;
  }

  public void run(Connection connection) throws SQLException {
    Statement statement = connection.createStatement();
    try {
      Serializer s = getSerializer(connection);
      String sql = s.serialize(this);
      statement.execute(sql);
    } finally {
      statement.close();
    }
  }


  public List<Table> getCreatedTables() {
    return createdTables;
  }

  public List<Index> getCreatedIndexes() {
    return createdIndexes;
  }

  //public Table updateTable(String name);
  //public Index createIndex(name);

}



