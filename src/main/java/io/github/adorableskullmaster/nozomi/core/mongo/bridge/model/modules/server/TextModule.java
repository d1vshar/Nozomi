package io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.server;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.Module;

public class TextModule implements Module {

    private long broadcastChannel;
    private String joinImage;
    private String leaveImage;
    private String joinText;
    private String leaveText;

    public long getBroadcastChannel() {
        return broadcastChannel;
    }

    public void setBroadcastChannel(long broadcastChannel) {
        this.broadcastChannel = broadcastChannel;
    }

    public String getJoinImage() {
        return joinImage;
    }

    public void setJoinImage(String joinImage) {
        this.joinImage = joinImage;
    }

    public String getLeaveImage() {
        return leaveImage;
    }

    public void setLeaveImage(String leaveImage) {
        this.leaveImage = leaveImage;
    }

    public String getJoinText() {
        return joinText;
    }

    public void setJoinText(String joinText) {
        this.joinText = joinText;
    }

    public String getLeaveText() {
        return leaveText;
    }

    public void setLeaveText(String leaveText) {
        this.leaveText = leaveText;
    }

    @Override
    public boolean status() {
        return false;
    }
}
