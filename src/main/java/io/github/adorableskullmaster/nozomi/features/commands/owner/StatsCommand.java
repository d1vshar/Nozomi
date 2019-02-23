package io.github.adorableskullmaster.nozomi.features.commands.owner;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.domains.Alliance;
import io.github.adorableskullmaster.pw4j.domains.Nation;
import io.github.adorableskullmaster.pw4j.domains.Wars;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SWarContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.WarContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.List;

@Deprecated
public class StatsCommand extends Command {

  private PoliticsAndWar politicsAndWar;

  public StatsCommand(PoliticsAndWar politicsAndWar) {
    this.name = "stats";
    this.help = "View alliance stats (maybe)";
    this.arguments = "++stats <number><h|d|w>";
    this.category = new Category("Everyone");
    this.guildOnly = true;
    this.politicsAndWar = politicsAndWar;
    this.cooldown = 300;
    this.cooldownScope = CooldownScope.USER;
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    commandEvent.async(
        () -> {
          commandEvent.getChannel().sendTyping().queue();
          commandEvent.reply("It will take at least 15 minutes to produce fresh stats. Please wait till then!");
          Bot.LOGGER.info("Stat Command Started");
          List<MessageEmbed> embeds = produceStats();
          Bot.LOGGER.info("Stats Produced");
          MessageEmbed[] embedsArray = embeds.toArray(new MessageEmbed[0]);
          for (MessageEmbed embed : embedsArray) {
            commandEvent.reply(embed);
          }
          Bot.LOGGER.info("Stats Sent");
        }
    );
  }

  private List<MessageEmbed> produceStats() {
    Bot.LOGGER.info("produceStats triggered");
    return fetchStats();
  }

  private List<MessageEmbed> fetchStats() {
    Bot.LOGGER.info("fetchStats triggered");
    Alliance arrgh = politicsAndWar.getAlliance(913);
    List<Integer> memberIdList = arrgh.getMemberIdList();
    Wars wars = politicsAndWar.getWars(2500, 913);
    List<SWarContainer> warList = wars.getWars();

    HashMap<Integer, MemberStat> memberStats = new HashMap<>();
    AllianceStat aaStat = new AllianceStat(0, 0, 0, 0);

    for (Integer id : memberIdList) {
      memberStats.put(id, new MemberStat(0, 0, 0, 0, 0));
      Bot.LOGGER.info("Member registered");
    }

    for (SWarContainer swar : warList) {
      Bot.LOGGER.info("sWar Loop");
      Instant warDate = Instant.from(Utility.convertISO8601(swar.getDate()));
      Instant now = Instant.now();
      Duration duration = Duration.between(warDate, now);
      if (duration.getSeconds() <= 86400) {
        Bot.LOGGER.info("time condition passed " + swar.getDate());
        WarContainer war = politicsAndWar.getWar(swar.getWarID()).getWar().get(0);
        Bot.LOGGER.info("mWar fetched");
        MemberStat memberStat;
        //Wars Block
        if (swar.getAttackerAA().equalsIgnoreCase("arrgh") && memberStats.containsKey(swar.getAttackerID())) {
          memberStat = memberStats.get(swar.getAttackerID());
          aaStat.setWarsDeclaredBy(aaStat.getWarsDeclaredBy() + 1);
          memberStat.setWarsDeclaredBy(memberStat.getWarsDeclaredBy() + 1);

          if (swar.getStatus().equalsIgnoreCase("attacker victory")) {
            aaStat.setWarsWon(aaStat.getWarsWon() + 1);
            memberStat.setWarsWon(memberStat.getWarsWon() + 1);
          } else if (swar.getStatus().equalsIgnoreCase("defender victory")) {
            aaStat.setWarsLost(aaStat.getWarsLost() + 1);
            memberStat.setWarsLost(memberStat.getWarsLost() + 1);
          }

          memberStat.setMoneyLooted(memberStat.getMoneyLooted() + war.getDefenderMoneyLost());

          memberStats.put(swar.getAttackerID(), memberStat);
          Bot.LOGGER.info("Stats Updated Aggressor");
        } else if (swar.getDefenderAA().equalsIgnoreCase("arrgh") && memberStats.containsKey(swar.getDefenderID())) {
          memberStat = memberStats.get(swar.getDefenderID());
          aaStat.setWarsDeclaredOn(aaStat.getWarsDeclaredOn() + 1);
          memberStat.setWarsDeclaredOn(memberStat.getWarsDeclaredOn() + 1);

          if (swar.getStatus().equalsIgnoreCase("attacker victory")) {
            aaStat.setWarsLost(aaStat.getWarsLost() + 1);
            memberStat.setWarsLost(memberStat.getWarsLost() + 1);
          } else if (swar.getStatus().equalsIgnoreCase("defender victory")) {
            aaStat.setWarsWon(aaStat.getWarsWon() + 1);
            memberStat.setWarsWon(memberStat.getWarsWon() + 1);
          }

          memberStat.setMoneyLooted(memberStat.getMoneyLooted() + war.getAggressorMoneyLost());

          memberStats.put(swar.getDefenderID(), memberStat);
          Bot.LOGGER.info("Stats Updated Defender");
        }
      }
    }
    Bot.LOGGER.info(memberStats.toString());
    Bot.LOGGER.info(aaStat.toString());
    return createEmbeds(memberStats);
  }

