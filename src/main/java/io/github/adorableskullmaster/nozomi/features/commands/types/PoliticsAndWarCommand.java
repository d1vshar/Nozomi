package io.github.adorableskullmaster.nozomi.features.commands.types;

import com.jagrosh.jdautilities.command.Command;

public abstract class PoliticsAndWarCommand extends BotCommand {

  protected PoliticsAndWarCommand() {
    this.category = new Command.Category("PW");
  }

}
