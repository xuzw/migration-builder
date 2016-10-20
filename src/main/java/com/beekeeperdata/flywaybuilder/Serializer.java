package com.beekeeperdata.flywaybuilder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by rathboma on 10/19/16.
 */
public abstract class Serializer {

    protected abstract String createTable(Table t);
    protected abstract String column(Column c);
    protected abstract String typeToString(C columnType);
    protected abstract String defaultValue(Column c);

    public abstract String serialize(Migration migration);
}


