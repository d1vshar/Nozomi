package io.github.adorableskullmaster.nozomi.features.commands.types;

import com.jagrosh.jdautilities.command.Command;

abstract class BotCommand extends Command {
  BotCommand() {
    this.guildOnly = true;
    this.category = new Command.Category(
            "BotCommand"
    );
  }
}
