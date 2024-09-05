package com.mikeleitz.sqlstatmentbuilder;


import java.util.Map;
import java.util.TreeMap;

/**
 * @author leitz@mikeleitz.com
 */
public class DeleteBuilder {
    private boolean preparedStatement = false;
    private String tableName;

    private Map<String, Object> whereColumnPredicates = new TreeMap<>();

    public SqlString build() {
        if (tableName == null || tableName.trim().length() == 0) {
            throw new UnableToCreateSqlStatementException("tableName is required to create SQL statement");
        }

        return new SqlString(createSqlString(), null);
    }

    public DeleteBuilder preparedStatement() {
        preparedStatement = true;
        return this;
    }
    public DeleteBuilder table(String tableName) {
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

    private String createSqlString() {
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
