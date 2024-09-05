package com.mikeleitz.sqlstatmentbuilder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlStringDeleteTest {
    @Test
    public void basicDeleteStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .delete()
            .fromTable("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("DELETE FROM USERS WHERE ID = '1'", sqlStatement.getSqlStatement());
        Assertions.assertEquals("DELETE FROM USERS WHERE ID = ?", sqlStatement.getSqlPreparedStatement());
    }

    @Test
    public void noWhereDeleteStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .delete()
            .fromTable("users")
            .build();

        Assertions.assertEquals("DELETE FROM USERS", sqlStatement.getSqlStatement());
        Assertions.assertEquals("DELETE FROM USERS", sqlStatement.getSqlPreparedStatement());
    }

    @Test
    public void multipleWhereDeleteStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .delete()
            .fromTable("users")
            .where("id", 1)
            .where("type", "basic")
            .build();

        Assertions.assertEquals("DELETE FROM USERS WHERE ID = '1' AND TYPE = 'basic'", sqlStatement.getSqlStatement());
        Assertions.assertEquals("DELETE FROM USERS WHERE ID = ? AND TYPE = ?", sqlStatement.getSqlPreparedStatement());
    }

    @Test
    public void noTableSpecifiedException() {
        try {
            SqlString sqlStatement = new SqlString.SqlStringBuilder()
                .delete()
                .where("id", 1)
                .where("type", "basic")
                .build();
        } catch (UnableToCreateSqlStatementException e) {
            return;
        }

        Assertions.fail("Expecting UnableToCreateSqlStatementException exception.");
    }
}
