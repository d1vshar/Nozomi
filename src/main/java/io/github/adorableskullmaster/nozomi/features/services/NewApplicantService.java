package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.DB;
import io.github.adorableskullmaster.nozomi.core.database.layer.Guild;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jooq.exception.DataAccessException;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewApplicantService implements Runnable {

  @Override
  public void run() {
    try {
      Bot.LOGGER.info("Starting Applicant Thread");
      DB db = Instances.getDBLayer();
      List<Long> guildIds = db.getAllSetupGuildIds();

      for (Long guildId : guildIds) {

        Guild guild = db.getGuild(guildId);

        if (guild.isSetup() && guild.isApplicantNotifier()) {
          List<SNationContainer> newApplicants = getNewApplicants(guild);

          for (SNationContainer applicant : newApplicants) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("New Applicant: " + applicant.getNation())
                .setColor(Color.CYAN)
                .setAuthor("https://politicsandwar.com/alliance/id=" + guild.getPwId(), "https://politicsandwar.com/alliance/id=" + guild.getPwId());

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

  private List<SNationContainer> getNewApplicants(Guild guild) {
    List<SNationContainer> containerApplicants = getCurrentApplicants(guild.getPwId());

    List<Integer> currentApplicants = containerApplicants.stream()
        .map(SNationContainer::getNationId)
        .collect(Collectors.toList());
    List<Integer> loadedApplicants = getLoadedApplicants(guild);

    update(guild, currentApplicants);
    List<Integer> diff = getDiff(loadedApplicants, currentApplicants);

    return containerApplicants.stream()
        .filter(nationContainer -> diff.contains(nationContainer.getNationId()))
        .collect(Collectors.toList());
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

  private List<SNationContainer> getCurrentApplicants(int aid) {
    return Bot.CACHE.getNations()
        .getNationsContainer()
        .stream()
        .filter(nationContainer -> nationContainer.getAllianceid() == aid)
        .filter(nationContainer -> nationContainer.getAllianceposition() == 1)
        .collect(Collectors.toList());
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
