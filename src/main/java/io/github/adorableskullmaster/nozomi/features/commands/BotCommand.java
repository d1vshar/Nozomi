package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;

abstract class BotCommand extends Command {
    BotCommand() {
        this.guildOnly = true;
        this.category = new Command.Category(
                "BotCommand",
                ":x: Not configured to respond here",
                event -> System.getenv("SERVER_ID").equals(event.getGuild().getId())
        );
    }
}
