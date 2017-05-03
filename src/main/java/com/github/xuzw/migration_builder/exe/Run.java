package com.github.xuzw.migration_builder.exe;

import java.sql.Connection;
import java.sql.SQLException;

import com.github.xuzw.migration_builder.C;
import com.github.xuzw.migration_builder.Migration;
import com.github.xuzw.migration_builder.Table;

class Run {
  public static void main(String[] args) throws SQLException {

    Connection c = getConnection();
    Migration m = new Migration();
    Table t = m.createTable("users");
    t.addBasics()
            .addColumn("first_name", C.STRING)
            .addColumn("last_name", C.STRING)
            .addColumn("email", C.STRING);

    //m.createIndex("users", new ArrayList("email"));

    m.run(c);
  }

  private static Connection getConnection() {
    return null;
  }
}