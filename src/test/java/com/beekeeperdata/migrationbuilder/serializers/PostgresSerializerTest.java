package com.beekeeperdata.migrationbuilder.serializers;

import com.beekeeperdata.migrationbuilder.C;
import com.beekeeperdata.migrationbuilder.Migration;
import com.beekeeperdata.migrationbuilder.Serializer;
import junit.framework.TestCase;
import org.junit.Assert;

/**
 * Created by rathboma on 10/20/16.
 */
public class PostgresSerializerTest extends TestCase {

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
}
