package io.github.adorableskullmaster.nozomi.core.mongo.morphia;

import dev.morphia.annotations.*;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.server.Text;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.server.VacMode;
import org.bson.types.ObjectId;

import java.util.List;

@Entity(value = "servers")
public class Server {
    @Id
    private ObjectId id;
    @Indexed
    private long serverId;
    @Embedded
    private long memberId;
    @Reference
    private List<Alliance> alliances;
    @Embedded
    private Text text;
    @Embedded
    private VacMode vacMode;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public List<Alliance> getAlliances() {
        return alliances;
    }

    public void setAlliances(List<Alliance> alliances) {
        this.alliances = alliances;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public VacMode getVacMode() {
        return vacMode;
    }

    public void setVacMode(VacMode vacMode) {
        this.vacMode = vacMode;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }
}
