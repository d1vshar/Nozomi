package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;

public abstract class UtilityCommand extends BotCommand {
  protected UtilityCommand() {
    this.category = new Command.Category("Utility");

  }
}
