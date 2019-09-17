package io.github.adorableskullmaster.nozomi.core.mongo.bridge.model;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.server.TextModule;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.server.VacModeModule;

import java.util.List;

public class DiscordServer {
    private long serverId;
    private List<AllianceProfile> allianceProfiles;
    private Long memberId;
    private TextModule textModule;
    private VacModeModule vacModeModule;

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public List<AllianceProfile> getAllianceProfiles() {
        return allianceProfiles;
    }

    public void setAllianceProfiles(List<AllianceProfile> allianceProfiles) {
        this.allianceProfiles = allianceProfiles;
    }

    public TextModule getTextModule() {
        return textModule;
    }

    public void setTextModule(TextModule textModule) {
        this.textModule = textModule;
    }

    public VacModeModule getVacModeModule() {
        return vacModeModule;
    }

    public void setVacModeModule(VacModeModule vacModeModule) {
        this.vacModeModule = vacModeModule;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
