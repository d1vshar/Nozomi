package io.github.adorableskullmaster.nozomi.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.core.database.ConfigurationDataSource;
import io.github.adorableskullmaster.nozomi.core.util.Emojis;
import net.dv8tion.jda.api.entities.ISnowflake;

import java.util.stream.Collectors;

public abstract class BotCommand extends Command {
    protected BotCommand() {
        this.guildOnly = true;
        this.category = new Command.Category(
                "Bot Command",
                Emojis.CANCEL.getAsMention() + " Join Arrgh, pleb.",
                event -> {
                    if (ConfigurationDataSource.isSetup()) {
                        Long memberRole = ConfigurationDataSource.getConfiguration().getMemberRole();
                        return event.getMember()
                                .getRoles()
                                .stream()
                                .map(ISnowflake::getIdLong).collect(Collectors.toList())
                                .contains(memberRole);
                    }
                    return false;
                }
        );
    }
}
