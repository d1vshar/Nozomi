package io.github.adorableskullmaster.nozomi.core.database.models;

public class Player {
    private long discordId;
    private int nationId;
    private boolean member;
    private int allianceId;

    public Player(long discordId, int nationId, boolean member, int allianceId) {
        this.discordId = discordId;
        this.nationId = nationId;
        this.member = member;
        this.allianceId = allianceId;
    }

    public long getDiscordId() {
        return discordId;
    }

    public void setDiscordId(long discordId) {
        this.discordId = discordId;
    }

    public int getNationId() {
        return nationId;
    }

    public void setNationId(int nationId) {
        this.nationId = nationId;
    }

    public boolean isMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }

    public int getAllianceId() {
        return allianceId;
    }

    public void setAllianceId(int allianceId) {
        this.allianceId = allianceId;
    }
}
