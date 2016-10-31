package com.beekeeperdata.migrationbuilder.serializers;

import com.beekeeperdata.migrationbuilder.*;
import org.apache.commons.lang3.StringUtils;

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
        String columns = StringUtils.join(serializedColumns, ",");
        innerTableSQL += columns;
        String primaryKey = "";
        if(t.getPrimaryKey() != null) {
            primaryKey = String.format("PRIMARY KEY(%s)", t.getPrimaryKey());
            innerTableSQL += ", " + primaryKey;
        }

        return String.format("CREATE TABLE %s(%s);", t.getName(), innerTableSQL);

    }

    @Override
    public String createIndex(Index i) {
        String columnsString = StringUtils.join(i.getColumns(), ", ");
        String template = "CREATE INDEX %s ON %s(%s);";
        String result = String.format(template, i.getName(), i.getTable(), columnsString);

        return result;
    }


    @Override
    protected String typeToString(C columnType) {
        switch (columnType) {
            case STRING:
                return "VARCHAR(255)";
            case DATETIME:
                return "DATETIME";
            case AUTOINC:
                return "BIGINT NOT NULL AUTO_INCREMENT";
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
        if(c.getNotNull() && c.getColumnType() != C.AUTOINC) {
            // dont want to add this to AUTOINC columns, as it comes as standard.
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

        for(Index i: migration.getCreatedIndexes()) {
            result += this.createIndex(i);
        }

        return result;
    }
}
