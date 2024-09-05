package com.mikeleitz.sqlstatmentbuilder;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leitz@mikeleitz.com
 */
public class SqlStringDeleteTest {
    @Test
    public void deleteStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .delete()
            .table("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("DELETE FROM USERS WHERE ID = '1'", sqlStatement.getSqlString());
    }

    @Test
    public void deletePreparedStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .delete()
            .preparedStatement()
            .table("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("DELETE FROM USERS WHERE ID = ?", sqlStatement.getSqlString());
    }
}
