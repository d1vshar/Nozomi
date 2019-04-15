package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;

public abstract class FunCommand extends BotCommand {

  protected FunCommand() {
    this.category = new Command.Category("fun");
  }

}
