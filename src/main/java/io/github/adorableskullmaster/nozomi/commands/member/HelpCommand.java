package io.github.adorableskullmaster.nozomi.commands.member;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.commands.BotCommand;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends BotCommand {

    public HelpCommand() {
        super();
        this.name = "help";
        this.aliases = new String[]{"commands", "command"};
        this.help = "Provides Help";
        this.arguments = "++help <commands name>";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
                String[] args = commandEvent.getArgs().trim().split(" ");
                if (args[0].isEmpty())
                    commandEvent.reply(getAll(commandEvent).build());
                else if (args.length == 1) {
                    commandEvent.reply(getByName(commandEvent, commandEvent.getArgs().trim()).build());
                } else
                    commandEvent.reply("Incorrect commands usage. Please use it like this: `++help ping` or just `++help`");
        } catch (Exception e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
        }
    }

    private EmbedBuilder getAll(CommandEvent commandEvent) {
        List<Command> commands = commandEvent.getClient().getCommands();
        final EmbedBuilder embed = new EmbedBuilder();

        embed.setColor(Color.CYAN)
                .setTitle("Help")
                .setDescription("Use ++help <commands> for more information");

        embed.addField("Bot Commands", commands.stream().map(Command::getName).collect(Collectors.joining(", ")), false);
        return embed;
    }

    private EmbedBuilder getByName(CommandEvent commandEvent, String commandQuery) {
        List<Command> commands = commandEvent.getClient().getCommands();
        final EmbedBuilder embed = new EmbedBuilder();

        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(commandQuery)) {
                embed.setTitle("Help: " + command.getName())
                        .setColor(Color.CYAN)
                        .addField("Displaying help for " + command.getName(), command.getHelp(), false)
                        .addField("What is the structure of the commands?", command.getArguments(), false)
                        .setFooter("Category: " + command.getCategory().getName(), null);
                if (command.getAliases().length > 0) {
                    embed.setDescription("Aliases: `" + String.join("`, `", command.getAliases()) + "`");
                }
                return embed;
            }
        }
        return embed.setDescription("Command not found.");
    }
}