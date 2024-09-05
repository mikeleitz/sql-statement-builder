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
}
