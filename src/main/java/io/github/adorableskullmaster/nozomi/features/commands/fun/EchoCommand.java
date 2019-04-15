package io.github.adorableskullmaster.nozomi.features.commands.fun;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.features.commands.FunCommand;

public class EchoCommand extends FunCommand {

  public EchoCommand() {
    this.name = "echo";
    this.help = "Make the bot say something";
    this.arguments = "++echo <#channel> | [message]";
  }

  protected void execute(CommandEvent commandEvent) {
    try {
      String args = commandEvent.getArgs();
      String[] parts = args.split("[|]");
      if (parts.length == 2 && commandEvent.getMessage().getMentionedChannels().size() == 1)
        commandEvent.getMessage().getMentionedChannels().get(0).sendMessage(parts[1]).queue();
      else
        commandEvent.reply(args);
      commandEvent.getMessage().delete().queue();
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }

}
