package io.github.adorableskullmaster.nozomi.features.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.features.commands.types.OwnerCommand;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;

public class ShutdownCommand extends OwnerCommand {

  private EventWaiter waiter;

  public ShutdownCommand(EventWaiter waiter) {
    this.waiter = waiter;
    this.name = "shutdown";
    this.aliases = new String[]{"kill"};
    this.help = "Shutdowns the bot.";
    this.arguments = "++shutdown";
  }

  private static void safExit(JDA jda) {
    jda.shutdownNow();
    System.exit(0);
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      commandEvent.getChannel().sendMessage("Are you sure you want me to shutdown? Y/N").queue(
          (c) -> waiter.waitForEvent(
              MessageReceivedEvent.class,
              (event) -> event.getAuthor().equals(commandEvent.getAuthor()) && event.getChannel().equals(commandEvent.getChannel()),
              (event) -> {
                if (event.getMessage().getContentDisplay().equalsIgnoreCase("Y")) {
                  event.getChannel().sendMessage("Bye, Bye").complete();
                  safExit(commandEvent.getJDA());
                }
              },
              30,
              TimeUnit.SECONDS,
              () -> CommandResponseHandler.timeout(commandEvent)
          )
      );
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }
}
