package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;

public abstract class OwnerCommand extends Command {

  protected OwnerCommand() {
    this.guildOnly = false;
    this.ownerCommand = true;
    this.category = new Command.Category("Owner",event -> AuthUtility.checkCommand(name,event.getGuild().getIdLong()));
  }

}
