package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;

public abstract class UtilityCommand extends Command {
  protected UtilityCommand() {
    this.guildOnly = false;
    this.category = new Command.Category("Utility",event -> AuthUtility.checkCommand(name,event.getGuild().getIdLong()));

  }
}
