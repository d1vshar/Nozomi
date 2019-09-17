package io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.server;

public class Text {
    private long channel;
    private String joinImage;
    private String leaveImage;
    private String joinText;
    private String leaveText;

    public long getChannel() {
        return channel;
    }

    public void setChannel(long channel) {
        this.channel = channel;
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
}
