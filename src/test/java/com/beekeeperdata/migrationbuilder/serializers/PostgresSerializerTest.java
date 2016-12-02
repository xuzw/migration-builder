package com.beekeeperdata.migrationbuilder.serializers;

import com.beekeeperdata.migrationbuilder.C;
import com.beekeeperdata.migrationbuilder.FK;
import com.beekeeperdata.migrationbuilder.Migration;
import com.beekeeperdata.migrationbuilder.Serializer;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by rathboma on 10/20/16.
 */
public class PostgresSerializerTest extends TestCase {

    private PostgresSerializer serializer = new PostgresSerializer();

    public void testPostgresDate() {
        Migration m = new Migration();
        m.createTable("test")
                .addColumn("created_at", C.DATETIME)
                .addColumn("name", C.STRING);
        Serializer s = new PostgresSerializer();
        String result = s.serialize(m);
        Assert.assertEquals("CREATE TABLE test(created_at TIMESTAMP,name VARCHAR(255));", result);
    }

    public void testPostgresIdField() {
        Migration m = new Migration();
        m.createTable("test")
                .addPKColumn("id", C.AUTOINC)
                .addColumn("name", C.STRING);
        Serializer s = new PostgresSerializer();
        String result = s.serialize(m);
        Assert.assertEquals("CREATE TABLE test(id SERIAL,name VARCHAR(255),PRIMARY KEY(id));", result);

    }

    public void testAddFK() {
        Migration m = new Migration().addForeignKey("users", "client_id", "clients", "id");
        Serializer s = new PostgresSerializer();
        String result = s.serialize(m);
        Assert.assertEquals("ALTER TABLE users ADD CONSTRAINT FK_users_client_id FOREIGN KEY(client_id) REFERENCES clients(id) ;", result);
    }

    public void testFKOptions() {
        Migration m = new Migration().addForeignKey("users", "client_id", "clients", "id", FK.CASCADEDELETE);
        Serializer s = new PostgresSerializer();
        String result = s.serialize(m);
        Assert.assertEquals("ALTER TABLE users ADD CONSTRAINT FK_users_client_id FOREIGN KEY(client_id) REFERENCES clients(id) ON CASCADE DELETE;", result);

    }
    public void testDropFK() {
        Migration m = new Migration();
        m.removeForeignKey("users", "client_id");
        String result = serializer.serialize(m);
        String expected = "ALTER TABLE users DROP CONSTRAINT FK_users_client_id;";
        Assert.assertEquals(expected, result);
    }
}
