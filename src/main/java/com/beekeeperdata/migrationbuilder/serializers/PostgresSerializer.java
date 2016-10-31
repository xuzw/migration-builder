package com.beekeeperdata.migrationbuilder.serializers;

import com.beekeeperdata.migrationbuilder.C;

/**
 * Created by rathboma on 10/19/16.
 */
public class PostgresSerializer extends SQLSerializer {

    @Override
    protected String typeToString(C columnType) {
        switch(columnType) {
            case DATETIME:
                return "TIMESTAMP";
            case AUTOINC:
                return "SERIAL";
            default:
                return super.typeToString(columnType);
        }
    }
}
