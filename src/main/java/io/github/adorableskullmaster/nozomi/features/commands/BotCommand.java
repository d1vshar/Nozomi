package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Instances;

import java.sql.SQLException;

abstract class BotCommand extends Command {
  BotCommand() {
    this.guildOnly = true;
    this.category = new Command.Category(
        "BotCommand",
        ":x: Not configured to respond here",
        event -> {
          try {
            return Instances.getBotDatabaseLayer().getGuildSettings(event.getGuild().getIdLong()).getModuleSettings().isBotActivated();
          } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
            return false;
          }
        }
    );
  }
}
