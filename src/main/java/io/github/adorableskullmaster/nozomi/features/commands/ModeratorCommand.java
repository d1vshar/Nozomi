package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import io.github.adorableskullmaster.nozomi.core.util.Utility;

public abstract class ModeratorCommand extends BotCommand {
  protected ModeratorCommand() {
    this.userPermissions = Utility.getModerator();
    this.category = new Command.Category("Moderator");
  }
}
