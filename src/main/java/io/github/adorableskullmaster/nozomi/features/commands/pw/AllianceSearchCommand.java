package io.github.adorableskullmaster.nozomi.features.commands.pw;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.PoliticsAndWarCommand;
import io.github.adorableskullmaster.pw4j.domains.Alliances;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SAllianceContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AllianceSearchCommand extends PoliticsAndWarCommand {

  private final EventWaiter waiter;
  private CommandEvent commandEvent;

  public AllianceSearchCommand(EventWaiter waiter) {
    this.waiter = waiter;
    this.name = "alliance";
    this.help = "Search an alliance by name or id.";
    this.arguments = "++alliance [Alliance ID/Alliance Name/Alliance Acronym]";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    this.commandEvent = commandEvent;
    try {
      commandEvent.async(() -> {
        if (!commandEvent.getArgs().trim().isEmpty()) {
          commandEvent.getChannel().sendTyping().queue();
          List<SAllianceContainer> results = search(commandEvent.getArgs());
          List<String> names = results
              .stream()
              .map(SAllianceContainer::getName)
              .collect(Collectors.toList());
          if (results.size() > 0) {
            if (results.size() == 1) {
              commandEvent.reply(embed(results.get(0)));
            } else {
              commandEvent.getChannel().sendMessage("**Choose one of the following:**\n" + String.join(", ", names)).queue(
                  (c) -> waiter.waitForEvent(
                      MessageReceivedEvent.class,
                      (event) -> event.getAuthor().equals(commandEvent.getAuthor()) && event.getChannel().equals(commandEvent.getChannel()),
                      (event) -> {
                        if (names.contains(event.getMessage().getContentDisplay()))
                          commandEvent.getChannel().sendMessage(embed(results.get(names.indexOf(event.getMessage().getContentDisplay())))).queue();
                      },
                      30,
                      TimeUnit.SECONDS,
                      () -> CommandResponseHandler.timeout(commandEvent)
                  )
              );
            }
          } else {
            CommandResponseHandler.error(commandEvent, "No such alliance found.");
          }
        } else {
          CommandResponseHandler.error(commandEvent, name);
        }
      });
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }

  private MessageEmbed embed(SAllianceContainer alliance) {
    EmbedBuilder embed = new EmbedBuilder();
    embed.setTitle(alliance.getName() + (alliance.getAcronym().isEmpty() ? "(" + alliance.getAcronym() + ")" : ""))
        .setColor(Utility.getGuildSpecificRoleColor(commandEvent))
        .setAuthor("https://politicsandwar.com/alliance/id=" + alliance.getId(), "https://politicsandwar.com/alliance/id=" + alliance.getId())
        .addField("Color", alliance.getColor().toUpperCase(), false)
        .addField("Members", Integer.toString(alliance.getMembers()), false)
        .addField("Rank", Integer.toString(alliance.getRank()), false)
        .addField("Score", Double.toString(alliance.getScore()), false)
        .addField("Average Score", Double.toString(alliance.getAvgscore()), false)
        .addField("Discord Invite Link", alliance.getIrcchan(), false)
        .setFooter("Politics And War", Utility.getPWIcon())
        .setTimestamp(Instant.now())
        .setImage(alliance.getFlagurl());
    return embed.build();
  }

  private List<SAllianceContainer> search(String arg) {
    List<String> commons = new ArrayList<>(Arrays.asList("the", "is", "a", "an", "of", "some", "few"));
    List<SAllianceContainer> result = new ArrayList<>();

    Alliances alliances = Bot.CACHE.getAlliances();
    if (alliances == null)
      alliances = Instances.getDefaultPW().getAlliances();
    List<SAllianceContainer> allianceList = alliances.getAlliances();

    if (Utility.isNumber(arg)) {
      for (SAllianceContainer alliance : allianceList) {
        if (alliance.getId() != null && alliance.getId().equals(arg)) {
          result.add(alliance);
          break;
        }
      }
    } else {
      List<SAllianceContainer> nameSort = allianceList.parallelStream()
          .filter(container -> container != null && ((container.getAcronym() != null && container.getAcronym().equalsIgnoreCase(arg))
              || (container.getName() != null && container.getName().equalsIgnoreCase(arg))))
          .collect(Collectors.toList());
      if (nameSort.size() == 1)
        result.add(nameSort.get(0));
      else {
        List<String> argParts =
            Arrays.stream(arg.split(" ")).map(String::toLowerCase).collect(Collectors.toList()).stream().map(String::trim).collect(Collectors.toList());
        argParts.removeIf(commons::contains);
        for (SAllianceContainer alliance : allianceList) {
          if (alliance != null && alliance.getName() != null) {
            for (String part : argParts) {
              if (alliance.getName().toLowerCase().contains(part))
                result.add(alliance);
            }
          }
        }
      }
    }
    return result;
  }
}