package com.beekeeperdata.migrationbuilder;

/**
 * Created by rathboma on 11/3/16.
 */
public class ForeignKey {
    private final String column;
    private final String foreignTable;
    private final String foreignColumn;
    private final FK[] options;
    private final String table;


    public ForeignKey(String table, String column, String fTable, String fKey, FK... options) {
        this.table = table;
        this.column = column;
        this.foreignTable = fTable;
        this.foreignColumn = fKey;
        this.options = options;
    }

    public String getColumn() {
        return column;
    }

    public String getForeignTable() {
        return foreignTable;
    }

    public String getForeignColumn() {
        return foreignColumn;
    }

    public FK[] getOptions() {
        return options;
    }

    public String getTable() {
        return table;
    }

    public String getConstraintName() {
        return String.format("FK_%s_%s", this.getTable(), this.getColumn());
    }
}
