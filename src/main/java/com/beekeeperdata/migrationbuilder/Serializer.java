package com.beekeeperdata.migrationbuilder;


/**
 * Created by rathboma on 10/19/16.
 */
public abstract class Serializer {

    protected abstract String createTable(Table t);
    protected abstract String createIndex(Index i);
    protected abstract String column(Column c);
    protected abstract String typeToString(C columnType);
    protected abstract String defaultValue(Column c);
    protected abstract String addColumn(String table, Column c);

    public abstract String serialize(Migration migration);

    public abstract String droppedTable(String t);
}


