package io.github.adorableskullmaster.nozomi.features.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.features.commands.OwnerCommand;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatusCommand extends OwnerCommand {
  private final Instant startup;

  public StatusCommand() {
    startup = Instant.now();
    this.name = "status";
    this.help = "Status of bot";
    this.arguments = "++uptime";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      commandEvent.async(
          () -> {
            Instant now = Instant.now();
            Duration uptime = Duration.between(startup, now);
            long hrs = uptime.getSeconds() / 3600;
            long min = uptime.getSeconds() / 60 - hrs * 60;
            long sec = uptime.getSeconds() - (min * 60 + hrs * 3600);

            long totalMemory = Runtime.getRuntime().totalMemory() / 1000000;
            int threadCount = Thread.activeCount();
            long ping = commandEvent.getJDA().getPing();

            boolean nationsCache = Bot.cacheManager.getNations() != null;
            boolean alliancesCache = Bot.cacheManager.getAlliance() != null;
            boolean militaryCache = Bot.cacheManager.getNationMilitary() != null;
            boolean citiesCache = Bot.cacheManager.getAllCities() != null;
            List<Boolean> booleanList = new ArrayList<>(Arrays.asList(nationsCache, alliancesCache, militaryCache, citiesCache));

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Bot Status")
                .setColor(Color.CYAN)
                .addField("Uptime", hrs + "h " + min + "m " + sec + "s", true)
                .addField("WebSocket Ping", Long.toString(ping), true)
                .addField("Current Memory", totalMemory + "MB", true)
                .addField("Threads", Integer.toString(threadCount), true)
                .addField("Cache State", Long.toString(booleanList.stream().filter(Boolean::booleanValue).count()), true)
                .setFooter("Nozomi v" + getClass().getPackage().getImplementationVersion() + (Bot.dev ? " beta" : ""),
                    commandEvent.getSelfUser().getAvatarUrl())
                .setTimestamp(Instant.now());

            commandEvent.reply(embedBuilder.build());
          }
      );
    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e, commandEvent);
    }
  }

}
