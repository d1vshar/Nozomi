package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.config.Config;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.Applicants;
import io.github.adorableskullmaster.pw4j.domains.subdomains.ApplicantNationsContainer;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewApplicantService implements Runnable {

  private final PoliticsAndWar politicsAndWar;

  public NewApplicantService() {
    this.politicsAndWar = new PoliticsAndWarBuilder().build();
  }

  @Override
  public void run() {
    try {
      List<Config.ConfigGuild> guilds = Bot.config.getGuilds();
      for(Config.ConfigGuild guild : guilds) {
        if(AuthUtility.checkService("NewApplicantService",guild.getDiscordId())) {
          List<Integer> currentApplicants = getCurrentApplicants(guild.getPwId());
          List<Integer> loadedApplicants = getLoadedApplicants(guild.getPwId());
          List<Integer> diff = getDiff(loadedApplicants, currentApplicants);
          update(guild.getPwId(),currentApplicants);

          for(Integer id : diff) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("New Applicant")
                .setColor(Color.CYAN)
                .setAuthor("https://politicsandwar.com/nation/id="+id);

            Bot.jda.getTextChannelById(guild.getGovChannel())
                .sendMessage(embedBuilder.build())
                .queue();
          }
        }
      }
    } catch (Throwable e) {
      Bot.botExceptionHandler.captureException(e);
    }
  }

  private List<Integer> getDiff(List<Integer> loaded, List<Integer> current) {
    return current.stream().filter(integer -> !loaded.contains(integer)).collect(Collectors.toList());
  }

  private List<Integer> getCurrentApplicants(int aid) {
    Applicants applicants = politicsAndWar.getApplicants(aid);

    return applicants.getApplicants()
        .stream()
        .map(ApplicantNationsContainer::getNationid)
        .collect(Collectors.toList());
  }

  private List<Integer> getLoadedApplicants(int aid) {
    try {
      Integer[] result = {};
      String SQL = "SELECT list FROM applicants WHERE aid = ?";
      PreparedStatement pstmt = Bot.pg.getConn().prepareStatement(SQL);
      pstmt.setInt(1, aid);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        Array list = rs.getArray("list");
        result = (Integer[]) list.getArray();
      }
      pstmt.close();
      return Arrays.asList(result);
    } catch (SQLException e) {
      Bot.botExceptionHandler.captureException(e);
      return null;
    }
  }

  private void update(int aid, List<Integer> list) {
    try {
      Integer[] integers = list.toArray(new Integer[0]);
      Array intArray = Bot.pg.getConn().createArrayOf("integer",integers);

      String SQL = "UPDATE applicants SET list = ? WHERE aid = ?";
      PreparedStatement pstmt = Bot.pg.getConn().prepareStatement(SQL);

      pstmt.setArray(1,intArray);
      pstmt.setInt(2, aid);
      pstmt.executeUpdate();
      pstmt.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
