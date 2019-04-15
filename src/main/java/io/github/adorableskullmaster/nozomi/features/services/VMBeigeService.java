package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.DB;
import io.github.adorableskullmaster.nozomi.core.database.layer.Guild;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
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

public class VMBeigeService implements Runnable {

  private PoliticsAndWar politicsAndWar = Instances.getDefaultPW();

  @Override
  public void run() {
    try {
      Date date = Date.from(Instant.now());
      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      cal.setTime(date);
      if (cal.get(Calendar.HOUR) % 2 == 0) {
        //noinspection MagicConstant
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
      Nations nationsObj = Bot.CACHE.getNations();
      DB db = Instances.getDBLayer();
      List<Long> guildIds = db.getAllSetupGuildIds();

      if (nationsObj.isSuccess()) {
        List<SNationContainer> nations = nationsObj.getNationsContainer();

        for (SNationContainer nation : nations) {
          if (nation.getScore() > 500) {

            if (Integer.parseInt(nation.getVacmode()) == 1) {
              for (Long guildId : guildIds) {
                Guild guild = db.getGuild(guildId);
                if (guild.isVmBeigeTracker()) {
                  TextChannel channel = Bot.jda.getGuildById(guild.getId()).getTextChannelById(guild.getId());
                  channel.sendMessage(
                      new EmbedBuilder()
                          .setAuthor("https://politicsandwar.com/nation/id=" + nation.getNationId(), "https://politicsandwar.com/nation/id=" + nation.getNationId())
                          .setColor(Color.WHITE)
                          .setTitle(nation.getNation() + " is leaving VM next turn!")
                          .addField("Leader Name", nation.getLeader(), true)
                          .addField("Alliance", nation.getAlliance(), true)
                          .addField("Score", Double.toString(nation.getScore()), true)
                          .setFooter("Politics And War", "https://cdn.discordapp.com/attachments/392736524308840448/485867309995524096/57ad65f5467e958a079d2ee44a0e80ce.png")
                          .setTimestamp(Instant.now())
                          .build()
                  ).queue();
                }
              }
            }

            if (nation.getColor().equalsIgnoreCase("beige")) {
              if (politicsAndWar.getNation(nation.getNationId()).getBeigeTurnsLeft() == 1) {
                for (Long guildId : guildIds) {
                  Guild guild = db.getGuild(guildId);
                  if (guild.isVmBeigeTracker()) {
                    TextChannel channel = Bot.jda.getGuildById(guild.getId()).getTextChannelById(guild.getGuildChannels().getVmBeigeChannel());
                    channel.sendMessage(
                        new EmbedBuilder()
                            .setAuthor("https://politicsandwar.com/nation/id=" + nation.getNationId(), "https://politicsandwar.com/nation/id=" + nation.getNationId())
                            .setColor(new Color(Integer.valueOf("cfbb63", 16)))
                            .setTitle(nation.getNation() + " is leaving Beige next turn!")
                            .addField("Leader Name", nation.getLeader(), true)
                            .addField("Alliance", nation.getAlliance(), true)
                            .addField("Score", Double.toString(nation.getScore()), true)
                            .setFooter("Politics And War", "https://cdn.discordapp.com/attachments/392736524308840448/485867309995524096/57ad65f5467e958a079d2ee44a0e80ce.png")
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
