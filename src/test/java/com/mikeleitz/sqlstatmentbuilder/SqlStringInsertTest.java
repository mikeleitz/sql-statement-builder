package com.mikeleitz.sqlstatmentbuilder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlStringInsertTest {
    @Test
    public void insertStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .insert()
            .insertValue("first_name", "mike")
            .insertValue("last_name", "leitz")
            .intoTable("users")
            .build();

        Assertions.assertEquals("INSERT INTO USERS (FIRST_NAME, LAST_NAME) VALUES ('mike', 'leitz')", sqlStatement.getSqlString());
    }

    @Test
    public void insertPreparedStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .insert()
            .preparedStatement()
            .insertValue("first_name", "mike")
            .insertValue("last_name", "leitz")
            .intoTable("users")
            .build();

        Assertions.assertEquals("INSERT INTO USERS (FIRST_NAME, LAST_NAME) VALUES (?, ?)", sqlStatement.getSqlString());
    }
}
