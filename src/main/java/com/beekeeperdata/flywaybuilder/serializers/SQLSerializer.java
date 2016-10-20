package com.beekeeperdata.flywaybuilder.serializers;

import com.beekeeperdata.flywaybuilder.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rathboma on 10/19/16.
 */
public abstract class SQLSerializer extends Serializer {
    @Override
    public String createTable(Table t) {
        List<String> serializedColumns = new ArrayList<String>();
        for (Column column : t.getCreatedColumns()) {
            serializedColumns.add(this.column(column));

        }
        String innerTableSQL = "";
        String columns = String.join(",", serializedColumns);
        innerTableSQL += columns;
        String primaryKey = "";
        if(t.getPrimaryKey() != null) {
            primaryKey = String.format("PRIMARY KEY(%s)", t.getPrimaryKey());
            innerTableSQL += ", " + primaryKey;
        }

        return String.format("CREATE TABLE %s(%s);", t.getName(), innerTableSQL);

    }


    @Override
    protected String typeToString(C columnType) {
        switch (columnType) {
            case STRING:
                return "VARCHAR(255)";
            case DATETIME:
                return "DATETIME";
            case AUTOINC:
                return "BIGINT AUTO_INCREMENT";
            case INT:
            case INTEGER:
                return "INTEGER";
            case DOUBLE:
                return "DOUBLE";
            case FLOAT:
                return "FLOAT";
            case BIGINT:
                return "BIGINT";
            case TEXT:
                return "TEXT";
            default:
                throw new RuntimeException("Not implemented");
        }
    }


    @Override
    protected String column(Column c) {
        String result = String.format("%s %s", c.getName(), this.typeToString(c.getColumnType()));
        if(c.getNotNull()) {
            result += " NOT NULL";
        }
        if(c.hasDefaultValue()) {
            result += (" DEFAULT " + defaultValue(c));
        }
        return result;
    }

    @Override
    protected String defaultValue(Column c) {
        C cType = c.getColumnType();
        String value = c.getDefaultValue();
        switch(cType) {
            case STRING:
                return String.format("'%s'", value);
            case DATETIME:
                if(c.hasDefaultCurrentTimestamp()) {
                    return "CURRENT_TIMESTAMP";
                } // else do the default
            default:
                return value;
        }
    }

    @Override
    public String serialize(Migration migration) {
        String result = "";
        for (Table t: migration.getCreatedTables()) {
            result += this.createTable(t);
        }
        return result;
    }
}
