package io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.Module;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.Bank;

public class BankModule implements Module {

    private String govAPIKey;
    private long broadcastChannel;
    private Bank.Resources resourceLimits;

    public String getGovAPIKey() {
        return govAPIKey;
    }

    public void setGovAPIKey(String govAPIKey) {
        this.govAPIKey = govAPIKey;
    }

    public long getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(long broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    public Bank.Resources getResourceLimits() {
        return resourceLimits;
    }

    public void setResourceLimits(Bank.Resources resourceLimits) {
        this.resourceLimits = resourceLimits;
    }

    @Override
    public boolean status() {
        return false;
    }
}
