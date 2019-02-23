package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;

public abstract class FunCommand extends Command {

  protected FunCommand() {
    this.guildOnly = false;
    this.category = new Command.Category("fun",event -> AuthUtility.checkCommand(name,event.getGuild().getIdLong()));
  }

}
