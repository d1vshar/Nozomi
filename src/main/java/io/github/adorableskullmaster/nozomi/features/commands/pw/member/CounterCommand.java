package io.github.adorableskullmaster.nozomi.features.commands.pw.member;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.config.Config;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.MemberPoliticsAndWarCommand;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.NationMilitary;
import io.github.adorableskullmaster.pw4j.domains.subdomains.NationMilitaryContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.time.Instant;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CounterCommand extends MemberPoliticsAndWarCommand {

  public CounterCommand() {
    this.name = "counter";
    this.aliases = new String[]{"backup"};
    this.help = "Gives closest counter for the target nation";
    this.arguments = "++counter <nationlink/nationid>";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      commandEvent.async(() -> {
        Config.ConfigGuild guild = AuthUtility.getGuildConfig(commandEvent.getGuild().getIdLong());

        commandEvent.getChannel().sendTyping().queue();
        if (!commandEvent.getArgs().trim().isEmpty() && commandEvent.getArgs().trim().split(" ").length != 1) {
          CommandResponseHandler.illegal(commandEvent, name);
        } else {
          String args = commandEvent.getArgs();
          args = args.trim();
          int id;
          if (Utility.isNumber(args)) {
            id = Integer.parseInt(args);
            commandEvent.reply(counter(id,guild).build());
          } else if (Utility.isNumber(args.substring(args.lastIndexOf('=') + 1))) {
            id = Integer.parseInt(args.substring(args.lastIndexOf('=') + 1));
            commandEvent.reply(counter(id,guild).build());
          } else
            CommandResponseHandler.illegal(commandEvent, name);
        }
      });
    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e, commandEvent);
    }
  }

  public EmbedBuilder counter(int id, Config.ConfigGuild guild) {
    PoliticsAndWar politicsAndWar = new PoliticsAndWarBuilder()
        .setApiKey(Bot.config.getCredentials().getMasterPWKey())
        .build();
    NationMilitary nationMilitary = Bot.cacheManager.getNationMilitary();
    if(nationMilitary==null)
      nationMilitary = politicsAndWar.getAllMilitaries();

    List<NationMilitaryContainer> targetFilter = nationMilitary.getNationMilitaries()
        .stream()
        .filter(nationMilitaryContainer -> nationMilitaryContainer.getNationId() == id)
        .collect(Collectors.toList());

    if(targetFilter.size()==1) {
      double targetNSM = calculateNSM(targetFilter.get(0));

      double high = targetFilter.get(0).getScore()*1.33333;
      double low = targetFilter.get(0).getScore()*0.57143;

      List<NationMilitaryContainer> memberFilter = nationMilitary.getNationMilitaries()
          .stream()
          .filter(nationMilitaryContainer -> nationMilitaryContainer.getAllianceId()==guild.getPwId())
          .filter(container -> container.getScore()<=high && container.getScore()>=low)
          .collect(Collectors.toList());

      return buildEmbed(targetFilter.get(0).getNationId(),targetNSM,getTop3(createMemberMap(memberFilter)).entrySet());
    }
    return null;
  }

  private EmbedBuilder buildEmbed(int target, double targetNSM, Set<Map.Entry<Integer, Double>> top3) {
    SNationContainer container = Bot.cacheManager.getNations()
        .getNationsContainer()
        .stream()
        .filter(nation -> nation.getNationId() == target)
        .collect(Collectors.toList())
        .get(0);

    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.CYAN)
        .setAuthor(container.getNation()+" - "+container.getScore()+" - "+String.format("%.2f",targetNSM),
            "https://politicsandwar.com/nation/id="+container.getNationId())
        .setTitle("Counter Finder")
        .setDescription("**_Nation Name - Score - Nozomi Strength Meter©_**")
        .setFooter("Politics And War", "https://cdn.discordapp.com/attachments/392736524308840448/485867309995524096/57ad65f5467e958a079d2ee44a0e80ce.png")
        .setTimestamp(Instant.now());

    int count =1;
    for(Map.Entry<Integer,Double> entry : top3) {
      SNationContainer temp = Bot.cacheManager.getNations()
          .getNationsContainer()
          .stream()
          .filter(nationContainer -> nationContainer.getNationId() == entry.getKey())
          .collect(Collectors.toList())
          .get(0);

      String s = String.format("[%s](%s) - %.2f - %.2f", temp.getNation(),"https://politicsandwar.com/nation/id="+temp.getNationId(), temp.getScore(), entry.getValue());
      embedBuilder.addField("Preference #"+count,s,false);
      count++;
    }
    return embedBuilder;
  }

  private LinkedHashMap<Integer, Double> getTop3(HashMap<Integer,Double> memberMap) {
    LinkedHashMap<Integer,Double> insertLinkMap = new LinkedHashMap<>();
    LinkedHashMap<Integer, Double> sortByValue = sortByValue(memberMap);

    int i=1;
    for(Map.Entry<Integer,Double> entry: sortByValue.entrySet()) {
      if(i==4)
        break;
      insertLinkMap.put(entry.getKey(),entry.getValue());
      i++;
    }
    return insertLinkMap;
  }


  private LinkedHashMap<Integer, Double> sortByValue(final HashMap<Integer, Double> unsorted) {
    return unsorted.entrySet()
        .stream()
        .sorted((Map.Entry.<Integer, Double>comparingByValue().reversed()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
  }

  private HashMap<Integer,Double> createMemberMap(List<NationMilitaryContainer> members) {
    HashMap<Integer,Double> result = new HashMap<>();
    for(NationMilitaryContainer member : members) {
      result.put(member.getNationId(),calculateNSM(member));
    }
    return result;
  }

  private double calculateNSM(NationMilitaryContainer container) {
    return (container.getSoldiers() + (container.getTanks()*6) + (container.getAircraft()*83.33) + (container.getShips()*1000))/750;
  }
}