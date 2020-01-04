package io.github.adorableskullmaster.nozomi.logging;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Emojis;

import java.util.Objects;

public class BotLogger {
    private long channelId;

    public void log(LogContext logContext) {
        LogType logType = logContext.getLogType();
        String message = logContext.getMessage();

        switch (logType) {
            case ADD:
                message = Emojis.ADD.getAsMention() + " " + message;
                break;
            case CHANGE:
                message = Emojis.CHANGE.getAsMention() + " " + message;
                break;
            case REMOVE:
                message = Emojis.CANCEL.getAsMention() + " " + message;
                break;
            case WARNING:
                message = Emojis.WARNING.getAsMention() + " " + message;
                break;
            default:
        }

        Objects.requireNonNull(Bot.jda.getTextChannelById(channelId))
                .sendMessage(message)
                .queue();

    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }
}
