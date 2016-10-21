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
        Assert.assertEquals("CREATE TABLE test(id BIGINT AUTO_INCREMENT,created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,name VARCHAR(255), PRIMARY KEY(id));",
                result);
    }





}