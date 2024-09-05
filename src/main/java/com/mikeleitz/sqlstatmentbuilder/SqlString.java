package com.mikeleitz.sqlstatmentbuilder;


import java.util.ArrayList;
import java.util.List;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlString {
    private final String sqlStatement;
    private final String sqlPreparedStatement;
    private final List<String> columnNames;
    private final List<Object> columnValues;
    private final List<String> wherePredicate;
    private final List<Object> whereValues;

    public SqlString(String sqlStatement, String sqlPreparedStatement) {
        this.sqlStatement = sqlStatement;
        this.sqlPreparedStatement = sqlPreparedStatement;
        this.columnNames = null;
        this.columnValues = null;
        this.wherePredicate = null;
        this.whereValues = null;
    }

    public SqlString(String sqlStatement, String sqlPreparedStatement, List<String> columnNames, List<Object> columnValues, List<String> wherePredicate, List<Object> whereValues) {
        this.sqlStatement = sqlStatement;
        this.sqlPreparedStatement = sqlPreparedStatement;
        this.columnNames = columnNames;
        this.columnValues = columnValues;
        this.wherePredicate = wherePredicate;
        this.whereValues = whereValues;
    }

    /**
     * Collects and returns a combined list of all bind parameters from the object's column values and where values.
     * If column values and where values do not exist, null is returned.
     *
     * @return a list of bind parameters containing both column values and where values, or null if column values and where values do not exist.
     */
    public List<Object> getAllBindParameters() {
        List<Object> allBindParameters = new ArrayList<>();

        if (columnValues == null && whereValues == null) {
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

    /**
     * Retrieves the full SQL statement with all values specified as a string.
     *
     * @return the SQL statement.
     */
    public String getSqlStatement() {
        return sqlStatement;
    }

    /**
     * Retrieves the SQL prepared statement with parameter placeholders.
     *
     * @return the SQL prepared statement.
     */
    public String getSqlPreparedStatement() {
        return sqlPreparedStatement;
    }

    /**
     * Retrieves the list of column names associated with the SQL statement.
     *
     * @return a list of column names.
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Retrieves the list of column values associated with the SQL statement.
     * For statements like insert/update.
     * @return a list of column values.
     */
    public List<Object> getColumnValues() {
        return columnValues;
    }

    /**
     * Retrieves the list of predicates used in the WHERE clause of the SQL statement.
     *
     * @return a list of predicates for the WHERE clause.
     */
    public List<String> getWherePredicate() {
        return wherePredicate;
    }

    /**
     * Retrieves the list of values used in the WHERE clause of the SQL statement.
     *
     * @return a list of values for the WHERE clause.
     */
    public List<Object> getWhereValues() {
        return whereValues;
    }

    @Override
    public String toString() {
        return "SqlString{" +
            "sqlStatement='" + sqlStatement + '\'' +
            ", sqlPreparedStatement='" + sqlPreparedStatement + '\'' +
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
