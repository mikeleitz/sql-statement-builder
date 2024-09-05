package com.mikeleitz.sqlstatmentbuilder;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author leitz@mikeleitz.com
 */
public class InsertBuilder {
    private boolean preparedStatement = false;
    private String tableName;
    private Map<String, Object> columnNamesAndValues = new TreeMap<>();

    public SqlString build() {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new UnableToCreateSqlStatementException("tableName is required to create SQL statement");
        }

        if (columnNamesAndValues == null || columnNamesAndValues.isEmpty()) {
            throw new UnableToCreateSqlStatementException("add one or more columns and values to create SQL insert statement");
        }

        List<String> columnNames = columnNamesAndValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey()).collect(Collectors.toList());
        List<Object> columnValues = columnNamesAndValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getValue()).collect(Collectors.toList());

        return new SqlString(createSqlString(), preparedStatement, columnNames, columnValues, null, null);
    }

    public InsertBuilder preparedStatement() {
        preparedStatement = true;
        return this;
    }

    public InsertBuilder intoTable(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public InsertBuilder insert(String columnName, Object value) {
        columnNamesAndValues.put(columnName, value);
        return this;
    }

    public InsertBuilder insertValues(Map<String, Object> columnNamesAndValues) {
        columnNamesAndValues.putAll(columnNamesAndValues);
        return this;
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

        if (preparedStatement) {
            IntStream.range(0, columnNamesAndValues.size()).forEach(i -> sqlStatement.append("?, "));
        } else {
            // use actual values.
            columnNamesAndValues.values().forEach(val -> sqlStatement.append("'").append(val).append("', "));
        }

        // Remove trailing comma
        sqlStatement.delete(sqlStatement.length() - 2, sqlStatement.length());

        sqlStatement.append(")");

        return sqlStatement.toString();
    }
}
