package com.beekeeperdata.flywaybuilder;


public class Column {

  private C columnType;
  private String name;
  private String defaultValue = null;
  private boolean notNull = false;
  private boolean defaultCurrentTimestamp;

  public Column(String name, C t) {
    this.name = name;
    this.columnType = t;
  }

  public Column defaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return this;
  }

  public Column notNull() {
    this.notNull = true;
    return this;
  }

  public C getColumnType() {
    return columnType;
  }

  public boolean getNotNull() {
    return notNull;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean hasDefaultValue() {

    return this.defaultValue != null || this.defaultCurrentTimestamp;
  }

  public String getName() {
    return name;
  }

  public Column defaultCurrentTimestamp() {
    this.defaultCurrentTimestamp = true;
    return this;

  }

  public boolean hasDefaultCurrentTimestamp() {
    return this.defaultCurrentTimestamp;
  }
}