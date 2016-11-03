package com.beekeeperdata.migrationbuilder.serializers;

import com.beekeeperdata.migrationbuilder.C;
import com.beekeeperdata.migrationbuilder.Migration;
import com.beekeeperdata.migrationbuilder.Serializer;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by rathboma on 10/20/16.
 */
public class H2SerializerTest extends TestCase {

    private Serializer serializer = new H2Serializer();

    public void testBasicSerialization() {
        Migration m = new Migration();
        m.createTable("test")
                .addColumn("name", C.STRING)
                .addColumn("email", C.STRING);

        String result = serializer.serialize(m);
        Assert.assertEquals("CREATE TABLE test(name VARCHAR(255),email VARCHAR(255));", result);

    }
    public void testBasicColumns() throws Exception {
        Migration h2 = new Migration();
        h2.createTable("test")
                .addBasics()
                .addColumn("name", C.STRING);

        String result = this.serializer.serialize(h2);
        Assert.assertEquals("CREATE TABLE test(id BIGINT NOT NULL AUTO_INCREMENT,created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,name VARCHAR(255), PRIMARY KEY(id));",
                result);
    }

    public void testCreateIndex() throws Exception {
        Migration h2 = new Migration();
        h2.createIndex("users", "first_name");
        String result = this.serializer.serialize(h2);
        Assert.assertEquals("CREATE INDEX idx_users_first_name ON users(first_name);", result);
    }

    public void testCreatePK() throws Exception {
        Migration h2 = new Migration();
        h2.createTable("test").addPKColumn("id", C.AUTOINC);
        String result = this.serializer.serialize(h2);
        Assert.assertEquals("CREATE TABLE test(id BIGINT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id));", result);
    }

    public void testAddColumn() throws Exception {
        Migration h2 = new Migration();
        h2.addColumn("users", "first_name", C.STRING)
                .addColumn("users", "last_name", C.STRING);
        String result = this.serializer.serialize(h2);
        String e = "ALTER TABLE users ADD first_name VARCHAR(255);ALTER TABLE users ADD last_name VARCHAR(255);";
        Assert.assertEquals(e, result);
    }

    public void testDropTable() throws Exception {
        Migration h2 = new Migration().dropTable("foo");
        String expected = "DROP TABLE foo;";
        Assert.assertEquals(expected, serializer.serialize(h2));
    }





}