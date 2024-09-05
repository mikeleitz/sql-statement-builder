package com.mikeleitz.sqlstatmentbuilder;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author leitz@mikeleitz.com
 */
public class UpdateBuilder {
    private String tableName;

    private Map<String, Object> columnNamesAndValues = new TreeMap<>();
    private Map<String, Object> whereColumnPredicates = new TreeMap<>();

    public SqlString build() {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new UnableToCreateSqlStatementException("tableName is required to create SQL statement");
        }

        if (columnNamesAndValues == null || columnNamesAndValues.isEmpty()) {
            throw new UnableToCreateSqlStatementException("add one or more columns and values to create SQL insert statement");
        }

        List<String> columnNames = columnNamesAndValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey()).collect(Collectors.toList());
        List<Object> columnValues = columnNamesAndValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getValue()).collect(Collectors.toList());

        if (whereColumnPredicates == null || whereColumnPredicates.isEmpty()) {
            return new SqlString(createSqlString(false), createSqlString(true), columnNames, columnValues, null, null);
        } else {
            List<String> whereColumnNames = whereColumnPredicates.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey()).collect(Collectors.toList());
            List<Object> whereColumnValues = whereColumnPredicates.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getValue()).collect(Collectors.toList());

            return new SqlString(createSqlString(false), createSqlString(true), columnNames, columnValues, whereColumnNames, whereColumnValues);
        }
    }

    public UpdateBuilder update(String columnName, Object value) {
        columnNamesAndValues.put(columnName, value);
        return this;
    }

    public UpdateBuilder table(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public UpdateBuilder update(Map<String, Object> columnNamesAndValues) {
        columnNamesAndValues.putAll(columnNamesAndValues);
        return this;
    }

    public UpdateBuilder where(String columnName, Object value) {
        whereColumnPredicates.put(columnName, value);
        return this;
    }

    public UpdateBuilder where(Map<String, Object> columnNamesAndValues) {
        columnNamesAndValues.putAll(columnNamesAndValues);
        return this;
    }

    private String createSqlString(boolean preparedStatement) {
        StringBuilder sqlStatement = new StringBuilder();

        sqlStatement.append("UPDATE ");
        sqlStatement.append(tableName.toUpperCase()).append(" SET ");

        Optional.ofNullable(columnNamesAndValues.entrySet())
            .stream()
            .flatMap(Set::stream)
            .forEach(entrySet -> {
                    if (preparedStatement) {
                        sqlStatement.append(entrySet.getKey().toUpperCase()).append(" = ?, ");
                    } else {
                        sqlStatement.append(entrySet.getKey().toUpperCase()).append(" = '").append(entrySet.getValue()).append("', ");
                    }
                }
            );

        sqlStatement.deleteCharAt(sqlStatement.length() - 2);

        if (whereColumnPredicates != null && !whereColumnPredicates.isEmpty()) {
            sqlStatement.append("WHERE ");
            whereColumnPredicates.forEach((key, value) -> {
                    if (preparedStatement) {
                        sqlStatement.append(key.toUpperCase()).append(" = ? AND ");
                    } else {
                        sqlStatement.append(key.toUpperCase()).append(" = '").append(value).append("' AND ");
                    }
                }
            );
            sqlStatement.delete(sqlStatement.length() - 5, sqlStatement.length());
        }

        return sqlStatement.toString();
    }

}
