package io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.Module;

public class NewApplicantModule implements Module {

    private long broadcastChannel;

    public long getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(long broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    @Override
    public boolean status() {
        return false;
    }
}
