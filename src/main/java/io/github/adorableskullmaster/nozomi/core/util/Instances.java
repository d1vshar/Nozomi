package io.github.adorableskullmaster.nozomi.core.util;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.DB;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Instances {

  public static PoliticsAndWar getDefaultPW() {
    return new PoliticsAndWarBuilder()
        .setApiKey(Bot.configuration.getMasterPWKey())
        .setEnableCache(true, 20, 300000)
        .build();
  }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(Bot.configuration.getDbUrl());
  }

  public static DB getDBLayer() throws SQLException {
    return new DB();
  }

}
