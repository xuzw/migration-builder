package com.beekeeperdata.flywaybuilder;

import junit.framework.TestCase;
import org.junit.Assert;


/**
 * Created by rathboma on 10/19/16.
 */
public class MigrationTest extends TestCase {
    public void testCreateTable() throws Exception {

    }

    public void testToSqlSimple() throws Exception {
        Migration m = new Migration(MigrationType.H2);
        m.createTable("test")
                .addColumn("name", C.STRING)
                .addColumn("email", C.STRING);
        String result = m.toSql();
        Assert.assertEquals("CREATE TABLE test(name VARCHAR(255),email VARCHAR(255));", result);

    }

    public void testToSqlPostgres() throws Exception {
        Migration m = new Migration(MigrationType.POSTGRES);
        m.createTable("test")
                .addColumn("created_at", C.DATETIME)
                .addColumn("name", C.STRING);
        String result = m.toSql();
        Assert.assertEquals("CREATE TABLE test(created_at TIMESTAMP,name VARCHAR(255));", result);
    }

    public void testBasicColumns() throws Exception {
        Migration h2 = new Migration(MigrationType.H2);
        h2.createTable("test")
                .addBasics()
                .addColumn("name", C.STRING);
        String result = h2.toSql();
        Assert.assertEquals("CREATE TABLE test(id BIGINT AUTO_INCREMENT,created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,name VARCHAR(255), PRIMARY KEY(id));",
                result);
    }

    public void testRun() throws Exception {

    }

    public void testGetCreateTables() throws Exception {

    }

}