package com.mikeleitz.sqlstatmentbuilder;


/**
 *
 * @author leitz@mikeleitz.com
 */
public class UnableToCreateSqlStatementException extends RuntimeException {
  public UnableToCreateSqlStatementException(String message) {
    super(message);
  }
}
