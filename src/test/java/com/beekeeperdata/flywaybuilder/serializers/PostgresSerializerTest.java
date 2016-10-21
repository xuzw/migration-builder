package com.beekeeperdata.flywaybuilder.serializers;

import com.beekeeperdata.flywaybuilder.C;
import com.beekeeperdata.flywaybuilder.Migration;
import com.beekeeperdata.flywaybuilder.MigrationType;
import com.beekeeperdata.flywaybuilder.Serializer;
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
}
