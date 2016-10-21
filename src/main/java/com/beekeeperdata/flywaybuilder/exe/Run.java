package com.beekeeperdata.flywaybuilder.exe;

import com.beekeeperdata.flywaybuilder.C;
import com.beekeeperdata.flywaybuilder.Migration;
import com.beekeeperdata.flywaybuilder.MigrationType;
import com.beekeeperdata.flywaybuilder.Table;


import java.sql.Connection;
import java.sql.SQLException;

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