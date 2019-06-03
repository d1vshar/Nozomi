package io.github.adorableskullmaster.nozomi.core.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class QAProvider {
  private final CommandEvent commandEvent;
  private final String[] questions;
  private final String[] answers;
  private final BiConsumer<String[],CommandEvent> callbackFunction;
  private final EventWaiter eventWaiter;
  private int current = 0;

  public QAProvider(CommandEvent commandEvent, String[] questions, BiConsumer<String[],CommandEvent> callbackFunction, EventWaiter eventWaiter) {
    this.commandEvent = commandEvent;
    this.questions = questions;
    this.callbackFunction = callbackFunction;
    this.answers = new String[questions.length];
    this.eventWaiter = eventWaiter;
  }

  public void execute() {
    if(current >= questions.length) {
      callbackFunction.accept(answers,commandEvent);
      return;
    }
    commandEvent.getChannel()
        .sendMessage(questions[current])
        .queue(s -> eventWaiter.waitForEvent(
            MessageReceivedEvent.class,
            (event) -> event.getAuthor().getIdLong() == commandEvent.getAuthor().getIdLong()
                && event.getChannel().getIdLong() == commandEvent.getChannel().getIdLong(),
            a -> {
              System.out.println("Triggered");
              answers[current] = a.getMessage().getContentDisplay();
              current++;
              execute();
            },
            120,
            TimeUnit.SECONDS,
            () -> CommandResponseHandler.timeout(commandEvent)
        ));
  }
}