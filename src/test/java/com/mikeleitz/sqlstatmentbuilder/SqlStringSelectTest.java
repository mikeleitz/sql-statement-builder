package com.mikeleitz.sqlstatmentbuilder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlStringSelectTest {
    @Test
    public void selectStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .select()
            .selectColumn("first_name")
            .selectColumn("last_name")
            .fromTable("users")
            .where("id", 1)
            .orderBy("last_name")
            .build();

        Assertions.assertEquals("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE ID = '1' ORDER BY LAST_NAME", sqlStatement.getSqlString());
    }

    @Test
    public void selectPreparedStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .select()
            .preparedStatement()
            .selectColumn("first_name")
            .selectColumn("last_name")
            .fromTable("users")
            .where("id", 1)
            .orderBy("last_name")
            .build();

        Assertions.assertEquals("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE ID = ? ORDER BY LAST_NAME", sqlStatement.getSqlString());
    }
}
