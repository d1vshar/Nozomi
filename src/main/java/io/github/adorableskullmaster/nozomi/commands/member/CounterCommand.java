package io.github.adorableskullmaster.nozomi.commands.member;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.commands.BotCommand;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.domains.Nation;
import io.github.adorableskullmaster.pw4j.domains.NationMilitary;
import io.github.adorableskullmaster.pw4j.domains.subdomains.NationMilitaryContainer;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class CounterCommand extends BotCommand {

    private final PoliticsAndWar politicsAndWar;
    private CommandEvent commandEvent;

    public CounterCommand() {
        super();
        this.name = "counter";
        this.aliases = new String[]{"backup"};
        this.help = "Gives closest counter for the target nation";
        this.arguments = "++counter <Nation Link/Nation ID>";
        this.politicsAndWar = Instances.getDefaultPW();
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        this.commandEvent = commandEvent;
        try {
            commandEvent.async(() -> {
                commandEvent.getChannel().sendTyping().queue();
                if (!commandEvent.getArgs().trim().isEmpty() && commandEvent.getArgs().trim().split(" ").length != 1) {
                    CommandResponseHandler.illegal(commandEvent, name);
                } else {
                    String args = commandEvent.getArgs();
                    args = args.trim();
                    int id;
                    if (Utility.isNumber(args)) {
                        id = Integer.parseInt(args);
                        try {
                            commandEvent.reply(getMessage(id));
                        } catch (IOException e) {
                            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
                        }
                    } else if (Utility.isNumber(args.substring(args.lastIndexOf('=') + 1))) {
                        id = Integer.parseInt(args.substring(args.lastIndexOf('=') + 1));
                        try {
                            commandEvent.reply(getMessage(id));
                        } catch (IOException e) {
                            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
                        }
                    } else
                        CommandResponseHandler.illegal(commandEvent, name);
                }
            });
        } catch (Exception e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
        }
    }

    public Message getMessage(int targetId) throws IOException {
        MessageBuilder messageBuilder = new MessageBuilder();
        Nation targetNation = politicsAndWar.getNation(targetId);
        messageBuilder.setEmbed(getCounters(targetId, Bot.staticConfiguration.getPWId()).build());
        if (targetNation.getDefensivewars() >= 3)
            messageBuilder.appendFormat("**NOTE:** %s has no defensive slots available.", targetNation.getName());
        return messageBuilder.build();
    }

    private EmbedBuilder getCounters(int targetId, int aaId) {
        NationMilitaryContainer targetContainer = getNationFromMilitaryCache(targetId);
        double high = targetContainer.getScore() * 1.33333;
        double low = targetContainer.getScore() * 0.57143;

        double targetNSM = calculateNSM(targetContainer);
        LinkedHashMap<Integer, Double> top3 = getTop3Counters(calculateMembersNSM(getEveryoneEligible(aaId, high, low)));
        return createCounterEmbed(targetId, targetNSM, top3.entrySet());
    }

    private EmbedBuilder createCounterEmbed(int targetId, double targetNSM, Set<Map.Entry<Integer, Double>> top3) {
        SNationContainer targetContainer = getNationFromNationsCache(targetId);

        Color color;
        if (commandEvent == null)
            color = Color.CYAN;
        else
            color = Utility.getGuildSpecificRoleColor(commandEvent);

        EmbedBuilder embedBuilder = new EmbedBuilder().setColor(color)
                .setAuthor(targetContainer.getNation() + " - " + targetContainer.getScore() + " - " + String.format("%.2f", targetNSM),
                        "https://politicsandwar.com/nation/id=" + targetContainer.getNationId())
                .setTitle("Counter Finder")
                .setDescription("**_Nation Name - Score - Nozomi Strength Meter©_**")
                .setFooter("Politics And War", Utility.getPWIcon())
                .setTimestamp(Instant.now());

        int count = 1;
        for (Map.Entry<Integer, Double> entry : top3) {
            SNationContainer temp = getNationFromNationsCache(entry.getKey());
            String s = String.format("[%s](%s) - %.2f - %.2f", temp.getNation(), "https://politicsandwar.com/nation/id=" + temp.getNationId(), temp.getScore(), entry.getValue());
            embedBuilder.addField("Preference #" + count, s, false);
            count++;
        }
        return embedBuilder;
    }

    private LinkedHashMap<Integer, Double> getTop3Counters(HashMap<Integer, Double> memberMap) {
        LinkedHashMap<Integer, Double> insertLinkMap = new LinkedHashMap<>();
        LinkedHashMap<Integer, Double> sortByValue = Utility.sortByValue(memberMap, true);

        int i = 1;
        for (Map.Entry<Integer, Double> entry : sortByValue.entrySet()) {
            if (i == 4)
                break;
            insertLinkMap.put(entry.getKey(), entry.getValue());
            i++;
        }
        return insertLinkMap;
    }

    private HashMap<Integer, Double> calculateMembersNSM(List<NationMilitaryContainer> members) {
        HashMap<Integer, Double> result = new HashMap<>();
        for (NationMilitaryContainer member : members) {
            result.put(member.getNationId(), calculateNSM(member));
        }
        return result;
    }

    private double calculateNSM(NationMilitaryContainer container) {
        return (container.getSoldiers() + (container.getTanks() * 6) + (container.getAircraft() * 83.33) + (container.getShips() * 1000)) / 750;
    }

    private List<NationMilitaryContainer> getEveryoneEligible(int aaId, double high, double low) {
        NationMilitary nationMilitary = Bot.CACHE.getNationMilitary();
        return nationMilitary.getNationMilitaries()
                .stream()
                .filter(nationMilitaryContainer -> nationMilitaryContainer.getAllianceId() == aaId)
                .filter(container -> container.getScore() <= high && container.getScore() >= low)
                .filter(container -> container.getVmIndicator() == 0)
                .collect(Collectors.toList());
    }

    private NationMilitaryContainer getNationFromMilitaryCache(int id) {
        NationMilitary nationMilitary = Bot.CACHE.getNationMilitary();

        return nationMilitary.getNationMilitaries()
                .stream()
                .filter(nationMilitaryContainer -> nationMilitaryContainer.getNationId() == id)
                .collect(Collectors.toList())
                .get(0);
    }

    private SNationContainer getNationFromNationsCache(int id) {
        return Bot.CACHE.getNations()
                .getNationsContainer()
                .stream()
                .filter(nationContainer -> nationContainer.getNationId() == id)
                .collect(Collectors.toList())
                .get(0);
    }
}