package io.github.adorableskullmaster.nozomi.core.config;

import java.util.Arrays;

public class StaticConfiguration {

    public boolean isSentryEnabled() {
        return getSentryDSN() != null;
    }

    public String getPrefix() {
        return System.getenv("PREFIX");
    }

    public String getBotToken() {
        return System.getenv("TOKEN");
    }

    public int getPWId() {
        return Integer.parseInt(System.getenv("PW_ID"));
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
