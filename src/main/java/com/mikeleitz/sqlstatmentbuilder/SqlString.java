package com.mikeleitz.sqlstatmentbuilder;


import java.util.ArrayList;
import java.util.List;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlString {
    private final String sqlString;
    private final boolean isPreparedStatement;
    private final List<String> columnNames;
    private final List<Object> columnValues;
    private final List<String> wherePredicate;
    private final List<Object> whereValues;

    public SqlString(String sqlString, boolean isPreparedStatement) {
        this.sqlString = sqlString;
        this.columnNames = null;
        this.columnValues = null;
        this.wherePredicate = null;
        this.whereValues = null;

        this.isPreparedStatement = isPreparedStatement;
    }

    public SqlString(String sqlString, boolean isPreparedStatement, List<String> columnNames, List<Object> columnValues, List<String> wherePredicate, List<Object> whereValues) {
        this.sqlString = sqlString;
        this.columnNames = columnNames;
        this.columnValues = columnValues;
        this.wherePredicate = wherePredicate;
        this.whereValues = whereValues;

        this.isPreparedStatement = isPreparedStatement;
    }

    public List<Object> getAllBindParameters() {
        List<Object> allBindParameters = new ArrayList<>();

        if (columnValues != null && whereValues == null) {
            return null;
        }

        if (columnValues != null) {
            allBindParameters.addAll(columnValues);
        }

        if (whereValues != null) {
            allBindParameters.addAll(whereValues);
        }

        return allBindParameters;
    }

    public String getSqlString() {
        return sqlString;
    }

    public boolean isPreparedStatement() {
        return isPreparedStatement;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<Object> getColumnValues() {
        return columnValues;
    }

    public List<String> getWherePredicate() {
        return wherePredicate;
    }

    public List<Object> getWhereValues() {
        return whereValues;
    }

    @Override
    public String toString() {
        return "SqlString{" +
            "sqlString='" + sqlString + '\'' +
            ", isPreparedStatement=" + isPreparedStatement +
            ", columnNames=" + columnNames +
            ", columnValues=" + columnValues +
            ", wherePredicate=" + wherePredicate +
            ", whereValues=" + whereValues +
            '}';
    }

    public static class SqlStringBuilder {

        public InsertBuilder insert() {
            return new InsertBuilder();
        }

        public SelectBuilder select() {
            return new SelectBuilder();
        }

        public UpdateBuilder update() {
            return new UpdateBuilder();
        }

        public DeleteBuilder delete() {
            return new DeleteBuilder();
        }
    }
}
