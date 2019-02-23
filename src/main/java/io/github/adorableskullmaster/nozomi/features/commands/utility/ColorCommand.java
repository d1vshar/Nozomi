package io.github.adorableskullmaster.nozomi.features.commands.utility;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.UtilityCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class ColorCommand extends UtilityCommand {

  private JSONObject colors;

  public ColorCommand() {
    this.name = "color";
    this.arguments = "++color <color hex>";
    this.help = "Get Color Image";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("colors.json");
    StringWriter writer = new StringWriter();
    try {
      IOUtils.copy(inputStream, writer, "UTF-8");
    } catch (IOException e) {
      Bot.botExceptionHandler.captureException(e);
    }
    String colorString = writer.toString();
    this.colors = new JSONObject(colorString);
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      if (commandEvent.getArgs().trim().split(" ").length == 1) {
        String code = commandEvent.getArgs().trim();
        if (colors.has(code)) {
          code = colors.getString(code);
        }
        code = code.toUpperCase();
        if (code.length() == 7)
          code = code.substring(1);
        if (Utility.isColorHex(code)) {
          EmbedBuilder embedBuilder = new EmbedBuilder()
              .setTitle("Color")
              .setColor(Color.decode("#" + code))
              .appendDescription("#" + code)
              .setThumbnail("https://via.placeholder.com/40/" + code + "/" + code);
          commandEvent.reply(embedBuilder.build());
        } else
          CommandResponseHandler.error(commandEvent, "Invalid color name/hex.");
      } else {
        CommandResponseHandler.illegal(commandEvent, name);
      }
    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e, commandEvent);
    }
  }
}
