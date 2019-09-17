package io.github.adorableskullmaster.nozomi.core.mongo.bridge.model;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance.BankModule;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance.NewApplicantModule;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance.NewWarModule;

public class AllianceProfile {
    private int aaId;
    private long serverId;
    private BankModule bankModule;
    private NewApplicantModule newApplicantModule;
    private NewWarModule newWarModule;

    public int getAaId() {
        return aaId;
    }

    public void setAaId(int aaId) {
        this.aaId = aaId;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public BankModule getBankModule() {
        return bankModule;
    }

    public void setBankModule(BankModule bankModule) {
        this.bankModule = bankModule;
    }

    public NewApplicantModule getNewApplicantModule() {
        return newApplicantModule;
    }

    public void setNewApplicantModule(NewApplicantModule newApplicantModule) {
        this.newApplicantModule = newApplicantModule;
    }

    public NewWarModule getNewWarModule() {
        return newWarModule;
    }

    public void setNewWarModule(NewWarModule newWarModule) {
        this.newWarModule = newWarModule;
    }
}
