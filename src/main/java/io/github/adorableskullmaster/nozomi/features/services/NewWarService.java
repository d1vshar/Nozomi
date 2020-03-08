package io.github.adorableskullmaster.nozomi.features.services;

import com.mongodb.client.FindIterable;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.AllianceConfig;
import io.github.adorableskullmaster.nozomi.core.mongo.MongoBotConnection;
import io.github.adorableskullmaster.nozomi.core.mongo.TrackedWar;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.domains.War;
import io.github.adorableskullmaster.pw4j.domains.Wars;
import io.github.adorableskullmaster.pw4j.domains.subdomains.NationMilitaryContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SWarContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.WarContainer;
import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class NewWarService implements Runnable {

    private static PoliticsAndWar politicsAndWar = Instances.getDefaultPW();
    private MongoBotConnection mongoBotConnection;

    public NewWarService() {
        mongoBotConnection = Bot.getMongoBotConnection();
    }

    @Override
    public void run() {
        Bot.getLOGGER().info("Starting War Thread");

        List<War> newWars = getNewWars();
        AllianceConfig guild = mongoBotConnection.getAllianceConfigMongoCollection().find().first();

        Bot.getLOGGER().info("New Wars: {}", newWars.size());


        for (int i = newWars.size() - 1; i >= 0; i--) {

            WarContainer warObj = newWars.get(i).getWar().get(0);

            List<SNationContainer> nations = Bot.getCACHE().getNations().getNationsContainer();
            List<NationMilitaryContainer> nationMilitaries = Bot.getCACHE().getNationMilitary().getNationMilitaries();

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

            if (agg != null && def != null) {
                assert guild != null;
                assert aggMil != null;
                assert defMil != null;
                if (agg.getAllianceid() == guild.getPwId() || def.getAllianceid() == guild.getPwId()) {
                    EmbedBuilder embedBuilder = embed(warObj, agg, def, aggMil, defMil);
                    if (agg.getAllianceid() == guild.getPwId())
                        embedBuilder.setColor(Color.GREEN);
                    else
                        embedBuilder.setColor(Color.RED);

                    Bot.getJda().getGuildById(guild.getServerId())
                            .getTextChannelById(guild.getWarChannelId())
                            .sendMessage(embedBuilder.build())
                            .queue();
                }
            }
        }

    }

    private EmbedBuilder embed(WarContainer warObj, SNationContainer agg, SNationContainer def, NationMilitaryContainer aggMil, NationMilitaryContainer defMil) {
        String aggPos = agg.getAllianceposition() == 1 ? " Applicant" : "";
        String defPos = def.getAllianceposition() == 1 ? " Applicant" : "";
        return new EmbedBuilder().setTitle(String.format("%s%s on %s%s: " + warObj.getWarType(), agg.getAlliance(), aggPos, def.getAlliance(), defPos))
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
        FindIterable<TrackedWar> trackedWars = mongoBotConnection.getTrackedWarMongoCollection().find();
        for (TrackedWar trackedWar : trackedWars)
            result.add(trackedWar.getWarId());
        return result;
    }

    private void storeNewWars(List<Integer> newWars) {
        mongoBotConnection.getTrackedWarMongoCollection().drop();
        mongoBotConnection.getTrackedWarMongoCollection().insertMany(
                newWars.stream().map(TrackedWar::new).collect(Collectors.toList())
        );
    }
}
