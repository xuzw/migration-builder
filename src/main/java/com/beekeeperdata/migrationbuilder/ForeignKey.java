package com.beekeeperdata.migrationbuilder;

/**
 * Created by rathboma on 11/3/16.
 */
public class ForeignKey {
    private final String column;
    private final String foreignTable;
    private final String foreignColumn;

    public ForeignKey(String column, String fTable, String fKey) {

        this.column = column;
        this.foreignTable = fTable;
        this.foreignColumn = fKey;
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
}
