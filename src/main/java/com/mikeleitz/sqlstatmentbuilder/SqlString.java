package com.mikeleitz.sqlstatmentbuilder;


import java.util.Map;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlString {
    private final String sqlString;
    private final Map<String, Object> columnsAndValues;

    public SqlString(String sqlString, Map<String, Object> columnsAndValues) {
        this.sqlString = sqlString;
        this.columnsAndValues = columnsAndValues;
    }

    public static class SqlStringBuilder {

        public InsertBuilder insert() {
            return new InsertBuilder(this);
        }

        public SelectBuilder select() {
            return new SelectBuilder();
        }

        public UpdateBuilder update() {
            return new UpdateBuilder();
        }
    }

    public String getSqlString() {
        return sqlString;
    }

    public Map<String, Object> getColumnsAndValues() {
        return columnsAndValues;
    }

    @Override
    public String toString() {
        return "SqlString{" +
            "sqlString='" + sqlString + '\'' +
            ", columnsAndValues=" + columnsAndValues +
            '}';
    }
}
