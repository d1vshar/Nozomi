package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.layer.BotDatabase;
import io.github.adorableskullmaster.nozomi.core.database.layer.GuildSettings;
import io.github.adorableskullmaster.nozomi.core.database.layer.tables.ModuleSettings;
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
      BotDatabase db = Instances.getBotDatabaseLayer();
      List<Long> guildIds = db.getAllActivatedGuildIds();

      for (Long guildId : guildIds) {

        GuildSettings guildSettings = db.getGuildSettings(guildId);
        ModuleSettings moduleSettings = guildSettings.getModuleSettings();

        if (moduleSettings.isApplicantModuleEnabled()) {
          List<SNationContainer> newApplicants = getNewApplicants(db,guildId,moduleSettings.getAaId());

          for (SNationContainer applicant : newApplicants) {
            EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("New Applicant: " + applicant.getNation())
                .setColor(Color.GREEN)
                .setAuthor("https://politicsandwar.com/alliance/id=" + moduleSettings.getAaId(),
                    "https://politicsandwar.com/alliance/id=" + moduleSettings.getAaId());

            Bot.jda.getTextChannelById(moduleSettings.getId())
                .sendMessage(embedBuilder.build())
                .queue();

          }
        }
      }
    } catch (Throwable e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  private List<SNationContainer> getNewApplicants(BotDatabase botDatabase, long id, int aid) {
    List<SNationContainer> containerApplicants = getCurrentApplicants(aid);

    List<Integer> currentApplicants = containerApplicants.stream()
        .map(SNationContainer::getNationId)
        .collect(Collectors.toList());
    List<Integer> loadedApplicants = getLoadedApplicants(botDatabase, id);

    update(botDatabase, currentApplicants, id);
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

  private List<Integer> getLoadedApplicants(BotDatabase botDatabase, long id) {
    ArrayList<Integer> result = new ArrayList<>();
    try {
      List<Integer> applicants = botDatabase.getApplicants(id);
      if (applicants != null)
        return applicants;
    } catch (DataAccessException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      result.add(0);
    }
    return result;
  }

  private void update(BotDatabase botDatabase, List<Integer> list, long id) {
    try {
      botDatabase.updateApplicants(list,id);
    } catch (DataAccessException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

}
