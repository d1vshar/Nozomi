package io.github.adorableskullmaster.nozomi.features.commands.gov;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.PlayerDataSource;
import io.github.adorableskullmaster.nozomi.core.database.models.Player;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

import java.util.List;

public class WhoIsCommand extends Command {

    public WhoIsCommand() {
        this.name = "whois";
        this.help = "Lookup a member or nation in database";
        this.arguments = "++whois [@User1] <@User2> <@User3> ...";
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
                    if (PlayerDataSource.isPlayerRegistered(user.getIdLong())) {
                        commandEvent.reply(wrapPlayer(user, PlayerDataSource.getPlayerData(user.getIdLong())));
                    }
                }
            } else {
                CommandResponseHandler.illegal(commandEvent, this.name);
            }
        } catch (Exception e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
        }
    }

    private String wrapPlayer(User user, Player player) {
        if (player != null) {
            return String.format("**Member on Discord:** %s (%d) | **Nation ID Registered:** %d",
                    user.getName(),
                    user.getIdLong(),
                    player.getNationId());
        }
        return String.format("**Member on Discord:** %s (%d) | **Nation ID Registered:** Not Registered",
                user.getName(),
                user.getIdLong());
    }
}
