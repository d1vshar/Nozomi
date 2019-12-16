package io.github.adorableskullmaster.nozomi.core.database.models;

import java.util.Arrays;

public class Configuration {
    private Long mainChannel;
    private Long membersChannel;
    private Long govChannel;
    private Long warsChannel;
    private Long vmChannel;
    private Long beigeChannel;
    private Long memberRole;
    private Long govRole;
    private String[] apiKeys;
    private String joinText;
    private Integer[] aaTrack;

    public Long getMainChannel() {
        return mainChannel;
    }

    public void setMainChannel(Long mainChannel) {
        this.mainChannel = mainChannel;
    }

    public Long getMembersChannel() {
        return membersChannel;
    }

    public void setMembersChannel(Long membersChannel) {
        this.membersChannel = membersChannel;
    }

    public Long getGovChannel() {
        return govChannel;
    }

    public void setGovChannel(Long govChannel) {
        this.govChannel = govChannel;
    }

    public Long getWarsChannel() {
        return warsChannel;
    }

    public void setWarsChannel(Long warsChannel) {
        this.warsChannel = warsChannel;
    }

    public Long getVmChannel() {
        return vmChannel;
    }

    public void setVmChannel(Long vmChannel) {
        this.vmChannel = vmChannel;
    }

    public Long getBeigeChannel() {
        return beigeChannel;
    }

    public void setBeigeChannel(Long beigeChannel) {
        this.beigeChannel = beigeChannel;
    }

    public Long getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Long memberRole) {
        this.memberRole = memberRole;
    }

    public Long getGovRole() {
        return govRole;
    }

    public void setGovRole(Long govRole) {
        this.govRole = govRole;
    }

    public String[] getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(String[] apiKeys) {
        this.apiKeys = apiKeys;
    }

    public String getJoinText() {
        return joinText;
    }

    public void setJoinText(String joinText) {
        this.joinText = joinText;
    }

    public Integer[] getAaTrack() {
        return aaTrack;
    }

    public void setAaTrack(Integer[] aaTrack) {
        this.aaTrack = aaTrack;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "mainChannel=" + mainChannel +
                ", membersChannel=" + membersChannel +
                ", govChannel=" + govChannel +
                ", warsChannel=" + warsChannel +
                ", vmChannel=" + vmChannel +
                ", beigeChannel=" + beigeChannel +
                ", memberRole=" + memberRole +
                ", govRole=" + govRole +
                ", apiKeys=" + Arrays.toString(apiKeys) +
                ", joinText='" + joinText + '\'' +
                ", aaTrack=" + Arrays.toString(aaTrack) +
                '}';
    }
}
