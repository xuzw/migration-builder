package com.github.xuzw.migration_builder;


import java.util.ArrayList;
import java.util.List;

public class Table {


  private List<Column> createdColumns = new ArrayList<Column>();
  private String primaryKey = null;

  private String name;
  private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();

  public Table(String name) {
    this.name = name;
  }

  public Table addColumn(String name, C type) {
    return this.addColumn(name, type, false, null);
  }

  public Table addColumn(String name, C type, boolean notNull, String defaultValue) {
    Column result = new Column(name, type);
    if(notNull){
      result.notNull();
    }
    result.defaultValue(defaultValue);
    this.createdColumns.add(result);
    return this;
  }

  public Table addPKColumn(String name, C type, boolean notNull, String defaultValue) {
    this.addColumn(name, type, notNull, defaultValue);
    this.setPrimaryKey(name);
    return this;
  }

  public Table addPKColumn(String name, C type) {
    return this.addPKColumn(name, type, false, null);
  }

  public Table addForeignKey(String column, String fTable, String fKey, FK... options) {
    this.foreignKeys.add(new ForeignKey(this.getName(), column, fTable, fKey, options));
    return this;
  }

  public Table addBasics() {

    Column updated = new Column("updated_at", C.DATETIME).notNull().defaultCurrentTimestamp();
    Column created = new Column("created_at", C.DATETIME).notNull().defaultCurrentTimestamp();

    this.addColumn("id", C.AUTOINC)
            .addColumn(created)
            .addColumn(updated);
    this.setPrimaryKey("id");
    return this;
  }

  private Table addColumn(Column created) {
    this.createdColumns.add(created);
    return this;
  }

  public String getName() {
    return name;
  }

  public List<Column> getCreatedColumns(){
    return this.createdColumns;
  }

  public Table setPrimaryKey(String primaryKey) {
    this.primaryKey = primaryKey;
    return this;
  }

  public String getPrimaryKey() {
    return primaryKey;
  }

  public List<ForeignKey> getForeignKeys() {
    return foreignKeys;
  }
}