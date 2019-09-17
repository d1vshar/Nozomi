package io.github.adorableskullmaster.nozomi.core.mongo.morphia;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.Bank;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.NewApplicant;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.NewWar;
import org.bson.types.ObjectId;

@Entity("allianceProfiles")
public class Alliance {

    @Id
    private ObjectId id;
    private int aaId;
    private long serverId;
    @Embedded
    private Bank bank;
    @Embedded
    private NewApplicant newApplicant;
    @Embedded
    private NewWar newWar;

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

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public NewApplicant getNewApplicant() {
        return newApplicant;
    }

    public void setNewApplicant(NewApplicant newApplicant) {
        this.newApplicant = newApplicant;
    }

    public NewWar getNewWar() {
        return newWar;
    }

    public void setNewWar(NewWar newWar) {
        this.newWar = newWar;
    }
}
