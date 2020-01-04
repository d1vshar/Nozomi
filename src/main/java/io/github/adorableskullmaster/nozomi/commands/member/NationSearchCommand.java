package io.github.adorableskullmaster.nozomi.commands.member;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.commands.BotCommand;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.pw4j.domains.Nations;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NationSearchCommand extends BotCommand {

    private final EventWaiter waiter;
    private CommandEvent commandEvent;

    public NationSearchCommand(EventWaiter waiter) {
        super();
        this.waiter = waiter;
        this.name = "nation";
        this.help = "Search an nation by name or id.";
        this.arguments = "++nation [Nation ID/Nation Name/Leader Name]";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        this.commandEvent = commandEvent;
        try {
            if (!commandEvent.getArgs().trim().isEmpty()) {
                List<SNationContainer> result = search(commandEvent.getArgs().trim());
                if (result.size() > 0) {
                    if (result.size() == 1)
                        commandEvent.reply(embed(result.get(0)));
                    else if (result.size() < 10) {
                        OrderedMenu.Builder orderedMenu = new OrderedMenu.Builder();
                        orderedMenu.setColor(Utility.getGuildSpecificRoleColor(commandEvent))
                                .setEventWaiter(waiter)
                                .setTimeout(30, TimeUnit.SECONDS)
                                .setDescription("Choose one of the following: ")
                                .setSelection((message, integer) -> commandEvent.reply(embed(result.get(integer - 1))))
                                .useCancelButton(true);
                        for (SNationContainer container : result) {
                            orderedMenu.addChoice("**" + container.getNationId() + "**" + " | " + container.getNation() + " - " + container.getLeader());
                        }
                        orderedMenu.build().display(commandEvent.getChannel());
                    } else {
                        CommandResponseHandler.error(commandEvent, "Too many results. Be more specific.");
                    }
                } else {
                    CommandResponseHandler.error(commandEvent, "No such nation found.");
                }

            } else {
                CommandResponseHandler.illegal(commandEvent, name);
            }

        } catch (Exception e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
        }
    }

    private MessageEmbed embed(SNationContainer nation) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(nation.getNation())
                .setColor(Utility.getGuildSpecificRoleColor(commandEvent))
                .setAuthor("https://politicsandwar.com/nation/id=" + nation.getNationId(), "https://politicsandwar.com/nation/id=" + nation.getNationId())
                .addField("Leader Name", nation.getLeader(), true)
                .addField("Color", nation.getColor().toUpperCase(), true)
                .addField("Cities", Integer.toString(nation.getCities()), true)
                .addField("Rank", Integer.toString(nation.getRank()), true)
                .addField("Score", Double.toString(nation.getScore()), true)
                .addField("Alliance", nation.getAlliance(), true)
                .setFooter("Politics And War", Utility.getPWIcon())
                .setTimestamp(Instant.now());
        return embed.build();
    }

    private List<SNationContainer> search(String args) throws IOException {
        List<String> commons = new ArrayList<>(Arrays.asList("the", "is", "a", "an", "of", "some", "few"));
        List<SNationContainer> result = new ArrayList<>();
        Nations nations = Bot.CACHE.getNations();
        if (nations == null)
            nations = Instances.getDefaultPW().getNations(true);
        List<SNationContainer> containers = nations.getNationsContainer();
        if (Utility.isNumber(args)) {
            int nid = Integer.parseInt(args);
            List<SNationContainer> filtered = containers.parallelStream().filter(sNationContainer -> sNationContainer.getNationId() == nid).collect(Collectors.toList());
            if (filtered.size() == 1) {
                result.add(filtered.get(0));
            }
        } else {
            List<SNationContainer> nameFilter = containers.parallelStream()
                    .filter(sNationContainer -> sNationContainer.getNation().equalsIgnoreCase(args) || sNationContainer.getLeader().equalsIgnoreCase(args))
                    .collect(Collectors.toList());
            if (nameFilter.size() == 1) {
                result.add(nameFilter.get(0));
            } else {
                List<String> argParts =
                        Arrays.stream(args.split(" ")).map(String::toLowerCase).collect(Collectors.toList()).stream().map(String::trim).collect(Collectors.toList());
                argParts.removeIf(commons::contains);
                for (SNationContainer container : containers) {
                    String nationName = container.getNation().toLowerCase().trim();
                    String leaderName = container.getLeader().toLowerCase().trim();

                    for (String part : argParts) {
                        if (nationName.contains(part) || leaderName.contains(part)) {
                            result.add(container);
                        }
                    }
                }
            }
        }
        return result;
    }
}
