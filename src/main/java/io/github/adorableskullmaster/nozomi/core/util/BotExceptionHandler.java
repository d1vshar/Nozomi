package io.github.adorableskullmaster.nozomi.core.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.config.Configuration;
import io.sentry.Sentry;

public class BotExceptionHandler {

    public BotExceptionHandler() {
        if (Configuration.isSentryEnabled())
            Sentry.init(Configuration.getSentryDSN());
        else
            Sentry.close();
    }

    private void captureException(Throwable t) {
        Bot.getLOGGER().error("Error Encountered: ", t);
        if (Configuration.isSentryEnabled())
            Sentry.capture(t);
    }

    public void captureException(Throwable t, CommandEvent event) {
        event.reply(":x: An Error occurred. It has been reported.");
        captureException(t);
    }

}
