package com.github.xuzw.migration_builder;

public class Column {
    private C columnType;
    private String name;
    private String comment;
    private String defaultValue = null;
    private boolean notNull = false;
    private boolean defaultCurrentTimestamp;

    public Column(String name, String comment, C type, boolean notNull, String defaultValue) {
        this.name = name;
        this.comment = comment;
        this.columnType = type;
        this.notNull = notNull;
        this.defaultValue = defaultValue;
    }

    public Column(String name, String comment, C t) {
        this(name, comment, t, false, null);
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

    public String getComment() {
        return comment;
    }

    public Column defaultCurrentTimestamp() {
        this.defaultCurrentTimestamp = true;
        return this;
    }

    public boolean hasDefaultCurrentTimestamp() {
        return this.defaultCurrentTimestamp;
    }
}