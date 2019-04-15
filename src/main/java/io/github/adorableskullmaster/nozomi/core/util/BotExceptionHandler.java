package io.github.adorableskullmaster.nozomi.core.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.sentry.Sentry;
import io.sentry.event.EventBuilder;
import io.sentry.event.interfaces.ExceptionInterface;

public class BotExceptionHandler {

  private EventBuilder eventBuilder;

  public BotExceptionHandler() {
    eventBuilder = new EventBuilder();
    eventBuilder.withRelease(getClass().getPackage().getImplementationVersion());

    if (Bot.configuration.isSentryEnabled())
      Sentry.init(Bot.configuration.getSentryDSN());
    else
      Sentry.close();
  }

  public void captureException(Throwable t) {
    Bot.LOGGER.error("Error Encountered: ", t);
    eventBuilder.withMessage(t.getMessage())
        .withSentryInterface(new ExceptionInterface(t));
    if (Bot.configuration.isSentryEnabled())
      Sentry.capture(eventBuilder);
  }

  public void captureException(Throwable t, CommandEvent event) {
    event.reply(":x: An Error occurred. It has been reported.");
    captureException(t);
  }

}
