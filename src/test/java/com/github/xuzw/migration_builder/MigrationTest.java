package com.github.xuzw.migration_builder;

import junit.framework.TestCase;
import org.junit.Assert;

import com.github.xuzw.migration_builder.Migration;
import com.github.xuzw.migration_builder.MigrationType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by rathboma on 10/19/16.
 */
public class MigrationTest extends TestCase {

    public class TestMigration extends Migration {
        public MigrationType parseType(Connection c) throws SQLException {
            return this.parseMigrationType(c);
        }
    }

    public void testParseMigrationType() throws Exception {
        Migration m = new Migration();
    }


    public void testConnectionParsingBasics() throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:./target/test.h2.db");
        TestMigration m = new TestMigration();
        MigrationType t = m.parseType(conn);
        Assert.assertEquals(t, MigrationType.H2);
    }

}