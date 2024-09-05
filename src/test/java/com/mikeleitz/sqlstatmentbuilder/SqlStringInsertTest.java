package com.mikeleitz.sqlstatmentbuilder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlStringInsertTest {
    @Test
    public void basicInsertStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .insert()
            .insert("first_name", "mike")
            .insert("last_name", "leitz")
            .intoTable("users")
            .build();

        Assertions.assertEquals("INSERT INTO USERS (FIRST_NAME, LAST_NAME) VALUES ('mike', 'leitz')", sqlStatement.getSqlStatement());
        Assertions.assertEquals("INSERT INTO USERS (FIRST_NAME, LAST_NAME) VALUES (?, ?)", sqlStatement.getSqlPreparedStatement());
    }
}
