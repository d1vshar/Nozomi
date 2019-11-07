package io.github.adorableskullmaster.nozomi.features.commands.utility;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.MemberPoliticsAndWarCommand;
import io.github.adorableskullmaster.nozomi.features.commands.PoliticsAndWarCommand;
import io.github.adorableskullmaster.nozomi.features.commands.UtilityCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Role;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand extends UtilityCommand {

    private boolean mod, member;

    public HelpCommand() {
        this.name = "help";
        this.aliases = new String[]{"commands", "command"};
        this.help = "Provides Help";
        this.arguments = "++help <commands name>";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            Guild guild = Instances.getDBLayer().getGuild(commandEvent.getGuild().getIdLong());
            if (guild != null) {
                mod = false;
                member = false;
                if (commandEvent.getMember().hasPermission(Utility.getModerator()))
                    mod = true;
                if (commandEvent.getMember().getRoles().stream().map(Role::getIdLong).anyMatch(id -> id == guild.getMemberRole()))
                    member = true;
                String[] args = commandEvent.getArgs().trim().split(" ");
                if (args[0].isEmpty())
                    commandEvent.reply(getAll(commandEvent).build());
                else if (args.length == 1) {
                    commandEvent.reply(getByName(commandEvent, commandEvent.getArgs().trim()).build());
                } else
                    commandEvent.reply("Incorrect commands usage. Please use it like this: `++help ping` or just `++help`");
            }
        } catch (Exception e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
        }
    }

    private EmbedBuilder getAll(CommandEvent commandEvent) {
        List<Command> commands = commandEvent.getClient().getCommands();
        final EmbedBuilder embed = new EmbedBuilder();

        List<Command> pwCommands = new ArrayList<>();
        List<Command> protectedCommands = new ArrayList<>();
        List<Command> funCommands = new ArrayList<>();
        List<Command> utilityCommands = new ArrayList<>();

        for (Command command : commands) {
            if (command instanceof FunCommand)
                funCommands.add(command);
            else if (command instanceof MemberPoliticsAndWarCommand)
                protectedCommands.add(command);
            else if (command instanceof PoliticsAndWarCommand)
                pwCommands.add(command);
            else if (command instanceof UtilityCommand)
                utilityCommands.add(command);
        }

        embed.setColor(Color.CYAN)
                .setTitle("Help")
                .setDescription("Use ++help <commands> for more information");
        if (member) {
            pwCommands.addAll(protectedCommands);
        }
        embed.addField("Politics And War Commands", pwCommands.stream().map(Command::getName).collect(Collectors.joining(", ")), false)
                .addField("Fun Commands", funCommands.stream().map(Command::getName).collect(Collectors.joining(", ")), false)
                .addField("Utility Commands", utilityCommands.stream().map(Command::getName).collect(Collectors.joining(", ")), false);

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