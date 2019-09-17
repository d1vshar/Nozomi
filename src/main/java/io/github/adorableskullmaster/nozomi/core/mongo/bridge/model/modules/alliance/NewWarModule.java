package io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.Module;

public class NewWarModule implements Module {

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

    @Override
    public boolean status() {
        return false;
    }
}
