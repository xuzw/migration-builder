package com.github.xuzw.migration_builder;

/**
 * Created by rathboma on 10/19/16.
 */
public abstract class Serializer {
    protected abstract String createTable(Table t);

    protected abstract String foreignKey(ForeignKey fKey);

    protected abstract String fkOptions(FK[] options);

    protected abstract String createIndex(Index i);

    protected abstract String column(Column c);

    protected abstract String typeToString(C columnType);

    protected abstract String defaultValue(Column c);

    protected abstract String addColumn(String table, Column c);

    protected abstract String addFK(ForeignKey key);

    protected abstract String dropFK(ForeignKey key);

    public abstract String serialize(Migration migration);

    public abstract String droppedTable(String t);
}
