package io.github.adorableskullmaster.nozomi.features.commands.utility;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.UtilityCommand;
import net.dv8tion.jda.core.EmbedBuilder;

public class PingCommand extends UtilityCommand {

  public PingCommand() {
    this.name = "ping";
    this.help = "Get current in Ping of Bot in ms";
    this.arguments = "++ping";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      long ping = commandEvent.getJDA().getPing();
      EmbedBuilder embed = new EmbedBuilder();
      embed.appendDescription("Pong! It took me " + Long.toString(ping) + "ms to respond!")
          .setColor(Utility.getGuildSpecificRoleColor(commandEvent));
      commandEvent.reply(embed.build());
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }
}
