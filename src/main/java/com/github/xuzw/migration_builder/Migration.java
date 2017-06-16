package com.github.xuzw.migration_builder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.xuzw.migration_builder.serializers.MySqlSerializer;

public class Migration {
    private final List<Table> createdTables = new ArrayList<Table>();
    private final List<Index> createdIndexes = new ArrayList<Index>();
    private final Map<String, List<Column>> addedColumns = new HashMap<String, List<Column>>();
    private List<ForeignKey> addedFKs = new ArrayList<ForeignKey>();
    private List<String> droppedTables = new ArrayList<String>();
    private List<AutoIncrement> autoIncrements = new ArrayList<AutoIncrement>();

    public List<ForeignKey> getAddedFKs() {
        return addedFKs;
    }

    public List<ForeignKey> getRemovedFKs() {
        return removedFKs;
    }

    private List<ForeignKey> removedFKs = new ArrayList<ForeignKey>();

    public Migration() {
    }

    private Serializer getSerializer(Connection c) throws SQLException {
        MigrationType migrationType = parseMigrationType(c);
        switch (migrationType) {
        case MYSQL:
            return new MySqlSerializer();
        default:
            return null;
        }
    }

    protected MigrationType parseMigrationType(Connection c) throws SQLException {
        String driverName = DriverManager.getDriver(c.getMetaData().getURL()).getClass().getName();
        if (driverName.equals("com.mysql.jdbc.Driver")) {
            return MigrationType.MYSQL;
        }
        throw new RuntimeException("unrecognized connection type");
    }

    public Table createTable(String name) {
        Table table = new Table(name);
        this.createdTables.add(table);
        return table;
    }

    public Migration dropTable(String name) {
        this.droppedTables.add(name);
        return this;
    }

    public Migration autoIncrement(String table, long begin) {
        this.autoIncrements.add(new AutoIncrement(table, begin));
        return this;
    }

    public Index createIndex(String table, String... columns) {
        Index index = new Index(table, columns);
        this.createdIndexes.add(index);
        return index;
    }

    public Migration addColumn(String table, String name, String comment, C type, boolean notNull, String defaultValue) {
        if (!this.addedColumns.containsKey(table)) {
            this.addedColumns.put(table, new ArrayList<Column>());
        }
        this.addedColumns.get(table).add(new Column(name, comment, type, notNull, defaultValue));
        return this;
    }

    public Migration addColumn(String table, String name, String comment, C type) {
        return this.addColumn(table, name, comment, type, false, null);
    }

    public Migration addForeignKey(String table, String column, String foreignTable, String foreignColumn, FK... options) {
        this.addedFKs.add(new ForeignKey(table, column, foreignTable, foreignColumn, options));
        return this;
    }

    public Migration removeForeignKey(String table, String column) {
        this.removedFKs.add(new ForeignKey(table, column, null, null));
        return this;
    }

    public void run(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            Serializer s = getSerializer(connection);
            String sql = s.serialize(this);
            statement.execute(sql);
        } finally {
            statement.close();
        }
    }

    public List<Table> getCreatedTables() {
        return createdTables;
    }

    public List<Index> getCreatedIndexes() {
        return createdIndexes;
    }

    public Map<String, List<Column>> getAddedColumns() {
        return this.addedColumns;
    }

    public List<String> getDroppedTables() {
        return droppedTables;
    }

    public List<AutoIncrement> getAutoIncrements() {
        return autoIncrements;
    }
}
