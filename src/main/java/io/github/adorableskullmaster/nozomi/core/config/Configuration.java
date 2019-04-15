package io.github.adorableskullmaster.nozomi.core.config;

public class Configuration {

  public boolean isSentryEnabled() {
    return getSentryDSN() != null;
  }

  public boolean isCheweyEnabled() {
    return getCheweyKey() != null;
  }

  public boolean isWeatherEnabled() {
    return getOwmKey() != null;
  }

  public String getBotToken() {
    return System.getenv("TOKEN");
  }

  public String getOwnerId() {
    return System.getenv("OWNER_ID");
  }

  public String getMasterPWKey() {
    return System.getenv("PW_KEY");
  }

  public String getSentryDSN() {
    return System.getenv("SENTRY");
  }

  public String getDbUrl() {
    return System.getenv("JDBC_DATABASE_URL");
  }

  public String getCheweyKey() {
    return System.getenv("CHW_KEY");
  }

  public String getOwmKey() {
    return System.getenv("OWM_KEY");
  }

}
