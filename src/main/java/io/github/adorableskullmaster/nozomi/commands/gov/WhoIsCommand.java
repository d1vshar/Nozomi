package io.github.adorableskullmaster.nozomi.commands.gov;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.PlayerDataSource;
import io.github.adorableskullmaster.nozomi.core.database.models.Player;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Emojis;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class WhoIsCommand extends Command {

    public WhoIsCommand() {
        this.name = "whois";
        this.help = "Lookup a member or nation in database";
        this.arguments = "++whois nation/discord [arguments]";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.category = new Category("Gov");
        this.children = new Command[]{new WhoIsDiscordCommand(), new WhoIsNationCommand()};
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reply("**Use in the following manner:**\n1. Finding nation: `++whois discord @Ping#1234`" +
                "\n2. Finding Discord ID: `++whois nation 123456`");
    }

    private String wrapPlayer(Player player) {
        if (player != null) {
            return String.format("**Member on Discord:** <@%d> | **Nation ID registered:** %d",
                    player.getDiscordId(),
                    player.getNationId());
        }
        return Emojis.CANCEL.getAsMention() + " Player not found in database.";
    }

    public class WhoIsNationCommand extends Command {

        WhoIsNationCommand() {
            this.name = "nation";
            this.help = "Find Discord ID of a nation";
            this.arguments = "++whois nation [Nation ID 1] <Nation ID 2> <Nation ID 3> ...";
            this.guildOnly = true;
            this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
            this.category = new Category("Gov");
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            try {
                String[] nationIds = commandEvent.getArgs().split(" ");
                if (nationIds.length > 0) {
                    for (String idStr : nationIds) {
                        try {
                            int id = Integer.parseInt(idStr);
                            commandEvent.reply(wrapPlayer(PlayerDataSource.getPlayerData(id)));
                        } catch (NumberFormatException e) {
                            CommandResponseHandler.error(commandEvent, Emojis.WARNING.getAsMention() + " Not a nation ID.");
                        }
                    }
                }
            } catch (Exception e) {
                Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
            }
        }
    }

    public class WhoIsDiscordCommand extends Command {

        WhoIsDiscordCommand() {
            this.name = "discord";
            this.help = "Find nation Id of a Discord member";
            this.arguments = "++whois discord [@User1] <@User2> <@User3> ...";
            this.guildOnly = true;
            this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
            this.category = new Category("Gov");
        }

        @Override
        protected void execute(CommandEvent commandEvent) {
            try {
                List<User> mentionedUsers = commandEvent.getMessage().getMentionedUsers();
                if (mentionedUsers.size() > 0) {
                    for (User user : mentionedUsers) {
                        commandEvent.reply(wrapPlayer(PlayerDataSource.getPlayerData(user.getIdLong())));
                    }
                } else {
                    CommandResponseHandler.illegal(commandEvent, this.name);
                }
            } catch (Exception e) {
                Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
            }
        }
    }
}
