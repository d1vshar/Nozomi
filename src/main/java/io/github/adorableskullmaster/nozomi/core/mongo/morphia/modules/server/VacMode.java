package io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.server;

public class VacMode {
    private int[] allianceIds;
    private int scoreFilter;
    private long channel;

    public int[] getAllianceIds() {
        return allianceIds;
    }

    public void setAllianceIds(int[] allianceIds) {
        this.allianceIds = allianceIds;
    }

    public int getScoreFilter() {
        return scoreFilter;
    }

    public void setScoreFilter(int scoreFilter) {
        this.scoreFilter = scoreFilter;
    }

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
    }
}
