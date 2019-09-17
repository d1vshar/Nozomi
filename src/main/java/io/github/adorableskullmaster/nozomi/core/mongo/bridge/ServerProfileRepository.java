package io.github.adorableskullmaster.nozomi.core.mongo.bridge;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.ModelConverter;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.DiscordServer;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.Server;

import java.util.List;

public class ServerProfileRepository {
    public List<DiscordServer> findAllDiscordServers() {
        return ModelConverter.convertIntoDiscordServerList(Bot.DATA_STORE.createQuery(Server.class)
                .find()
                .toList());
    }

    public DiscordServer findAllDiscordServerById(long serverId) {
        Server server = Bot.DATA_STORE.createQuery(Server.class)
                .field("serverId")
                .equal(serverId)
                .first();
        if (server != null)
            return ModelConverter.convertIntoDiscordServer(server);
        return null;
    }
}
