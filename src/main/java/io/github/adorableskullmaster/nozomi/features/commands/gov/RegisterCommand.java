package io.github.adorableskullmaster.nozomi.features.commands.gov;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.PlayerDataSource;
import io.github.adorableskullmaster.nozomi.core.database.models.Player;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Emojis;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.User;

import java.util.stream.Collectors;

public class RegisterCommand extends Command {

    public RegisterCommand() {
        this.name = "register";
        this.help = "Register a nation with a discord account";
        this.arguments = "++register [User Id] [Nation ID]";
        this.guildOnly = true;
        this.userPermissions = new Permission[]{Permission.MANAGE_ROLES};
        this.category = new Category("Gov");
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        try {
            String[] argParts = commandEvent.getArgs().trim().split(" ");
            long newPlayerId = Long.parseLong(argParts[0]);
            int newNationId = Integer.parseInt(argParts[1]);
            if (argParts.length == 2) {
                if (PlayerDataSource.isPlayerRegistered(newPlayerId)) {
                    Player playerData = PlayerDataSource.getPlayerData(newPlayerId);
                    if (playerData != null) {
                        User userById = commandEvent.getJDA().getUserById(playerData.getDiscordId());
                        String s = wrapPlayer(userById, playerData);
                        commandEvent.reply(Emojis.CANCEL.getAsMention() + " USER ID already registered with another NATION ID\n" + s);
                    }
                } else if (PlayerDataSource.isNationRegistered(newNationId)) {
                    Player playerData = PlayerDataSource.getPlayerData(newNationId);
                    if (playerData != null) {
                        User userById = commandEvent.getJDA().getUserById(playerData.getDiscordId());
                        String s = wrapPlayer(userById, playerData);
                        commandEvent.reply(Emojis.CANCEL.getAsMention() + " NATION ID already registered with another USER ID\n" + s);
                    }
                } else if (commandEvent.getGuild().getMemberById(newPlayerId) == null) {
                    commandEvent.reply(Emojis.CANCEL.getAsMention() + " Incorrect User ID. There is no server member with user id: `" + newPlayerId + "`");
                } else if (!Bot.CACHE.getNations()
                        .getNationsContainer()
                        .stream()
                        .map(SNationContainer::getNationId)
                        .collect(Collectors.toList()).contains(newNationId)) {
                    commandEvent.reply(Emojis.CANCEL.getAsMention() + " Can't find Nation with ID `" + newNationId + "`. If it exists in-game, try again after 30min.");
                } else {
                    SNationContainer nation = Bot.CACHE.getNations()
                            .getNationsContainer()
                            .stream()
                            .filter(sNationContainer -> sNationContainer.getNationId() == newNationId)
                            .collect(Collectors.toList())
                            .get(0);

                    Player player = new Player(newPlayerId, newNationId, nation.getAllianceposition() > 1, nation.getAllianceid());
                    PlayerDataSource.insertPlayerData(player);
                }
            } else {
                CommandResponseHandler.illegal(commandEvent, this.name);
            }
        } catch (Exception e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
        }
    }

    private String wrapPlayer(User user, Player player) {
        return String.format("**Member on Discord:** %s (%d) | **Nation ID Registered:** %d",
                user.getName(),
                player.getDiscordId(),
                player.getNationId());
    }
}
