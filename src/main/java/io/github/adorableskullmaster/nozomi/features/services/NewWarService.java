package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.WarsRecord;
import io.github.adorableskullmaster.nozomi.core.database.layer.BotDatabase;
import io.github.adorableskullmaster.nozomi.core.database.layer.GuildSettings;
import io.github.adorableskullmaster.nozomi.core.database.layer.tables.ModuleSettings;
import io.github.adorableskullmaster.nozomi.core.database.layer.tables.WarModule;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.pw.member.CounterCommand;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.domains.War;
import io.github.adorableskullmaster.pw4j.domains.Wars;
import io.github.adorableskullmaster.pw4j.domains.subdomains.NationMilitaryContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SWarContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.WarContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import org.jooq.Result;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NewWarService implements Runnable {

  private static PoliticsAndWar politicsAndWar = Instances.getDefaultPW();
  private BotDatabase db;

  public NewWarService() {
    try {
      db = Instances.getBotDatabaseLayer();
    } catch (SQLException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  @Override
  public void run() {
    try {
      Bot.LOGGER.info("Starting War Thread");

      List<War> newWars = getNewWars();
      List<Long> guildIds = db.getAllActivatedGuildIds();

      Bot.LOGGER.info("New Wars: {}", newWars.size());


      for (int i = newWars.size() - 1; i >= 0; i--) {

        WarContainer warObj = newWars.get(i).getWar().get(0);

        for (Long guildId : guildIds) {
          GuildSettings guildSettings = db.getGuildSettings(guildId);
          List<SNationContainer> nations = Bot.CACHE.getNations().getNationsContainer();
          List<NationMilitaryContainer> nationMilitaries = Bot.CACHE.getNationMilitary().getNationMilitaries();

          int aggId = Integer.parseInt(warObj.getAggressorId());
          int defId = Integer.parseInt(warObj.getDefenderId());

          SNationContainer agg = nations.stream()
              .filter(nationContainer -> nationContainer.getNationId() == aggId)
              .findFirst()
              .orElse(null);
          SNationContainer def = nations.stream()
              .filter(nationContainer -> nationContainer.getNationId() == defId)
              .findFirst()
              .orElse(null);

          NationMilitaryContainer aggMil = nationMilitaries.stream()
              .filter(nationMilitaryContainer -> nationMilitaryContainer.getNationId() == aggId)
              .findFirst()
              .orElse(null);

          NationMilitaryContainer defMil = nationMilitaries.stream()
              .filter(nationMilitaryContainer -> nationMilitaryContainer.getNationId() == defId)
              .findFirst()
              .orElse(null);

          ModuleSettings moduleSettings = guildSettings.getModuleSettings();
          if (moduleSettings.isWarModuleEnabled() && (agg != null && def != null)) {
            WarModule warModuleSettings = guildSettings.getWarModuleSettings();

            if (agg.getAllianceid() == moduleSettings.getAaId() || def.getAllianceid() == moduleSettings.getAaId()) {
              EmbedBuilder embedBuilder = embed(warObj, agg, def, aggMil, defMil);

              if (agg.getAllianceid() == moduleSettings.getAaId()) {
                embedBuilder.setColor(Color.GREEN);
                Bot.jda.getGuildById(guildSettings.getId())
                    .getTextChannelById(warModuleSettings.getOffensiveWarChannel())
                    .sendMessage(embedBuilder.build())
                    .queue();
              } else {
                embedBuilder.setColor(Color.RED);
                Message counter = new CounterCommand().getMessage(agg.getNationId(), guildSettings);

                Bot.jda.getGuildById(guildSettings.getId())
                    .getTextChannelById(warModuleSettings.getDefensiveWarChannel())
                    .sendMessage(embedBuilder.build())
                    .queue();
                if (warModuleSettings.getAutoCounter()) {
                  Bot.jda.getGuildById(guildSettings.getId())
                      .getTextChannelById(warModuleSettings.getDefensiveWarChannel())
                      .sendMessage(counter)
                      .queue();
                }
              }
            }
          }
        }
      }
    } catch (Throwable e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  private EmbedBuilder embed(WarContainer warObj, SNationContainer agg, SNationContainer def, NationMilitaryContainer aggMil, NationMilitaryContainer defMil) {
    return new EmbedBuilder().setTitle(String.format("%s on %s: " + warObj.getWarType(), agg.getAlliance(), def.getAlliance()))
        .setDescription(String.format(
            "[%s of %s](https://politicsandwar.com/nation/id=%d) attacked [%s of %s](https://politicsandwar.com/nation/id=%d) \n\n Reason: `%s`",
            agg.getLeader(), agg.getNation(), agg.getNationId(), def.getLeader(), def.getNation(), def.getNationId(), warObj.getWarReason()))
        .addField("Score", String.format(
            "%.1f on %.1f",
            agg.getScore(), def.getScore()
            ),
            true)
        .addField("Cities", String.format(
            "%d on %d",
            agg.getCities(), def.getCities()
            ),
            true)
        .addField("Last Active", String.format(
            "%s on %s",
            Utility.getMinutesString(agg.getMinutessinceactive()), Utility.getMinutesString(def.getMinutessinceactive())
            ),
            true)
        .addField("War Policy", String.format(
            "%s on %s",
            agg.getWarPolicy(), def.getWarPolicy()
            ),
            true)
        .addField("War Slots", String.format(
            "%d/5 & %d/3 on %d/5 & %d/3",
            agg.getOffensivewars(), agg.getDefensivewars(),
            def.getOffensivewars(), def.getDefensivewars()
            ),
            true)
        .addField("Military", String.format(
            "%d/%d/%d/%d on %d/%d/%d/%d",
            aggMil.getSoldiers(), aggMil.getTanks(), aggMil.getAircraft(), aggMil.getShips(),
            defMil.getSoldiers(), defMil.getTanks(), defMil.getAircraft(), defMil.getShips()
            ),
            true)
        .setFooter("Politics And War", Utility.getPWIcon())
        .setTimestamp(Utility.convertISO8601(warObj.getDate()));
  }

  private List<War> getNewWars() {
    List<Integer> newWars = getWarsFromAPI();
    List<Integer> oldWars = getWarsFromDB();
    storeNewWars(newWars);
    return getDiff(oldWars, newWars);
  }

  private List<War> getDiff(List<Integer> oldWars, List<Integer> newWars) {
    List<War> diff = new ArrayList<>();
    for (Integer id : newWars) {
      if (!oldWars.contains(id)) {
        diff.add(politicsAndWar.getWar(id));
      }
    }
    return diff;
  }

  private List<Integer> getWarsFromAPI() {
    Wars wars = politicsAndWar.getWarsByAmount(25);
    return wars.getWars()
        .stream()
        .map(SWarContainer::getWarID)
        .collect(Collectors.toList());
  }

  private List<Integer> getWarsFromDB() {
    List<Integer> result = new ArrayList<>();
    result.add(0);
    Result<WarsRecord> warsDir = db.getWarsDir();
    for (WarsRecord warsRecord : warsDir) {
      result.add(warsRecord.getWarid());
    }
    return result;
  }

  private void storeNewWars(List<Integer> newWars) {
    db.storeWarsDir(newWars);
  }
}
