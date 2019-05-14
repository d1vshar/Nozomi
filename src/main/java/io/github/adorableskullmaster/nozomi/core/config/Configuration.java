package io.github.adorableskullmaster.nozomi.core.config;

import java.util.Arrays;

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

  public String getPrefix() { return System.getenv("PREFIX"); }

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

  public int getNewWarFrequency() {
    return parseServicesFrequency()[0];
  }

  public int getBankCheckFrequency() {
    return parseServicesFrequency()[1];
  }

  public int getNewApplicantFrequency() {
    return parseServicesFrequency()[2];
  }

  private Integer[] parseServicesFrequency() {
    String services_frequency = System.getenv("SERVICES_FREQUENCY");
    String[] split = services_frequency.trim().split("-");
    return Arrays.stream(split).map(Integer::parseInt).toArray(Integer[]::new);
  }

}
