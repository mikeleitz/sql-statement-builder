package com.mikeleitz.sqlstatmentbuilder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlStringUpdateTest {
    @Test
    public void updateStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .update()
            .update("first_name", "mike")
            .update("last_name", "leitz")
            .table("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("UPDATE USERS SET FIRST_NAME = 'mike', LAST_NAME = 'leitz' WHERE ID = '1'", sqlStatement.getSqlStatement());
        Assertions.assertEquals("UPDATE USERS SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?", sqlStatement.getSqlPreparedStatement());
    }

    @Test
    public void updateOneColumnStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .update()
            .update("first_name", "mike")
            .table("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("UPDATE USERS SET FIRST_NAME = 'mike' WHERE ID = '1'", sqlStatement.getSqlStatement());
        Assertions.assertEquals("UPDATE USERS SET FIRST_NAME = ? WHERE ID = ?", sqlStatement.getSqlPreparedStatement());
    }

    @Test
    public void noUpdateTableSpecifiedException() {
        try {
            SqlString sqlStatement = new SqlString.SqlStringBuilder()
                .update()
                .update("first_name", "mike")
                .build();
        } catch (UnableToCreateSqlStatementException e) {
            return;
        }

        Assertions.fail("Expecting UnableToCreateSqlStatementException exception.");
    }

    @Test
    public void noUpdateColumnSpecifiedException() {
        try {
            SqlString sqlStatement = new SqlString.SqlStringBuilder()
                .update()
                .table("users")
                .build();
        } catch (UnableToCreateSqlStatementException e) {
            return;
        }

        Assertions.fail("Expecting UnableToCreateSqlStatementException exception.");
    }
}
