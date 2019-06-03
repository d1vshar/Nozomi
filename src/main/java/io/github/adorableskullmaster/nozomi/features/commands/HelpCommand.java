package io.github.adorableskullmaster.nozomi.features.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Emojis;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

  public HelpCommand() {
    this.name = "help";
    this.aliases = new String[]{"commands", "command"};
    this.help = "Provides Help";
    this.arguments = "++help <commands name>";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      String[] args = commandEvent.getArgs().trim().split(" ");
      if(commandEvent.getArgs().trim().isEmpty())
        commandEvent.replyInDm(getAll(commandEvent),s -> success(commandEvent),f -> fail(commandEvent));
      else if(args.length==1)
        commandEvent.replyInDm(getByName(commandEvent,args[0]),s -> success(commandEvent),f -> fail(commandEvent));
      else
        commandEvent.replyFormatted(
            Emojis.CANCEL.getAsMention() + " Incorrect commands usage. Please use it like this: `%shelp ping` or just `%shelp`",
            commandEvent.getClient().getPrefix(),
            commandEvent.getClient().getPrefix()
        );
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }

  private void success(CommandEvent commandEvent) {
    commandEvent.reply(Emojis.LETTER_CHECK.getAsMention() + " Sent you help in DMs.");
  }

  private void fail(CommandEvent commandEvent) {
    commandEvent.reply(Emojis.CANCEL.getAsMention() + " Couldn't send you help in DMs.");
  }

  private Message getAll(CommandEvent commandEvent) {
    List<String> categories = fetchAllCategories(commandEvent);
    categories.sort(String::compareToIgnoreCase);

    final MessageBuilder messageBuilder = new MessageBuilder();

    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setTitle("Help")
        .setDescription(String.format("Use %shelp <commands> for more information",commandEvent.getClient().getPrefix()))
        .setColor(Utility.getGuildSpecificRoleColor(commandEvent));
    for(String category : categories) {
      embedBuilder.addField(category, String.join(", ", findAllCommandsByCategory(commandEvent, category)),false);
    }
    return messageBuilder.setEmbed(embedBuilder.build()).build();
  }

  private Message getByName(CommandEvent commandEvent, String commandQuery) {
    final MessageBuilder messageBuilder = new MessageBuilder();
    final EmbedBuilder embed = new EmbedBuilder();

    CommandDescriptor command = findCommand(commandEvent, commandQuery);
    if(command==null)
      return messageBuilder.setContent(Emojis.CANCEL.getAsMention() + " Command not found.").build();

    embed.setTitle("Help: " + command.name)
        .setColor(Color.CYAN)
        .addField("Displaying help for " + command.name, command.description, false)
        .addField("What is the structure of the commands?", command.arguments, false)
        .setFooter("Category: " + command.category, null);

    if (command.aliases.length > 0)
      embed.setDescription("Aliases: `" + String.join("`, `", command.aliases) + "`");
    return messageBuilder.setEmbed(embed.build()).build();
  }

  private CommandDescriptor findCommand(CommandEvent commandEvent, String name) {
    return commandEvent.getClient()
        .getCommands()
        .stream()
        .filter(command -> command.getName().equals(name) || Arrays.stream(command.getAliases()).anyMatch(s -> s.equalsIgnoreCase(name)))
        .map(command -> {
          String category = "None";
          if(command.getCategory()!=null)
            category = command.getCategory().getName();
          return new CommandDescriptor(command.getName(),command.getAliases(),command.getHelp(),command.getArguments(),category);
        })
        .findFirst()
        .orElse(null);
  }

  private List<String> findAllCommandsByCategory(CommandEvent commandEvent, String name) {
    return commandEvent.getClient()
        .getCommands()
        .stream()
        .filter(command -> {
          if(command.getCategory()!=null)
            return command.getCategory().getName().equalsIgnoreCase(name);
          return false;
        })
        .map(Command::getName)
        .collect(Collectors.toList());
  }

  private List<String> fetchAllCategories(CommandEvent commandEvent) {
    List<String> categories = new ArrayList<>();
    commandEvent.getClient()
        .getCommands()
        .forEach(
            command -> {
              if(command.getCategory()!=null && !categories.contains(command.getCategory().getName()))
                categories.add(command.getCategory().getName());
            }
        );
    return categories;
  }

  private class CommandDescriptor {
    String name;
    String[] aliases;
    String description;
    String arguments;
    String category;

    CommandDescriptor(String name, String[] aliases, String description, String arguments, String category) {
      this.name = name;
      this.aliases = aliases;
      this.description = description;
      this.arguments = arguments;
      this.category = category;
    }
  }
}