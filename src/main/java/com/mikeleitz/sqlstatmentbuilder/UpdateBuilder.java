package com.mikeleitz.sqlstatmentbuilder;


import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author leitz@mikeleitz.com
 */
public class UpdateBuilder {
    private boolean preparedStatement = false;
    private String tableName;

    private Map<String, Object> columnNamesAndValues = new TreeMap<>();
    private Map<String, Object> whereColumnPredicates = new TreeMap<>();

    public SqlString build() {
        if (tableName == null || tableName.trim().length() == 0) {
            throw new UnableToCreateSqlStatementException("tableName is required to create SQL statement");
        }

        if (columnNamesAndValues == null || columnNamesAndValues.isEmpty()) {
            throw new UnableToCreateSqlStatementException("add one or more columns and values to create SQL insert statement");
        }

        return new SqlString(createSqlString(), null);
    }

    public UpdateBuilder preparedStatement() {
        preparedStatement = true;
        return this;
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

    private String createSqlString() {
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
