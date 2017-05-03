package com.github.xuzw.migration_builder.serializers;

import junit.framework.TestCase;
import org.junit.Assert;

import com.github.xuzw.migration_builder.C;
import com.github.xuzw.migration_builder.Migration;
import com.github.xuzw.migration_builder.serializers.MySqlSerializer;

/**
 * Created by rathboma on 11/30/16.
 */
public class MySqlSerializerTest extends TestCase {

    private MySqlSerializer serializer = new MySqlSerializer();


    public void testFKCreation() {
        Migration m = new Migration();
        m.createTable("users")
                .addColumn("client_id", C.BIGINT)
                .addForeignKey("client_id", "clients", "id");

        String result = serializer.serialize(m);
        String expected = "CREATE TABLE users(client_id BIGINT,CONSTRAINT FK_users_client_id FOREIGN KEY(client_id) REFERENCES clients(id) );";
        Assert.assertEquals(expected, result);;
    }

    public void testDropFK() {
        Migration m = new Migration();
        m.removeForeignKey("users", "client_id");
        String result = serializer.serialize(m);
        String expected = "ALTER TABLE users DROP FOREIGN KEY FK_users_client_id;";
        Assert.assertEquals(expected, result);
    }


}
