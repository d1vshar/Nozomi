package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.config.Config;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.pw.member.CounterCommand;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.Nation;
import io.github.adorableskullmaster.pw4j.domains.War;
import io.github.adorableskullmaster.pw4j.domains.Wars;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SWarContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.WarContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewWarService implements Runnable {

  private static PoliticsAndWar politicsAndWar = new PoliticsAndWarBuilder().build();

  @Override
  public void run() {
    try {
      Bot.LOGGER.info("Starting War Thread");
      List<Integer> newWars = fetchWarsFromAPI();
      List<Integer> oldWars = fetchWarsFromPG();
      List<War> result = fetchNewWars(oldWars, newWars);
      Bot.LOGGER.info("New Wars: {}", result.size());
      storeNewWars(newWars);

      List<Config.ConfigGuild> guilds = Bot.config.getGuilds();

      EmbedBuilder embedBuilder = new EmbedBuilder();

      for (int i = result.size() - 1; i >= 0; i--) {

        War war = result.get(i);
        WarContainer warobj = war.getWar().get(0);

        for(Config.ConfigGuild guild : guilds) {
          if (AuthUtility.checkService("NewWarService",guild.getDiscordId())) {

            Nation agg = politicsAndWar.getNation(Integer.parseInt(warobj.getAggressorId()));
            Nation def = politicsAndWar.getNation(Integer.parseInt(warobj.getDefenderId()));

            if (Integer.parseInt(agg.getAllianceid())==guild.getPwId() || Integer.parseInt(def.getAllianceid())==guild.getPwId()) {
              embedBuilder.clear();
              embedBuilder.setTitle("New War: " + warobj.getWarType())
                  .setThumbnail(Bot.jda.getSelfUser().getAvatarUrl())
                  .setDescription("Reason: " + warobj.getWarReason())
                  .addField("Aggressor Nation Name", "[" + agg.getName() + "](https://politicsandwar.com/nation/id=" + agg.getNationid() + ")", true)
                  .addField("Aggressor Leader Name", agg.getLeadername(), true)
                  .addField("Aggressor Alliance Name", "[" + warobj.getAggressorAllianceName() + "](https://politicsandwar.com/alliance/id=" + agg.getAllianceid() + ")", true)
                  .addField("Aggressor Score", agg.getScore(), true)
                  .addField("Defender Nation Name", "[" + def.getName() + "](https://politicsandwar.com/nation/id=" + def.getNationid() + ")", true)
                  .addField("Defender Leader Name", def.getLeadername(), true)
                  .addField("Defender Alliance Name", "[" + warobj.getDefenderAllianceName() + "](https://politicsandwar.com/alliance/id=" + def.getAllianceid() + ")", true)
                  .addField("Defender Score", def.getScore(), true)
                  .setFooter("Politics And War", "https://cdn.discordapp.com/attachments/392736524308840448/485867309995524096/57ad65f5467e958a079d2ee44a0e80ce.png")
                  .setTimestamp(Utility.convertISO8601(warobj.getDate()));

              if(Integer.parseInt(agg.getAllianceid()) == guild.getPwId()) {
                embedBuilder.setColor(Color.GREEN);
                Bot.jda.getGuildById(guild.getDiscordId())
                    .getTextChannelById(guild.getOffensiveWarChannel())
                    .sendMessage(embedBuilder.build())
                    .queue();
              }
              else {
                embedBuilder.setColor(Color.RED);
                EmbedBuilder counter = new CounterCommand().counter(Integer.parseInt(agg.getNationid()), guild);

                Bot.jda.getGuildById(guild.getDiscordId())
                    .getTextChannelById(guild.getDefensiveWarChannel())
                    .sendMessage(embedBuilder.build())
                    .queue();
                if(guild.isAutoCounter()) {
                  Bot.jda.getGuildById(guild.getDiscordId())
                      .getTextChannelById(guild.getDefensiveWarChannel())
                      .sendMessage(counter.build())
                      .queue();
                }
              }
            }
          }
        }
      }
    } catch (Throwable e) {
      Bot.botExceptionHandler.captureException(e);
    }
  }

  private List<War> fetchNewWars(List<Integer> oldWars, List<Integer> newWars) {
    List<War> diff = new ArrayList<>();
    for (Integer id : newWars) {
      if (!oldWars.contains(id)) {
        diff.add(politicsAndWar.getWar(id));
      }
    }
    return diff;
  }

  private List<Integer> fetchWarsFromPG() throws SQLException {
    List<Integer> result = new ArrayList<>();
    result.add(0);
    Statement statement;
    statement = Bot.pg.getConn().createStatement();
    ResultSet rs = statement.executeQuery("SELECT * FROM wars");
    while (rs.next()) {
      result.add(rs.getInt(1));
    }
    rs.close();
    statement.close();
    return result;
  }

  private List<Integer> fetchWarsFromAPI() {
    Integer[] aids = Bot.config.getGuilds().stream().map(Config.ConfigGuild::getPwId).toArray(Integer[]::new);
    Wars wars = politicsAndWar.getWars(25, aids);
    return wars.getWars()
        .stream()
        .map(SWarContainer::getWarID)
        .collect(Collectors.toList());
  }

  private void storeNewWars(List<Integer> newWars) throws SQLException {
    String delete = "DELETE FROM wars";
    Statement statement = Bot.pg.getConn().createStatement();
    statement.execute(delete);
    for (Integer id : newWars) {
      String insert = "INSERT INTO wars (warID) VALUES(?)";
      PreparedStatement pstmt = Bot.pg.getConn().prepareStatement(insert);
      pstmt.setInt(1, id);
      pstmt.executeUpdate();
      pstmt.close();
    }
    statement.close();
  }
}
