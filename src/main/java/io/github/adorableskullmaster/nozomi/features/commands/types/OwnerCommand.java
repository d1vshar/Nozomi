package io.github.adorableskullmaster.nozomi.features.commands.types;

import com.jagrosh.jdautilities.command.Command;

public abstract class OwnerCommand extends Command {

  protected OwnerCommand() {
    this.guildOnly = false;
    this.ownerCommand = true;
    this.category = new Command.Category("Owner");
  }

}
