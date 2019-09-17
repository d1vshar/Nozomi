package io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.server;

public class VacModeModule {

    private int[] allianceIds;
    private int scoreFilter;
    private long broadcastChannel;

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

    public long getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(long broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }
}
