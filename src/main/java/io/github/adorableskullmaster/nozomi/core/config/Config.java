package io.github.adorableskullmaster.nozomi.core.config;

import java.util.List;

public class Config {

  private ConfigCredentials credentials;
  private List<ConfigGuild> guilds;

  public ConfigCredentials getCredentials() {
    return credentials;
  }

  public void setCredentials(ConfigCredentials credentials) {
    this.credentials = credentials;
  }

  public List<ConfigGuild> getGuilds() {
    return guilds;
  }

  public void setGuilds(List<ConfigGuild> guilds) {
    this.guilds = guilds;
  }

  public static class ConfigCredentials {

    private String prefix;
    private String testPrefix;
    private String botToken;
    private String testBotToken;
    private long ownerId;
    private String masterPWKey;
    private String sentryDSN;
    private String testDB;
    private String cheweyKey;
    private String owmKey;

    public String getPrefix() {
      return prefix;
    }

    public void setPrefix(String prefix) {
      this.prefix = prefix;
    }

    public String getTestPrefix() {
      return testPrefix;
    }

    public void setTestPrefix(String testPrefix) {
      this.testPrefix = testPrefix;
    }

    public String getBotToken() {
      return botToken;
    }

    public void setBotToken(String botToken) {
      this.botToken = botToken;
    }

    public String getTestBotToken() {
      return testBotToken;
    }

    public void setTestBotToken(String testBotToken) {
      this.testBotToken = testBotToken;
    }

    public long getOwnerId() {
      return ownerId;
    }

    public void setOwnerId(long ownerId) {
      this.ownerId = ownerId;
    }

    public String getSentryDSN() {
      return sentryDSN;
    }

    public void setSentryDSN(String sentryDSN) {
      this.sentryDSN = sentryDSN;
    }

    public String getTestDB() {
      return testDB;
    }

    public void setTestDB(String testDB) {
      this.testDB = testDB;
    }

    public String getCheweyKey() {
      return cheweyKey;
    }

    public void setCheweyKey(String cheweyKey) {
      this.cheweyKey = cheweyKey;
    }

    public String getOwmKey() {
      return owmKey;
    }

    public void setOwmKey(String owmKey) {
      this.owmKey = owmKey;
    }

    public String getMasterPWKey() {
      return masterPWKey;
    }

    public void setMasterPWKey(String masterPWKey) {
      this.masterPWKey = masterPWKey;
    }
  }

  public static class ConfigGuild {

    private int pwId;
    private String pwKey;
    private long discordId;
    private long mainChannel;
    private long govChannel;
    private long offensiveWarChannel;
    private long defensiveWarChannel;
    private boolean autoCounter;
    private long vmBeigeChannel;
    private long memberRole;
    private ConfigText texts;
    private List<String> excludedServices;
    private List<String> excludedCommands;

    public int getPwId() {
      return pwId;
    }

    public void setPwId(int pwId) {
      this.pwId = pwId;
    }

    public String getPwKey() {
      return pwKey;
    }

    public void setPwKey(String pwKey) {
      this.pwKey = pwKey;
    }

    public long getDiscordId() {
      return discordId;
    }

    public void setDiscordId(long discordId) {
      this.discordId = discordId;
    }

    public long getMainChannel() {
      return mainChannel;
    }

    public void setMainChannel(long mainChannel) {
      this.mainChannel = mainChannel;
    }

    public long getGovChannel() {
      return govChannel;
    }

    public void setGovChannel(long govChannel) {
      this.govChannel = govChannel;
    }

    public long getOffensiveWarChannel() {
      return offensiveWarChannel;
    }

    public void setOffensiveWarChannel(long offensiveWarChannel) {
      this.offensiveWarChannel = offensiveWarChannel;
    }

    public long getDefensiveWarChannel() {
      return defensiveWarChannel;
    }

    public void setDefensiveWarChannel(long defensiveWarChannel) {
      this.defensiveWarChannel = defensiveWarChannel;
    }

    public long getVmBeigeChannel() {
      return vmBeigeChannel;
    }

    public void setVmBeigeChannel(long vmBeigeChannel) {
      this.vmBeigeChannel = vmBeigeChannel;
    }

    public long getMemberRole() {
      return memberRole;
    }

    public void setMemberRole(long memberRole) {
      this.memberRole = memberRole;
    }

    public ConfigText getTexts() {
      return texts;
    }

    public void setTexts(ConfigText texts) {
      this.texts = texts;
    }

    public List<String> getExcludedServices() {
      return excludedServices;
    }

    public void setExcludedServices(List<String> excludedServices) {
      this.excludedServices = excludedServices;
    }

    public List<String> getExcludedCommands() {
      return excludedCommands;
    }

    public void setExcludedCommands(List<String> excludedCommands) {
      this.excludedCommands = excludedCommands;
    }

    public boolean isAutoCounter() {
      return autoCounter;
    }

    public void setAutoCounter(boolean autoCounter) {
      this.autoCounter = autoCounter;
    }

    public static class ConfigText {

      private String join;
      private String joinImg;

      public String getJoin() {
        return join;
      }

      public void setJoin(String join) {
        this.join = join;
      }

      public String getJoinImg() {
        return joinImg;
      }

      public void setJoinImg(String joinImg) {
        this.joinImg = joinImg;
      }
    }
  }
}
