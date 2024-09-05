package com.mikeleitz.sqlstatmentbuilder;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
class SqlStringTest {

    @Test
    public void basicUsage() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .insert()
            .intoTable("users")
            .insert("name", "leitz")
            .build();

        Assertions.assertEquals("INSERT INTO USERS (NAME) VALUES ('leitz')", sqlStatement.getSqlString());

        Assertions.assertNotNull(sqlStatement.getColumnNames());
        Assertions.assertNotNull(sqlStatement.getColumnValues());
        Assertions.assertEquals(1, sqlStatement.getColumnNames().size());
        Assertions.assertEquals(1, sqlStatement.getColumnValues().size());
    }

    @Test
    public void preparedStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .insert()
            .preparedStatement()
            .intoTable("users")
            .insert("name", "leitz")
            .build();

        Assertions.assertEquals("INSERT INTO USERS (NAME) VALUES (?)", sqlStatement.getSqlString());
    }

    @Test
    public void select() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .select()
            .preparedStatement()
            .column("first_name")
            .column("last_name")
            .fromTable("users")
            .where("id", 1)
            .orderBy("last_name")
            .build();

        Assertions.assertEquals("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE ID = ? ORDER BY LAST_NAME", sqlStatement.getSqlString());

    }
}
