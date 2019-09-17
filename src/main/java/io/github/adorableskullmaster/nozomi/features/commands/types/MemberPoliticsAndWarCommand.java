package io.github.adorableskullmaster.nozomi.features.commands.types;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.ServerProfileRepository;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.DiscordServer;
import net.dv8tion.jda.core.entities.Role;

import java.util.stream.Collectors;

public abstract class MemberPoliticsAndWarCommand extends PoliticsAndWarCommand {
    protected MemberPoliticsAndWarCommand() {
        this.category = new Category(
                "PW",
                "❌ You do not have the Member role",
                event -> {
                    DiscordServer discordServer = new ServerProfileRepository().findAllDiscordServerById(event.getGuild().getIdLong());

                    return discordServer != null &&
                            event.getMember()
                                    .getRoles()
                                    .stream()
                                    .map(Role::getIdLong)
                                    .collect(Collectors.toList())
                                    .contains(discordServer.getMemberId());
                }
        );
    }
}