  private List<MessageEmbed> createEmbeds(HashMap<Integer, MemberStat> memberStats) {
    Bot.LOGGER.info("createEmbeds triggered");
    ArrayList<MessageEmbed> embeds = new ArrayList<>();
    ArrayList<HashMap.Entry<Integer, MemberStat>> list = new ArrayList<>(memberStats.entrySet());
    for (TextConstants value : TextConstants.values()) {
      HashMap<Integer, Integer> input = new HashMap<>();
      switch (value) {
        case MEMBER_MONEY_LOOT:
          for (Map.Entry<Integer, MemberStat> entry : list) {
            input.put(entry.getKey(), entry.getValue().getMoneyLooted());
          }
          break;
        case MEMBER_WAR_WON:
          for (Map.Entry<Integer, MemberStat> entry : list) {
            input.put(entry.getKey(), entry.getValue().getWarsWon());
          }
          break;
        case MEMBER_WAR_LOST:
          for (Map.Entry<Integer, MemberStat> entry : list) {
            input.put(entry.getKey(), entry.getValue().getWarsLost());
          }
          break;
        case MEMBER_WAR_DECLARED_BY:
          for (Map.Entry<Integer, MemberStat> entry : list) {
            input.put(entry.getKey(), entry.getValue().getWarsDeclaredBy());
          }
          break;
        case MEMBER_WAR_DECLARED_ON:
          for (Map.Entry<Integer, MemberStat> entry : list) {
            input.put(entry.getKey(), entry.getValue().getWarsDeclaredOn());
          }
          break;

      }
      embeds.add(createMemberEmbed(value.getTitle(), value.getDescription(), Utility.sortByValue(input, false)));
    }
    return embeds;
  }

  private MessageEmbed createMemberEmbed(String title, String description, LinkedHashMap<Integer, Integer> leaderboard) {
    Bot.LOGGER.info("createMemberEmbed triggered");
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setTitle(title)
        .setDescription(description)
        .setColor(Color.CYAN)
        .setFooter("Arrgh! Leaderboards", null)
        .setTimestamp(Instant.now());

    Set<Map.Entry<Integer, Integer>> entries = leaderboard.entrySet();
    int count = 0;
    for (Map.Entry<Integer, Integer> entry : entries) {
      String name;
      Nation nation = politicsAndWar.getNation(entry.getKey());
      name = nation.getName();
      String id = nation.getNationid();
      embedBuilder.addField("**#" + count + " | " + name + ": https://politicsandwar.com/nation/id=" + id + "**", Integer.toString(entry.getValue()), false);
      if (count == 4)
        break;
      count++;
    }
    return embedBuilder.build();
  }

