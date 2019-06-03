package io.github.adorableskullmaster.nozomi.core.util;

import com.jagrosh.jdautilities.command.CommandEvent;

public class CommandResponseHandler {

  public static void timeout(CommandEvent event) {
    event.reply(Emojis.CANCEL.getAsMention() + " You took too long to respond! Please try again!");
  }

  public static void illegal(CommandEvent event, String commandName) {
    event.replyFormatted(Emojis.CANCEL.getAsMention() + " Incorrect Command usage. Please see `%shelp %s`.", event.getClient().getPrefix(), commandName);
  }

  public static void error(CommandEvent event, String message) {
    event.reply(Emojis.CANCEL.getAsMention() + " " + message);
  }
}
