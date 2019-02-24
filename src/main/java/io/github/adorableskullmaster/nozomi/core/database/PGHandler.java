package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PGHandler {

  private Connection conn;

  public PGHandler(String url) {
    try {
      conn = DriverManager.getConnection(url);
      conn.setAutoCommit(true);
      setup();
    } catch (SQLException e) {
      Bot.botExceptionHandler.captureException(e);
    }
  }

  public Connection getConn() {
    return conn;
  }

  private void setup() {
    setupWars();
    setupApplicants();
  }

  private void setupWars() {
    try {
      String SQL = "CREATE TABLE IF NOT EXISTS wars (warID INTEGER);";
      Statement statement = conn.createStatement();
      statement.execute(SQL);
      statement.close();
    } catch (SQLException e) {
      Bot.botExceptionHandler.captureException(e);
    }
  }

  private void setupApplicants() {
    try {
      String SQL = "CREATE TABLE IF NOT EXISTS applicants (aid INTEGER, list INTEGER[])";
      Statement statement = conn.createStatement();
      statement.execute(SQL);
      statement.close();
    } catch (SQLException e) {
      Bot.botExceptionHandler.captureException(e);
    }
  }
}
