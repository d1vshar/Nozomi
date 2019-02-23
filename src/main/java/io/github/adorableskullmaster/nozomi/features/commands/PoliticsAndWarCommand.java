package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;

public abstract class PoliticsAndWarCommand extends Command {

  protected PoliticsAndWarCommand() {
    this.guildOnly = true;
    this.category = new Command.Category("PW",event -> AuthUtility.checkCommand(name,event.getGuild().getIdLong()));
  }

}
