package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.DB;
import io.github.adorableskullmaster.nozomi.core.database.layer.Guild;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.domains.Applicants;
import io.github.adorableskullmaster.pw4j.domains.subdomains.ApplicantNationsContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jooq.exception.DataAccessException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewApplicantService implements Runnable {

  private final PoliticsAndWar politicsAndWar;

  public NewApplicantService() {
    this.politicsAndWar = Instances.getDefaultPW();
  }

  @Override
  public void run() {
    try {
      Bot.LOGGER.info("Starting Applicant Thread");
      DB db = Instances.getDBLayer();
      List<Long> guildIds = db.getAllSetupGuildIds();

      for (Long guildId : guildIds) {

        Guild guild = db.getGuild(guildId);

        if (guild.isSetup() && guild.isApplicantNotifier()) {
          List<Integer> newApplicants = getNewApplicants(guild);

          for (Integer applicant : newApplicants) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("New Applicant: " + politicsAndWar.getNation(applicant).getName())
                .setColor(Color.CYAN)
                .setAuthor("https://politicsandwar.com/alliance/id=" + guild.getPwId(), "https://politicsandwar.com/nation/id=" + guild.getPwId());

            Bot.jda.getTextChannelById(guild.getGuildChannels().getGovChannel())
                .sendMessage(embedBuilder.build())
                .queue();

          }
        }
      }
    } catch (Throwable e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  private List<Integer> getNewApplicants(Guild guild) {
    List<Integer> currentApplicants = getCurrentApplicants(guild.getPwId());
    List<Integer> loadedApplicants = getLoadedApplicants(guild);
    update(guild, currentApplicants);
    return getDiff(loadedApplicants, currentApplicants);
  }

  private List<Integer> getDiff(List<Integer> loaded, List<Integer> current) {
    List<Integer> diff = new ArrayList<>();
    for (Integer id : current) {
      if (!loaded.contains(id)) {
        diff.add(id);
      }
    }
    return diff;
  }

  private List<Integer> getCurrentApplicants(int aid) {
    if (politicsAndWar.getAlliance(aid).getApplicants() > 0) {
      Applicants applicants = politicsAndWar.getApplicants(aid);

      return applicants.getApplicants()
          .stream()
          .map(ApplicantNationsContainer::getNationid)
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  private List<Integer> getLoadedApplicants(Guild guild) {
    ArrayList<Integer> result = new ArrayList<>();
    try {
      List<Integer> applicants = guild.getGuildApplicants().getAllApplicants();
      if (applicants != null)
        return applicants;
    } catch (DataAccessException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      result.add(0);
    }
    return result;
  }

  private void update(Guild guild, List<Integer> list) {
    try {
      guild.getGuildApplicants().update(list);
    } catch (DataAccessException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

}
