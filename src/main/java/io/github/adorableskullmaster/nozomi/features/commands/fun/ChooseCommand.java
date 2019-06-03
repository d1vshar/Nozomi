package io.github.adorableskullmaster.nozomi.features.commands.fun;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.features.commands.types.FunCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChooseCommand extends FunCommand {
  public ChooseCommand() {
    this.name = "choose";
    this.arguments = "++choose <arg1> <arg2> [arg3] [arg4]...";
    this.help = "Chooses one of the arguments provided.";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      String megaArgs = commandEvent.getArgs();
      String[] argsArray = megaArgs.split(" ");
      if (argsArray.length > 0) {
        for (int i = 0; i < argsArray.length; i++) {
          String arg = argsArray[i];
          argsArray[i] = arg.trim();
        }
        List<String> argsList = Arrays.asList(argsArray);
        Random r = new Random();
        int i = r.nextInt(argsList.size());

        commandEvent.reply(argsList.get(i));
      } else
        commandEvent.reply("LOL! I choose me. Dummy provide me options.");
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }
}
