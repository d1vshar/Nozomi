package io.github.adorableskullmaster.nozomi.features.commands.owner;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.OwnerCommand;
import net.dv8tion.jda.core.EmbedBuilder;

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

                    boolean nationsCache = Bot.getCACHE().getNations() != null;
                    boolean alliancesCache = Bot.getCACHE().getAlliances() != null;
                    boolean militaryCache = Bot.getCACHE().getNationMilitary() != null;
                    List<Boolean> booleanList = new ArrayList<>(Arrays.asList(nationsCache, alliancesCache, militaryCache));

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle("Bot Status")
                            .setColor(Utility.getGuildSpecificRoleColor(commandEvent))
                            .addField("Uptime", hrs + "h " + min + "m " + sec + "s", true)
                            .addField("WebSocket Ping", Long.toString(ping), true)
                            .addField("Current Memory", totalMemory + "MB", true)
                            .addField("Threads", Integer.toString(threadCount), true)
                            .addField("Cache State", Long.toString(booleanList.stream().filter(Boolean::booleanValue).count()), true)
                            .setFooter("Nozomi v" + getClass().getPackage().getImplementationVersion(), commandEvent.getSelfUser().getAvatarUrl())
                            .setTimestamp(Instant.now());

                    commandEvent.reply(embedBuilder.build());
                }
        );
    }

}
