package com.mikeleitz.sqlstatmentbuilder;

import static org.junit.jupiter.api.Assertions.*;
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
            .insertValue("name", "leitz")
            .build();

        Assertions.assertEquals("INSERT INTO USERS (NAME) VALUES (?)", sqlStatement.getSqlString());

        Assertions.assertNotNull(sqlStatement.getColumnsAndValues());
        Assertions.assertEquals(1, sqlStatement.getColumnsAndValues().size());
    }
}
