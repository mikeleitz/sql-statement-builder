package com.mikeleitz.sqlstatmentbuilder;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author leitz@mikeleitz.com
 */
public class DeleteBuilder {
    private String tableName;

    private final Map<String, Object> whereColumnPredicates = new TreeMap<>();

    public SqlString build() {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new UnableToCreateSqlStatementException("tableName is required to create SQL statement");
        }

        if (whereColumnPredicates == null || whereColumnPredicates.isEmpty()) {
            return new SqlString(createSqlString(false), createSqlString(true));
        } else {
            List<String> whereColumnNames = whereColumnPredicates.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey()).collect(Collectors.toList());
            List<Object> whereColumnValues = whereColumnPredicates.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getValue()).collect(Collectors.toList());

            return new SqlString(createSqlString(false), createSqlString(true), null, null, whereColumnNames, whereColumnValues);
        }
    }

    public DeleteBuilder fromTable(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public DeleteBuilder where(String columnName, Object value) {
        whereColumnPredicates.put(columnName, value);
        return this;
    }

    public DeleteBuilder where(Map<String, Object> columnNamesAndValues) {
        columnNamesAndValues.putAll(columnNamesAndValues);
        return this;
    }

    private String createSqlString(boolean preparedStatement) {
        StringBuilder sqlStatement = new StringBuilder();

        sqlStatement.append("DELETE FROM ");
        sqlStatement.append(tableName.toUpperCase());

        if (whereColumnPredicates != null && !whereColumnPredicates.isEmpty()) {
            sqlStatement.append(" WHERE ");
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