  private enum TextConstants {
    MEMBER_MONEY_LOOT("Member Loot Leaderboard", "Members with most loot within specified time period"),
    MEMBER_WAR_WON("Member Wars Won Leaderboard", "Members with most war won within specified time period"),
    MEMBER_WAR_LOST("Member Wars Lost Leaderboard", "Members with most wars lost within specified time period"),
    MEMBER_WAR_DECLARED_BY("Member Wars Declared By Leaderboard", "Members with most wars declared on other nations within specified time period"),
    MEMBER_WAR_DECLARED_ON("Member Wars Declared Upon Leaderboard", "Members with most wars declared upon within specified time period"),
    ALLIANCE_WAR_WON("Alliance Wars Won Leaderboard", "Wars won within specified time period"),
    ALLIANCE_WAR_LOST("Alliance Wars Lost Leaderboard", "Wars lost within specified time period"),
    ALLIANCE_WAR_DECLARED_BY("Alliance Wars Declared By Leaderboard", "Wars declared on others within specified time period"),
    ALLIANCE_WAR_DECLARED_ON("Alliance Wars Declared Upon Leaderboard", "Wars declared upon within specified time period");

    private String title, description;

    TextConstants(String title, String description) {
      this.title = title;
      this.description = description;
    }

    public String getTitle() {
      return title;
    }

    public String getDescription() {
      return description;
    }
  }

  static private class MemberStat {
    private int moneyLooted;
    private int warsDeclaredOn;
    private int warsDeclaredBy;
    private int warsWon;
    private int warsLost;

    MemberStat(int moneyLooted, int warsDeclaredOn, int warsDeclaredBy, int warsWon, int warsLost) {
      this.moneyLooted = moneyLooted;
      this.warsDeclaredOn = warsDeclaredOn;
      this.warsDeclaredBy = warsDeclaredBy;
      this.warsWon = warsWon;
      this.warsLost = warsLost;
    }

    int getMoneyLooted() {
      return moneyLooted;
    }

    void setMoneyLooted(int moneyLooted) {
      this.moneyLooted = moneyLooted;
    }

    int getWarsDeclaredOn() {
      return warsDeclaredOn;
    }

    void setWarsDeclaredOn(int warsDeclaredOn) {
      this.warsDeclaredOn = warsDeclaredOn;
    }

    int getWarsWon() {
      return warsWon;
    }

    void setWarsWon(int warsWon) {
      this.warsWon = warsWon;
    }

    int getWarsLost() {
      return warsLost;
    }

    void setWarsLost(int warsLost) {
      this.warsLost = warsLost;
    }

    int getWarsDeclaredBy() {
      return warsDeclaredBy;
    }

    void setWarsDeclaredBy(int warsDeclaredBy) {
      this.warsDeclaredBy = warsDeclaredBy;
    }

    @Override
    public String toString() {
      return "MemberStat{" +
          "moneyLooted=" + moneyLooted +
          ", warsDeclaredOn=" + warsDeclaredOn +
          ", warsDeclaredBy=" + warsDeclaredBy +
          ", warsWon=" + warsWon +
          ", warsLost=" + warsLost +
          '}';
    }
  }

  static private class AllianceStat {
    private int warsDeclaredOn;
    private int warsDeclaredBy;
    private int warsWon;
    private int warsLost;

    AllianceStat(int warsDeclaredOn, int warsDeclaredBy, int warsWon, int warsLost) {
      this.warsDeclaredOn = warsDeclaredOn;
      this.warsDeclaredBy = warsDeclaredBy;
      this.warsWon = warsWon;
      this.warsLost = warsLost;
    }

    int getWarsDeclaredOn() {
      return warsDeclaredOn;
    }

    void setWarsDeclaredOn(int warsDeclaredOn) {
      this.warsDeclaredOn = warsDeclaredOn;
    }

    int getWarsWon() {
      return warsWon;
    }

    void setWarsWon(int warsWon) {
      this.warsWon = warsWon;
    }

    int getWarsLost() {
      return warsLost;
    }

    void setWarsLost(int warsLost) {
      this.warsLost = warsLost;
    }

    int getWarsDeclaredBy() {
      return warsDeclaredBy;
    }

    void setWarsDeclaredBy(int warsDeclaredBy) {
      this.warsDeclaredBy = warsDeclaredBy;
    }

    @Override
    public String toString() {
      return "AllianceStat{" +
          "warsDeclaredOn=" + warsDeclaredOn +
          ", warsDeclaredBy=" + warsDeclaredBy +
          ", warsWon=" + warsWon +
          ", warsLost=" + warsLost +
          '}';
    }
  }
}
