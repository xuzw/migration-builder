package com.beekeeperdata.migrationbuilder;


import com.beekeeperdata.migrationbuilder.serializers.H2Serializer;
import com.beekeeperdata.migrationbuilder.serializers.PostgresSerializer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Migration {

  private final List<Table> createdTables = new ArrayList<Table>();
  private final List<Index> createdIndexes = new ArrayList<Index>();
    private final Map<String,List<Column>> addedColumns = new HashMap<String, List<Column>>();
    private List<String> droppedTables = new ArrayList<String>();

    public Migration() {

  }


  private Serializer getSerializer(Connection c) throws SQLException {
    MigrationType migrationType = parseMigrationType(c);
    switch(migrationType) {
      case POSTGRES:
        return new PostgresSerializer();
      case MYSQL:
      default:
        return new H2Serializer();
    }
  }

  protected MigrationType parseMigrationType(Connection c) throws SQLException {
      String driverName = DriverManager.getDriver(c.getMetaData().getURL()).getClass().getName();
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

  public Migration dropTable(String name) {
      this.droppedTables.add(name);
      return this;
  }

  public Index createIndex(String table, String... columns) {
      String[] columnArray = columns;
      Index index = new Index(table, columns);
      this.createdIndexes.add(index);
      return index;
  }

  public Migration addColumn(String table, String name, C type, boolean notNull, String defaultValue) {
      if(!this.addedColumns.containsKey(table)) {
          this.addedColumns.put(table, new ArrayList<Column>());
      }
      this.addedColumns.get(table).add(new Column(name, type, notNull, defaultValue));
      return this;
  }

  public Migration addColumn(String table, String name, C type) {
      return this.addColumn(table, name, type, false, null);
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

  public Map<String, List<Column>> getAddedColumns() {
      return this.addedColumns;
  }

    public List<String> getDroppedTables() {
        return droppedTables;
    }

    //public Table updateTable(String name);
  //public Index createIndex(name);

}



