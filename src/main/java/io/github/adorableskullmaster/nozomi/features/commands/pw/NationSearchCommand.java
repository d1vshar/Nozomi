package io.github.adorableskullmaster.nozomi.features.commands.pw;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.OrderedMenu;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.PoliticsAndWarCommand;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.Nations;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NationSearchCommand extends PoliticsAndWarCommand {

  private final EventWaiter waiter;

  public NationSearchCommand(EventWaiter waiter) {
    this.waiter = waiter;
    this.name = "nation";
    this.help = "Search an nation by name or id.";
    this.arguments = "++nation [Nation ID/Nation Name/Leader Name]";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      if (!commandEvent.getArgs().trim().isEmpty()) {
        ArrayList<SNationContainer> result = fuzzySearch(commandEvent.getArgs().trim());
        if (result.size() > 0) {
          if (result.size() == 1) {
            commandEvent.reply(embed(result.get(0)));
          } else if (result.size() < 10) {
            OrderedMenu.Builder menu = new OrderedMenu.Builder()
                .setColor(Color.CYAN)
                .setEventWaiter(waiter)
                .setTimeout(30, TimeUnit.SECONDS)
                .allowTextInput(true)
                .setDescription("Choose one of the following:")
                .useCancelButton(true)
                .setCancel(message -> message.delete().queue())
                .setSelection((message, integer) -> commandEvent.reply(embed(result.get(integer - 1))));
            for (SNationContainer container : result) {
              menu.addChoice(String.format("**%s** - **%s** | **%s**", container.getNation(), container.getLeader(), container.getAlliance()));
            }
            menu.build().display(commandEvent.getChannel());
          } else {
            CommandResponseHandler.error(commandEvent, "Too many results! Be more specific.");
          }
        } else {
          CommandResponseHandler.error(commandEvent, "No such nation found.");
        }

      } else {
        CommandResponseHandler.illegal(commandEvent, name);
      }

    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e, commandEvent);
    }
  }

  private MessageEmbed embed(SNationContainer nation) {
    EmbedBuilder embed = new EmbedBuilder();
    embed.setTitle(nation.getNation())
        .setColor(Color.CYAN)
        .setAuthor("https://politicsandwar.com/nation/id=" + nation.getNationId(), "https://politicsandwar.com/nation/id=" + nation.getNationId())
        .addField("Leader Name", nation.getLeader(), true)
        .addField("Color", nation.getColor().toUpperCase(), true)
        .addField("Cities", Integer.toString(nation.getCities()), true)
        .addField("Rank", Integer.toString(nation.getRank()), true)
        .addField("Score", Double.toString(nation.getScore()), true)
        .addField("Alliance", nation.getAlliance(), true)
        .setFooter("Politics And War", "https://cdn.discordapp.com/attachments/392736524308840448/485867309995524096/57ad65f5467e958a079d2ee44a0e80ce.png")
        .setTimestamp(Instant.now());
    return embed.build();
  }

  private ArrayList<SNationContainer> fuzzySearch(String query) {

    List<SNationContainer> nationsContainer = Bot.cacheManager.getNations().getNationsContainer();

    HashMap<Integer, String> nameHashMap = new HashMap<>();
    HashMap<Integer, String> lNameHashMap = new HashMap<>();

    for (SNationContainer container : nationsContainer) {
      nameHashMap.put(container.getNationId(), container.getNation());
      lNameHashMap.put(container.getNationId(), container.getLeader());
    }

    List<Integer> nameResult = Utility.stringSearch(query, nameHashMap, 85);
    List<Integer> lNameResult = Utility.stringSearch(query, lNameHashMap, 85);

    return (ArrayList<SNationContainer>) nationsContainer.stream()
        .filter(nationContainer -> nameResult.contains(nationContainer.getNationId()) || lNameResult.contains(nationContainer.getNationId()))
        .distinct()
        .collect(Collectors.toList());
  }

  @SuppressWarnings("unused")
  @Deprecated
  private List<SNationContainer> search(String args) {
    List<String> commons = new ArrayList<>(Arrays.asList("the", "is", "a", "an", "of", "some", "few"));
    List<SNationContainer> result = new ArrayList<>();
    Nations nations = Bot.cacheManager.getNations();
    if (nations == null)
      nations = new PoliticsAndWarBuilder()
          .setApiKey(Bot.config.getCredentials().getMasterPWKey())
          .build()
          .getNations(true);
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
