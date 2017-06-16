package com.github.xuzw.migration_builder.serializers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.xuzw.migration_builder.AutoIncrement;
import com.github.xuzw.migration_builder.C;
import com.github.xuzw.migration_builder.Column;
import com.github.xuzw.migration_builder.FK;
import com.github.xuzw.migration_builder.ForeignKey;
import com.github.xuzw.migration_builder.Index;
import com.github.xuzw.migration_builder.Migration;
import com.github.xuzw.migration_builder.Serializer;
import com.github.xuzw.migration_builder.Table;

/**
 * Created by rathboma on 10/19/16.
 */
public abstract class SQLSerializer extends Serializer {
    @Override
    public String createTable(Table t) {
        List<String> serializedColumns = new ArrayList<String>();
        List<String> results = new ArrayList<String>();
        for (Column column : t.getCreatedColumns()) {
            serializedColumns.add(this.column(column));
        }
        results.addAll(serializedColumns);
        if (t.getPrimaryKey() != null) {
            results.add(String.format("PRIMARY KEY(%s)", t.getPrimaryKey()));
        }
        for (String uniqueKeys : t.getUniqueKeys()) {
            results.add(String.format("UNIQUE (%s)", uniqueKeys));
        }
        for (ForeignKey fKey : t.getForeignKeys()) {
            results.add(foreignKey(fKey));
        }
        String resultString = StringUtils.join(results, ",");
        return String.format("CREATE TABLE %s(%s);", t.getName(), resultString);

    }

    @Override
    protected String foreignKey(ForeignKey fKey) {
        String template = "CONSTRAINT %s FOREIGN KEY(%s) REFERENCES %s(%s) %s";
        return String.format(template, fKey.getConstraintName(), fKey.getColumn(), fKey.getForeignTable(), fKey.getForeignColumn(), fkOptions(fKey.getOptions()));
    }

    @Override
    protected String fkOptions(FK[] options) {
        List<String> results = new ArrayList<String>();
        for (FK option : options) {
            switch (option) {
            case CASCADEDELETE:
                results.add("ON CASCADE DELETE");
            }
        }
        return StringUtils.join(results, " ");
    }

    @Override
    public String createIndex(Index i) {
        String columnsString = StringUtils.join(i.getColumns(), ", ");
        String template = "CREATE INDEX %s ON %s(%s);";
        String result = String.format(template, i.getName(), i.getTable(), columnsString);
        return result;
    }

    @Override
    public String droppedTable(String table) {
        return String.format("DROP TABLE %s;", table);
    }

    @Override
    public String autoIncrement(AutoIncrement autoIncrement) {
        return String.format("ALTER TABLE %s AUTO_INCREMENT=%s;", autoIncrement.getTable(), autoIncrement.getBegin());
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
        case BOOLEAN:
            return "BOOLEAN";
        default:
            throw new RuntimeException("Not implemented");
        }
    }

    @Override
    protected String column(Column c) {
        String result = String.format("%s %s", c.getName(), this.typeToString(c.getColumnType()));
        if (c.getNotNull() && c.getColumnType() != C.AUTOINC) {
            // dont want to add this to AUTOINC columns, as it comes as
            // standard.
            result += " NOT NULL";
        }
        if (c.hasDefaultValue()) {
            result += String.format(" DEFAULT %s", defaultValue(c));
        }
        if (StringUtils.isNoneBlank(c.getComment())) {
            result += String.format(" COMMENT '%s'", c.getComment());
        }
        return result;
    }

    @Override
    protected String defaultValue(Column c) {
        C cType = c.getColumnType();
        String value = c.getDefaultValue();
        switch (cType) {
        case STRING:
            return String.format("'%s'", value);
        case DATETIME:
            if (c.hasDefaultCurrentTimestamp()) {
                return "CURRENT_TIMESTAMP";
            } // else do the default
        default:
            return value;
        }
    }

    @Override
    protected String addColumn(String table, Column column) {
        return String.format("ALTER TABLE %s ADD %s;", table, this.column(column));
    }

    @Override
    protected String addFK(ForeignKey key) {
        return String.format("ALTER TABLE %s ADD %s;", key.getTable(), this.foreignKey(key));
    }

    @Override
    protected String dropFK(ForeignKey key) {
        // different for mysql (of course)
        return String.format("ALTER TABLE %s DROP CONSTRAINT %s;", key.getTable(), key.getConstraintName());
    }

    @Override
    public String serialize(Migration migration) {
        String result = "";
        for (Table t : migration.getCreatedTables()) {
            result += this.createTable(t);
        }
        for (String t : migration.getAddedColumns().keySet()) {
            List<Column> columns = migration.getAddedColumns().get(t);
            for (Column c : columns) {
                result += this.addColumn(t, c);
            }
        }
        for (ForeignKey foreignKey : migration.getAddedFKs()) {
            result += this.addFK(foreignKey);
        }
        for (ForeignKey foreignKey : migration.getRemovedFKs()) {
            result += this.dropFK(foreignKey);
        }
        for (String t : migration.getDroppedTables()) {
            result += this.droppedTable(t);
        }
        for (AutoIncrement a : migration.getAutoIncrements()) {
            result += this.autoIncrement(a);
        }
        for (Index i : migration.getCreatedIndexes()) {
            result += this.createIndex(i);
        }
        return result;
    }
}
