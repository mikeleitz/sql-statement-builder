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
            .updateValue("first_name", "mike")
            .updateValue("last_name", "leitz")
            .table("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("UPDATE USERS SET FIRST_NAME = 'mike', LAST_NAME = 'leitz' WHERE ID = '1'", sqlStatement.getSqlString());
    }

    @Test
    public void updatePreparedStatement() {
        SqlString sqlStatement = new SqlString.SqlStringBuilder()
            .update()
            .preparedStatement()
            .updateValue("first_name", "mike")
            .updateValue("last_name", "leitz")
            .table("users")
            .where("id", 1)
            .build();

        Assertions.assertEquals("UPDATE USERS SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?", sqlStatement.getSqlString());
    }
}
