package io.github.adorableskullmaster.nozomi.features.commands.fun;

import com.github.ricksbrown.cowsay.Cowsay;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.features.commands.FunCommand;
import net.dv8tion.jda.core.MessageBuilder;

import java.util.Arrays;

public class CowsayCommand extends FunCommand {

  public CowsayCommand() {
    this.name = "cowsay";
    this.help = "Make the cow say something";
    this.arguments = "++cowsay <Text>";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      MessageBuilder messageBuilder = new MessageBuilder();

      String[] split = split(commandEvent.getArgs());
      String result;

      if(split[0].isEmpty() && split[1].isEmpty())
        result = String.join(", ", Cowsay.say(new String[]{"-l"}).split("\n"));
      else if(split[1].isEmpty())
        result = Cowsay.say(new String[]{split[0]});
      else if(valid(split[0]))
        result = Cowsay.say(new String[]{"-f "+split[0],split[1]});
      else
        result = Cowsay.say(new String[]{split[0],split[1]});
      messageBuilder.appendCodeBlock(result,"");
      commandEvent.reply(messageBuilder.build());
    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e,commandEvent);
    }
  }

  private String[] split(String args) {
    String[] result = new String[2];
    String[] split = args.split(" ");
    if(args.isEmpty()) {
      result[0] = "";
      result[1] = "";
    }
    else if(split.length>1) {
      result[0] = split[0];
      result[1] = args.substring(args.trim().indexOf(" ")+1);
    }
    else {
      result[0] = args;
      result[1] = "";
    }
    return result;
  }

  private boolean valid(String arg) {
    return Arrays.stream(Cowsay.say(new String[]{"-l"}).split("\n")).map(String::trim).anyMatch(s -> s.equalsIgnoreCase(arg));
  }
}
