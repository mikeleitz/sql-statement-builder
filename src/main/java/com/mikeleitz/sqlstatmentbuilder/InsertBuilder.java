package com.mikeleitz.sqlstatmentbuilder;


import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.IntStream;

/**
 * @author leitz@mikeleitz.com
 */
public class InsertBuilder {
    private SqlString.SqlStringBuilder sqlStringBuilder;

    private boolean preparedStatement = false;
    private String tableName;
    private Map<String, Object> columnNamesAndValues = new TreeMap<>();

    public InsertBuilder(SqlString.SqlStringBuilder sqlStringBuilder) {
        this.sqlStringBuilder = sqlStringBuilder;
    }

    public InsertBuilder preparedStatement() {
        preparedStatement = true;
        return this;
    }

    public InsertBuilder intoTable(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public InsertBuilder insertValue(String columnName, Object value) {
        columnNamesAndValues.put(columnName, value);
        return this;
    }

    public InsertBuilder insertValues(Map<String, Object> columnNamesAndValues) {
        columnNamesAndValues.putAll(columnNamesAndValues);
        return this;
    }

    public SqlString build() {
        if (tableName == null || tableName.trim().length() == 0) {
            throw new UnableToCreateSqlStatementException("tableName is required to create SQL statement");
        }

        if (columnNamesAndValues == null || columnNamesAndValues.isEmpty()) {
            throw new UnableToCreateSqlStatementException("add one or more columns and values to create SQL insert statement");
        }

        return new SqlString(createSqlString(), columnNamesAndValues);
    }

    private String createSqlString() {
        // Create adapters to support other databases, where appropriate.
        StringBuilder sqlStatement = new StringBuilder();

        sqlStatement.append("INSERT INTO ");
        sqlStatement.append(tableName.toUpperCase());
        sqlStatement.append(" (");

        Optional.ofNullable(columnNamesAndValues.keySet())
            .stream()
            .flatMap(Collection::stream)
            .sorted()
            .forEach(col -> sqlStatement.append(col.toUpperCase()).append(", "));

        // Remove trailing comma
        sqlStatement.delete(sqlStatement.length() - 2, sqlStatement.length());

        sqlStatement.append(") VALUES (");

        IntStream.range(0, columnNamesAndValues.size()).forEach(i -> sqlStatement.append("?, "));

        // Remove trailing comma
        sqlStatement.delete(sqlStatement.length() - 2, sqlStatement.length());

        sqlStatement.append(")");

        return sqlStatement.toString();
    }
}
