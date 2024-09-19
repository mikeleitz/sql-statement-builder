package com.mikeleitz.sqlstatmentbuilder;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author leitz@mikeleitz.com
 */
public class SelectBuilder {
    private String tableName;

    private Map<String, Boolean> columnsAndIfTheyAreCountColumns = new TreeMap<>();
    private Map<String, Object> whereColumnPredicates = new TreeMap<>();
    private List<String> orderBy = new ArrayList<>();

    public SqlString build() {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new UnableToCreateSqlStatementException("tableName is required to create SQL statement");
        }

        if (columnsAndIfTheyAreCountColumns.isEmpty()) {
            throw new UnableToCreateSqlStatementException("add one or more columns and values to create SQL insert statement");
        }

        if (whereColumnPredicates == null || whereColumnPredicates.isEmpty()) {
            return new SqlString(createSqlString(false), createSqlString(true), columnsAndIfTheyAreCountColumns.keySet().stream().collect(Collectors.toList()), null, null, null);
        } else {
            List<String> whereColumnNames = whereColumnPredicates.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey()).collect(Collectors.toList());
            List<Object> whereColumnValues = whereColumnPredicates.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getValue()).collect(Collectors.toList());

            return new SqlString(createSqlString(false), createSqlString(true), columnsAndIfTheyAreCountColumns.keySet().stream().collect(Collectors.toList()), null, whereColumnNames, whereColumnValues);
        }
    }

    public SelectBuilder fromTable(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public SelectBuilder count(String column) {
        // Can only have one column with count = true
        long totalColumnsCounting = columnsAndIfTheyAreCountColumns.values()
            .stream()
            .filter(b -> b)
            .count();

        if (totalColumnsCounting > 0) {
            throw new UnableToCreateSqlStatementException("Can only count one column per select statement");
        }

        columnsAndIfTheyAreCountColumns.put(column, true);
        return this;
    }

    public SelectBuilder column(String column) {
        columnsAndIfTheyAreCountColumns.put(column, false);
        return this;
    }

    public SelectBuilder where(String columnName, Object value) {
        whereColumnPredicates.put(columnName, value);
        return this;
    }

    public SelectBuilder where(Map<String, Object> columnNamesAndValues) {
        columnNamesAndValues.putAll(columnNamesAndValues);
        return this;
    }

    public SelectBuilder orderBy(String columnName) {
        orderBy.add(columnName);
        return this;
    }

    public SelectBuilder orderBy(List<String> columnNames) {
        orderBy.addAll(columnNames);
        return this;
    }

    private String createSqlString(boolean preparedStatement) {
        StringBuilder sqlStatement = new StringBuilder();

        sqlStatement.append("SELECT ");

        //List<String> columnNames = columnNamesAndValues.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(entry -> entry.getKey()).collect(Collectors.toList());

        columnsAndIfTheyAreCountColumns.entrySet()
            .stream()
            .forEach(entry ->
                {
                    if (entry.getValue()) {
                        sqlStatement.append("COUNT(").append(entry.getKey().toUpperCase()).append("), ");
                    } else {
                        sqlStatement.append(entry.getKey().toUpperCase()).append(", ");
                    }
                }
            );
        sqlStatement.delete(sqlStatement.length() - 2, sqlStatement.length());

        sqlStatement.append(" FROM ");
        sqlStatement.append(tableName.toUpperCase());

        if (whereColumnPredicates != null && !whereColumnPredicates.isEmpty()) {
            sqlStatement.append(" WHERE ");
            whereColumnPredicates
                .entrySet()
                .stream()
                .sorted()
                .forEach(entry -> {
                        if (preparedStatement) {
                            sqlStatement.append(entry.getKey().toUpperCase()).append(" = ? AND ");
                        } else {
                            sqlStatement.append(entry.getKey().toUpperCase()).append(" = '").append(entry.getValue()).append("' AND ");
                        }
                    }
                );

            sqlStatement.delete(sqlStatement.length() - 5, sqlStatement.length());
        }

        if (orderBy != null && !orderBy.isEmpty()) {
            sqlStatement.append(" ORDER BY ");
            orderBy.forEach(element -> sqlStatement.append(element.toUpperCase()).append(", "));
            sqlStatement.delete(sqlStatement.length() - 2, sqlStatement.length());
        }

        return sqlStatement.toString();
    }
}
