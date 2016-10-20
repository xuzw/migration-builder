package com.beekeeperdata.flywaybuilder.serializers;

import com.beekeeperdata.flywaybuilder.C;

/**
 * Created by rathboma on 10/19/16.
 */
public class PostgresSerializer extends SQLSerializer {

    @Override
    protected String typeToString(C columnType) {
        switch(columnType) {
            case DATETIME:
                return "TIMESTAMP";
            default:
                return super.typeToString(columnType);
        }
    }
}
