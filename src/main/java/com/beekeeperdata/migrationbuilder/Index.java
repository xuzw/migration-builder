package com.beekeeperdata.migrationbuilder;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by rathboma on 10/19/16.
 */
public class Index {

    private final String table;
    private final String[] columns;
    private final String name;

    public Index(String table, String[] columns) {

        this.table = table;
        this.columns = columns;
        String indexNameTemplate = "idx_%s_%s";
        this.name = String.format(indexNameTemplate,
                table, StringUtils.join(columns, "_")
        );
    }

    public Index(String table, String[] columns, String name) {

        this.table = table;
        this.columns = columns;
        this.name = name;
    }



    public String getTable() {
        return table;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getName() {
        return name;
    }
}
