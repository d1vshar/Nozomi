package io.github.adorableskullmaster.nozomi.features.commands.pw.member;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.features.commands.MemberPoliticsAndWarCommand;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.domains.Alliance;
import io.github.adorableskullmaster.pw4j.domains.City;
import io.github.adorableskullmaster.pw4j.domains.Nation;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AnalyzeCommand extends MemberPoliticsAndWarCommand {

  public AnalyzeCommand() {
    this.name = "analyze";
    this.aliases = new String[]{"kowalski"};
    this.help = "Analyzes target nations";
    this.arguments = "++analyze <nation link>";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      commandEvent.async(
          () -> {
            commandEvent.getChannel().sendTyping().queue();
            commandEvent.getChannel().sendMessage("Analyzing... might take some time!").queue(
                (event) -> {
                  if (identify(commandEvent.getArgs()) == ResponseType.NATION) {
                    EmbedBuilder embedBuilder = analyzeNation(commandEvent.getArgs());
                    event.delete().queue();
                    if (embedBuilder != null)
                      commandEvent.reply(embedBuilder.build());
                    else
                      throw new NullPointerException();
                  }
                }
            );
          }
      );
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }

  private ResponseType identify(String url) {
    if (url.startsWith("https://politicsandwar.com/nation/id="))
      return ResponseType.NATION;
    else if (url.startsWith("https://politicsandwar.com/alliance/id=")) {
      return ResponseType.ALLIANCE;
    } else
      return ResponseType.UNKNOWN;
  }

  private EmbedBuilder analyzeNation(String url) {
    try {
      int nid = Integer.parseInt(url.substring(url.indexOf("=") + 1));
      PoliticsAndWar politicsAndWar = Instances.getDefaultPW();
      Nation nation = politicsAndWar.getNation(nid);
      List<String> cityIds = nation.getCityids();
      int cityCount = nation.getCities();

      Date date = new SimpleDateFormat("YYYY-MM-DD").parse(nation.getFounded().substring(0, 10));
      long seconds = Instant.now().getEpochSecond() - Instant.from(date.toInstant()).getEpochSecond();

      boolean newNation;
      boolean inactive;
      List<Boolean> powered = new ArrayList<>();
      List<Integer> mines = new ArrayList<>();
      List<Integer> manufacturing = new ArrayList<>();
      List<Double> commerce = new ArrayList<>();
      double military;

      newNation = seconds <= 30 * 24 * 60 * 60;
      inactive = nation.getMinutessinceactive() >= 3 * 24 * 60;

      //Cities analyses
      for (String cityId : cityIds) {
        City city = politicsAndWar.getCity(Integer.parseInt(cityId));

        //Powered
        powered.add(city.getPowered().equalsIgnoreCase("Yes"));

        commerce.add((Integer.parseInt(city.getImpSupermarket()) * 0.03) + (Integer.parseInt(city.getImpBank()) * 0.05)
            + (Integer.parseInt(city.getImpMall()) * 0.09) + (Integer.parseInt(city.getImpStadium()) * 0.12) + (Integer.parseInt(city.getImpSubway())) * 0.08);

        manufacturing.add(Integer.parseInt(city.getImpGasrefinery()) + Integer.parseInt(city.getImpMunitionsfactory())
            + Integer.parseInt(city.getImpAluminumrefinery()) + Integer.parseInt(city.getImpSteelmill()));

        //Mines
        mines.add(Integer.parseInt(city.getImpCoalmine()) + Integer.parseInt(city.getImpIronmine())
            + Integer.parseInt(city.getImpLeadmine()) + Integer.parseInt(city.getImpBauxitemine())
            + Integer.parseInt(city.getImpOilwell()) + Integer.parseInt(city.getImpUramine()));
      }

      //Military %
      double tanks = Double.parseDouble(nation.getTanks()) / (1250 * cityCount);
      double aircrafts = Double.parseDouble(nation.getAircraft()) / (90 * cityCount);
      double ships = Double.parseDouble(nation.getShips()) / (15 * cityCount);
      double soldiers = Double.parseDouble(nation.getSoldiers()) / (15000 * cityCount);
      military = ((soldiers + tanks + aircrafts + ships) / 4);

      Alliance alliance = null;
      if (!nation.getAllianceid().equalsIgnoreCase("0"))
        alliance = politicsAndWar.getAlliance(Integer.parseInt(nation.getAllianceid()));

      return createEmbed(nation, newNation, inactive, mines, powered, commerce, manufacturing, military, alliance);
    } catch (ParseException e) {
      return null;
    }
  }

  private EmbedBuilder createEmbed(Nation nation, boolean newNation, boolean inactive, List<Integer> mines, List<Boolean> powered, List<Double> commerce,
                                   List<Integer> manufacturing, double mil, Alliance aa) {
    String tier = nation.getCities() <= 10 ? "low tier" : (nation.getCities() <= 20 ? "mid tier" : "high tier");
    String activeString = inactive ? "inactive" : "active";
    String nationInfoString, manuComPowString, rawString, militaryString, allianceString = "";

    int totalMines = mines.stream().mapToInt(Integer::intValue).sum();
    double totalComm = commerce.stream().mapToDouble(Double::doubleValue).sum();
    int totalManu = manufacturing.stream().mapToInt(Integer::intValue).sum();

    nationInfoString = String.format("**%s** is a **%s %s** nation has been **%s** lately.", nation.getName(), (newNation ? "new" : "old"), tier, activeString);
    rawString = String.format("It has an average **%s raw mines per city (Total: %d)**", totalMines / nation.getCities(), totalMines);
    manuComPowString = String.format("It has **%s manufacturing improvements per city (Total: %d)** and **%s%% average commerce** ",
        totalManu / nation.getCities(), totalManu, (int) (Math.ceil((totalComm / (double) nation.getCities()) * 100)));
    if (powered.stream().noneMatch(aBoolean -> aBoolean.equals(true)))
      manuComPowString += " but **no powered cities.**";
    else if (powered.stream().allMatch(aBoolean -> aBoolean.equals(true)))
      manuComPowString += " with **all powered cities.**";
    else {
      int poweredCount = Collections.frequency(powered, true);
      manuComPowString += String.format(" with **%s (out of %d) powered cities.**", poweredCount, nation.getCities());
    }
    militaryString = String.format("The nation is **%s militarized (%s%%).**", mil > 0.75 ? "highly" : (mil > 0.4 ? "moderately" : "poorly"),
        (int) (mil * 100));

    if (aa != null)
      allianceString = String.format("%s has %d members with an average score of %s.", aa.getName(), aa.getMembers(),
          (int) Double.parseDouble(aa.getScore()) / aa.getMembers());
    return fillEmbed(nation, nationInfoString, manuComPowString, rawString, militaryString, allianceString);
  }

  private EmbedBuilder fillEmbed(Nation nation, String nationInfoString, String manuComPowString, String rawString, String militaryString,
                                 String allianceString) {
    String url = "https://politicsandwar.com/nation/id=" + nation.getNationid();
    EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("Kowalski, analysis")
        .setColor(Color.CYAN)
        .setAuthor(url, url)
        .setThumbnail("https://i.imgur.com/qbmpc0p.jpg")
        .addField("Target Nation", "[" + nation.getName() + "](" + url + ")", false)
        .setFooter("Politics And War", "https://cdn.discordapp.com/attachments/392736524308840448/485867309995524096/57ad65f5467e958a079d2ee44a0e80ce.png")
        .setTimestamp(Instant.now());

    String ns = String.join("\n", nationInfoString, rawString, manuComPowString, militaryString);
    embedBuilder.addField("Nation Analysis", ns, false);

    if (!allianceString.isEmpty())
      embedBuilder.addField("Alliance Analysis", allianceString, false);

    return embedBuilder;
  }

  private enum ResponseType {
    NATION,
    ALLIANCE,
    UNKNOWN
  }
}