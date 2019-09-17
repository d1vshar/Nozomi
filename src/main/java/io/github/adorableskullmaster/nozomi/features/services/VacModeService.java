package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.ServerProfileRepository;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.DiscordServer;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarAPIException;
import io.github.adorableskullmaster.pw4j.domains.Nations;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VacModeService implements Runnable {

  private ServerProfileRepository serverProfileRepository;

  public VacModeService() {
    this.serverProfileRepository = new ServerProfileRepository();
  }

  @Override
  public void run() {
    try {
      Date date = Date.from(Instant.now());
      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      cal.setTime(date);
      if (cal.get(Calendar.HOUR) % 2 == 0) {
        if (cal.get(Calendar.MINUTE) == 5) {
          Bot.LOGGER.info("Starting VM-Beige Thread");
          process();
        }
      }
    } catch (Throwable e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  private void process() throws PoliticsAndWarAPIException, NullPointerException {
    try {

      Nations cacheNations = Bot.CACHE.getNations();
      List<DiscordServer> allDiscordServers = serverProfileRepository.findAllDiscordServers();

      if (cacheNations.isSuccess()) {
        List<SNationContainer> nationsContainer = cacheNations.getNationsContainer();
        for (SNationContainer sNationContainer : nationsContainer) {

          if (Integer.parseInt(sNationContainer.getVacmode()) == 1) {
            for (DiscordServer discordServer : allDiscordServers) {

              if (discordServer.getVacModeModule() != null) {
                int[] allianceIds = discordServer.getVacModeModule().getAllianceIds();

                if (allianceIds.length > 0) {
                  List<Integer> allianceIdsList = IntStream.of(allianceIds).boxed().collect(Collectors.toList());
                  int DEFAULT_FILTER = 500;
                  int scoreFilter = discordServer.getVacModeModule()
                          .getScoreFilter() == 0 ? discordServer.getVacModeModule().getScoreFilter() : DEFAULT_FILTER;

                  if (allianceIdsList.contains(sNationContainer.getAllianceid()) &&
                          sNationContainer.getScore() > scoreFilter) {
                    TextChannel channel = Bot.jda.getGuildById(discordServer.getServerId())
                            .getTextChannelById(discordServer.getVacModeModule().getBroadcastChannel());
                    channel.sendMessage(
                            new EmbedBuilder()
                                    .setAuthor("https://politicsandwar.com/nation/id=" + sNationContainer.getNationId(),
                                            "https://politicsandwar.com/nation/id=" + sNationContainer.getNationId())
                                    .setColor(Color.WHITE)
                                    .setTitle(sNationContainer.getNation() + " is leaving VM next turn!")
                                    .addField("Leader Name", sNationContainer.getLeader(), true)
                                    .addField("Alliance", sNationContainer.getAlliance(), true)
                                    .addField("Score", Double.toString(sNationContainer.getScore()), true)
                                    .setFooter("Politics And War", Utility.getPWIcon())
                                    .setTimestamp(Instant.now())
                                    .build()
                    ).queue();
                  }
                }

              }
            }
          }
        }
      }
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }
}
