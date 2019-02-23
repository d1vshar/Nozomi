package io.github.adorableskullmaster.nozomi.features.commands.moderator;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.features.commands.ModeratorCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;

import java.util.List;

public class PurgeCommand extends ModeratorCommand {

  public PurgeCommand() {
    this.name = "purge";
    this.help = "Purges messages";
    this.arguments = "++purge [amount]";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      String args = commandEvent.getArgs();
      if (!args.trim().isEmpty()) {
        MessageHistory msgHistory = new MessageHistory(commandEvent.getChannel());
        int amount = Integer.parseInt(args);
        commandEvent.getMessage().delete().queue(
            (c) -> {
              if (amount > 100 || amount < 2)
                commandEvent.reply("Maximum 100 messages and minimum 2 can be deleted at once.");
              else {
                List<Message> messages = msgHistory.retrievePast(amount).complete();
                commandEvent.getChannel().purgeMessages(messages);
              }
            }
        );
      } else {
        CommandResponseHandler.illegal(commandEvent, name);
      }
    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e, commandEvent);
    }
  }
}