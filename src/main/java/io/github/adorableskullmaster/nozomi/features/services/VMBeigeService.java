package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.AllianceConfig;
import io.github.adorableskullmaster.nozomi.core.mongo.MongoBotConnection;
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

public class VMBeigeService implements Runnable {

    private MongoBotConnection mongoBotConnection;

    public VMBeigeService() {
        this.mongoBotConnection = Bot.getMongoBotConnection();
    }

    @Override
    public void run() {
        Date date = Date.from(Instant.now());
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(date);
        if (cal.get(Calendar.HOUR) % 2 == 0) {
            if (cal.get(Calendar.MINUTE) == 5) {
                Bot.getLOGGER().info("Starting VM-Beige Thread");
                process();
            }
        }
    }

    private void process() throws PoliticsAndWarAPIException, NullPointerException {
        Nations nationsObj = Bot.getCACHE().getNations();

        AllianceConfig guild = mongoBotConnection.getAllianceConfigMongoCollection().find().first();

        if (nationsObj.isSuccess()) {
            List<SNationContainer> nations = nationsObj.getNationsContainer();
            for (SNationContainer nation : nations) {
                if (Integer.parseInt(nation.getVacmode()) == 1) {
                    assert guild != null;
                    Integer filterLow = guild.getVmScoreLow();
                    Integer filterHigh = guild.getVmScoreHigh();

                    if (nation.getScore() >= filterLow && nation.getScore() <= filterHigh) {
                        TextChannel channel = Bot.getJda().getGuildById(guild.getServerId())
                                .getTextChannelById(guild.getVmChannelId());
                        channel.sendMessage(
                                new EmbedBuilder()
                                        .setAuthor("https://politicsandwar.com/nation/id=" + nation.getNationId(),
                                                "https://politicsandwar.com/nation/id=" + nation.getNationId())
                                        .setColor(Color.WHITE)
                                        .setTitle(nation.getNation() + " is leaving VM next turn!")
                                        .addField("Leader Name", nation.getLeader(), true)
                                        .addField("Alliance", nation.getAlliance(), true)
                                        .addField("Score", Double.toString(nation.getScore()), true)
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
