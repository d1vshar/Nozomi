package io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance;

public class NewWar {
    private long offensiveChannel;
    private long defensiveChannel;
    private boolean counterSuggestion;

    public long getOffensiveChannel() {
        return offensiveChannel;
    }

    public void setOffensiveChannel(long offensiveChannel) {
        this.offensiveChannel = offensiveChannel;
    }

    public long getDefensiveChannel() {
        return defensiveChannel;
    }

    public void setDefensiveChannel(long defensiveChannel) {
        this.defensiveChannel = defensiveChannel;
    }

    public boolean isCounterSuggestion() {
        return counterSuggestion;
    }

    public void setCounterSuggestion(boolean counterSuggestion) {
        this.counterSuggestion = counterSuggestion;
    }
}
