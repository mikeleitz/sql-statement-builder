package com.mikeleitz.sqlstatmentbuilder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlStringSelectTest {
    @Test
    public void basicSelectStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .select()
            .column("first_name")
            .column("last_name")
            .fromTable("users")
            .where("id", 1)
            .orderBy("last_name")
            .build();

        Assertions.assertEquals("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE ID = '1' ORDER BY LAST_NAME", sqlStatement.getSqlStatement());
        Assertions.assertEquals("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE ID = ? ORDER BY LAST_NAME", sqlStatement.getSqlPreparedStatement());
    }

    @Test
    public void twoOrderBySelectStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .select()
            .column("first_name")
            .column("last_name")
            .fromTable("users")
            .where("id", 1)
            .orderBy("last_name")
            .orderBy("first_name")
            .build();

        Assertions.assertEquals("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE ID = '1' ORDER BY LAST_NAME, FIRST_NAME", sqlStatement.getSqlStatement());
        Assertions.assertEquals("SELECT FIRST_NAME, LAST_NAME FROM USERS WHERE ID = ? ORDER BY LAST_NAME, FIRST_NAME", sqlStatement.getSqlPreparedStatement());
    }

    @Test
    public void oneColumnSelectStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .select()
            .column("first_name")
            .fromTable("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("SELECT FIRST_NAME FROM USERS WHERE ID = ?", sqlStatement.getSqlPreparedStatement());
        Assertions.assertEquals("SELECT FIRST_NAME FROM USERS WHERE ID = '1'", sqlStatement.getSqlStatement());
    }

    @Test
    public void noSelectColumnsSpecifiedException() {
        try {
            SqlString sqlStatement = new SqlString.SqlStringBuilder()
                .select()
                .fromTable("users")
                .build();
        } catch (UnableToCreateSqlStatementException e) {
            return;
        }

        Assertions.fail("Expecting UnableToCreateSqlStatementException exception.");
    }

    @Test
    public void noSelectTableSpecifiedException() {
        try {
            SqlString sqlStatement = new SqlString.SqlStringBuilder()
                .select()
                .column("first_name")
                .build();
        } catch (UnableToCreateSqlStatementException e) {
            return;
        }

        Assertions.fail("Expecting UnableToCreateSqlStatementException exception.");
    }
}
