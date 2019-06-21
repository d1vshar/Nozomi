package io.github.adorableskullmaster.nozomi.features.commands.fun;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.apis.animals.RandomAnimal;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.types.FunCommand;
import net.dv8tion.jda.core.EmbedBuilder;

import java.util.Random;

public class AnimalCommand extends FunCommand {

  public AnimalCommand() {
    this.name = "animal";
    this.help = "A random animal";
    this.arguments = "++animal";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      int i = new Random().nextInt(11);
      RandomAnimal animal = new RandomAnimal();
      String url = "";
      switch (i) {
        case 0:
          url = animal.getCheweyRabbit();
          break;
        case 1:
          url = animal.getRandomCat();
          break;
        case 2:
          url = animal.getCheweyCat();
          break;
        case 3:
          url = animal.getRandomDog();
          break;
        case 4:
          url = animal.getCheweyDog();
          break;
        case 5:
          url = animal.getCheweyBirb();
          break;
        case 6:
          url = animal.getRandomWolf();
          break;
        case 7:
          url = animal.getCheweyOtter();
          break;
        case 8:
          url = animal.getCheweySnake();
          break;
        case 9:
          url = animal.getShibe();
          break;
        case 10:
          url = animal.getCheweyDuck();
          break;
        case 11:
          url = animal.getCheweyTurtle();
      }
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setTitle("Random Animal")
          .setColor(Utility.getGuildSpecificRoleColor(commandEvent))
          .setImage(url);
      commandEvent.reply(embedBuilder.build());
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }
}
