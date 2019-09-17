package io.github.adorableskullmaster.nozomi.core.mongo.bridge;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.ModelConverter;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.AllianceProfile;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.Alliance;

import java.util.List;

public class AllianceProfileRepository {

    public List<AllianceProfile> findAllAllianceProfiles() {
        return ModelConverter.convertIntoAllianceProfileList(Bot.DATA_STORE.createQuery(Alliance.class)
                .find()
                .toList());
    }

    public List<AllianceProfile> findAllianceProfilesByServerId(long serverId) {
        List<Alliance> allianceList = Bot.DATA_STORE.createQuery(Alliance.class)
                .field("serverId")
                .equal(serverId)
                .find()
                .toList();
        return ModelConverter.convertIntoAllianceProfileList(allianceList);
    }

    public List<AllianceProfile> findAllianceProfilesByAllianceId(long allianceId) {
        List<Alliance> allianceList = Bot.DATA_STORE.createQuery(Alliance.class)
                .field("aaId")
                .equal(allianceId)
                .find()
                .toList();
        return ModelConverter.convertIntoAllianceProfileList(allianceList);
    }

    public void storeAllianceProfile(AllianceProfile allianceProfile) {
        Alliance alliance = ModelConverter.convertIntoAlliance(allianceProfile);
        Bot.DATA_STORE.save(alliance);
    }

}
