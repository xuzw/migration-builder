package com.github.xuzw.migration_builder;

/**
 * @author 徐泽威 xuzewei_2012@126.com
 * @time 2017年6月16日 下午4:28:18
 */
public class AutoIncrement {
    private final String table;
    private final long begin;

    public AutoIncrement(String table, long begin) {
        this.table = table;
        this.begin = begin;
    }

    public String getTable() {
        return table;
    }

    public long getBegin() {
        return begin;
    }
}
