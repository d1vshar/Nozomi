package io.github.adorableskullmaster.nozomi.core.mongo;

import org.bson.types.ObjectId;

public class AllianceConfig {
    private ObjectId objectId;
    private Long serverId;
    private Long pwId;
    private Long memberRoleId;
    private Long[] warAAIds;
    private Long warChannelId;
    private Integer vmScoreLow;
    private Integer vmScoreHigh;
    private Integer vmScoreIds;
    private Long vmChannelId;

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long[] getWarAAIds() {
        return warAAIds;
    }

    public void setWarAAIds(Long[] warAAIds) {
        this.warAAIds = warAAIds;
    }

    public Integer getVmScoreLow() {
        return vmScoreLow;
    }

    public void setVmScoreLow(Integer vmScoreLow) {
        this.vmScoreLow = vmScoreLow;
    }

    public Integer getVmScoreHigh() {
        return vmScoreHigh;
    }

    public void setVmScoreHigh(Integer vmScoreHigh) {
        this.vmScoreHigh = vmScoreHigh;
    }

    public Integer getVmScoreIds() {
        return vmScoreIds;
    }

    public void setVmScoreIds(Integer vmScoreIds) {
        this.vmScoreIds = vmScoreIds;
    }

    public Long getWarChannelId() {
        return warChannelId;
    }

    public void setWarChannelId(Long warChannelId) {
        this.warChannelId = warChannelId;
    }

    public Long getVmChannelId() {
        return vmChannelId;
    }

    public void setVmChannelId(Long vmChannelId) {
        this.vmChannelId = vmChannelId;
    }

    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
    }

    public Long getMemberRoleId() {
        return memberRoleId;
    }

    public void setMemberRoleId(Long memberRoleId) {
        this.memberRoleId = memberRoleId;
    }

    public Long getPwId() {
        return pwId;
    }

    public void setPwId(Long pwId) {
        this.pwId = pwId;
    }
}
