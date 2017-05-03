package com.github.xuzw.migration_builder.serializers;

import junit.framework.TestCase;
import org.junit.Assert;

import com.github.xuzw.migration_builder.C;
import com.github.xuzw.migration_builder.Migration;
import com.github.xuzw.migration_builder.Serializer;
import com.github.xuzw.migration_builder.serializers.H2Serializer;

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
        Assert.assertEquals("CREATE TABLE test(id BIGINT NOT NULL AUTO_INCREMENT,created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,name VARCHAR(255),PRIMARY KEY(id));",
                result);
    }

    public void testBooleanColumn() throws Exception {
        Migration h2 = new Migration();
        h2.createTable("test")
                .addBasics()
                .addColumn("is_awesome", C.BOOLEAN, true, "FALSE");

        String result = this.serializer.serialize(h2);
        Assert.assertEquals("CREATE TABLE test(id BIGINT NOT NULL AUTO_INCREMENT,created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,is_awesome BOOLEAN NOT NULL DEFAULT FALSE,PRIMARY KEY(id));",
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
        Assert.assertEquals("CREATE TABLE test(id BIGINT NOT NULL AUTO_INCREMENT,PRIMARY KEY(id));", result);
    }

    public void testAddColumn() throws Exception {
        Migration h2 = new Migration();
        h2.addColumn("users", "first_name", C.STRING)
                .addColumn("users", "last_name", C.STRING);
        String result = this.serializer.serialize(h2);
        String e = "ALTER TABLE users ADD first_name VARCHAR(255);ALTER TABLE users ADD last_name VARCHAR(255);";
        Assert.assertEquals(e, result);
    }

    public void testAddBooleanColumn() throws Exception {
        Migration h2 = new Migration();
        h2.addColumn("users", "is_awesome", C.BOOLEAN);
        String result = this.serializer.serialize(h2);
        String e = "ALTER TABLE users ADD is_awesome BOOLEAN;";
        Assert.assertEquals(e, result);
    }

    public void testForeignKey() throws Exception {
        Migration h2 = new Migration();
        h2.createTable("posts")
                .addColumn("user_id", C.BIGINT)
                .addForeignKey("user_id", "users", "id");

        String result = serializer.serialize(h2);
        String expected = "CREATE TABLE posts(user_id BIGINT,CONSTRAINT FK_posts_user_id FOREIGN KEY(user_id) REFERENCES users(id) );";
        Assert.assertEquals(expected, result);
    }

    public void testDropTable() throws Exception {
        Migration h2 = new Migration().dropTable("foo");
        String expected = "DROP TABLE foo;";
        Assert.assertEquals(expected, serializer.serialize(h2));
    }





}