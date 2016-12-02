package com.beekeeperdata.migrationbuilder.serializers;

import com.beekeeperdata.migrationbuilder.ForeignKey;

/**
 * Created by rathboma on 11/30/16.
 */
public class MySqlSerializer extends SQLSerializer {
    @Override
    protected String dropFK(ForeignKey key) {
        return String.format("ALTER TABLE %s DROP FOREIGN KEY %s;", key.getTable(), key.getConstraintName());
    }
}
