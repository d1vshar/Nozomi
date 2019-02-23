package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;
import io.github.adorableskullmaster.nozomi.core.util.Utility;

public abstract class ModeratorCommand extends Command {
  protected ModeratorCommand() {
    this.guildOnly = true;
    this.userPermissions = Utility.getModerator();
    this.category = new Command.Category("Moderator",event -> AuthUtility.checkCommand(name,event.getGuild().getIdLong()));
  }
}
