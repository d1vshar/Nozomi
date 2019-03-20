package io.github.adorableskullmaster.nozomi.features.commands.pw.member;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.features.commands.MemberPoliticsAndWarCommand;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class FindTargetCommand extends MemberPoliticsAndWarCommand {

  public FindTargetCommand() {
    this.name = "findtarget";
    this.aliases = new String[]{"find"};
    this.help = "Sends you a list of of powered nations in your range";
    this.arguments = "++findtarget <score>";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    commandEvent.async(() -> {
      commandEvent.reply("Eh give me 10min lel.");
      List<Integer> result = find(Double.parseDouble(commandEvent.getArgs()));
      List<String> stringResult = result
          .stream()
          .map(integer -> "https://politicsandwar.com/nation/id=".concat(Integer.toString(integer)))
          .collect(Collectors.toList());
      commandEvent.reply(String.join("\n", stringResult));
    });
  }

  private List<Integer> find(double score) {
    int high = (int)(score * 1.75);
    int low = (int)(score * 0.75);
    Bot.LOGGER.info("score:" + score + " | high(" + high + ")low(" + low + ")");
    List<Integer> result = new ArrayList<>();

    List<SNationContainer> nations = new PoliticsAndWarBuilder()
        .setApiKey(Bot.config.getCredentials().getMasterPWKey())
        .build()
        .getNationsByScore(false, high, low)
        .getNationsContainer();
    Bot.LOGGER.info(nations.size() + " nations in range.");

    int c = 0;
    int cities = 0;
    for (SNationContainer snation : nations) {
      if (snation.getMinutessinceactive() > 3 * 24 * 60 && snation.getVacmode().equalsIgnoreCase("0")
          && snation.getDefensivewars() < 3) {
        cities += snation.getCities();
        c++;
      }
    }
    Bot.LOGGER.info(c + " nations with " + cities + " cities in range meeting conditions are inactive.");
    /*
    int count=1;
    for(SNation snation : nations) {
      if (snation.getMinutessinceactive()>3*24*60) {
        Bot.LOGGER.info("Nation #"+count);
        Nation nation = politicsAndWar.getNation(snation.getNationid());
        List<Integer> cityIds = nation.getCityids()
            .stream()
            .map(Integer::parseInt)
            .collect(Collectors.toList());

        boolean powered = true;
        for(int cityId : cityIds) {
          City city = politicsAndWar.getCity(cityId);
          if (!city.getPowered().equalsIgnoreCase("yes")) {
            powered = false;
            break;
          }
        }
        if(powered)
          result.add(snation.getNationid());
        count++;
      }
    }*/
    return result;
  }
}