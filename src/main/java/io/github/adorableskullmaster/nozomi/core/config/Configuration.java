package io.github.adorableskullmaster.nozomi.core.config;

import java.util.Arrays;

public class Configuration {

    public static boolean isSentryEnabled() {
        return getSentryDSN() != null;
    }

    public static String getPrefix() {
        return System.getenv("PREFIX");
    }

    public static String getBotToken() {
        return System.getenv("TOKEN");
    }

    public static String getOwnerId() {
        return System.getenv("OWNER_ID");
    }

    public static String getMasterPWKey() {
        return System.getenv("PW_KEY");
    }

    public static String getSentryDSN() {
        return System.getenv("SENTRY");
    }

    public static int getNewWarFrequency() {
        return parseServicesFrequency()[0];
    }

    private static Integer[] parseServicesFrequency() {
        String services_frequency = System.getenv("SERVICES_FREQUENCY");
        String[] split = services_frequency.trim().split("-");
        return Arrays.stream(split).map(Integer::parseInt).toArray(Integer[]::new);
    }

}
