package io.github.adorableskullmaster.nozomi.core.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.sentry.Sentry;

public class BotExceptionHandler {

    public BotExceptionHandler() {
        if (Bot.staticConfiguration.isSentryEnabled())
            Sentry.init(Bot.staticConfiguration.getSentryDSN());
        else
            Sentry.close();
    }

    public void captureException(Throwable t) {
        Bot.LOGGER.error("Error Encountered: ", t);
        if (Bot.staticConfiguration.isSentryEnabled())
            Sentry.capture(t);
    }

    public void captureException(Throwable t, CommandEvent event) {
        event.reply(":x: An Error occurred. It has been reported.");
        captureException(t);
    }

}
