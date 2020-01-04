package io.github.adorableskullmaster.nozomi.hooks;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.PlayerDataSource;
import io.github.adorableskullmaster.nozomi.core.database.models.Player;
import io.github.adorableskullmaster.nozomi.logging.LogContext;
import io.github.adorableskullmaster.nozomi.logging.LogType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class LogListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getUser();
        if (PlayerDataSource.isPlayerRegistered(user.getIdLong())) {
            Player playerData = PlayerDataSource.getPlayerData(user.getIdLong());
            assert playerData != null;
            Bot.BOT_LOGGER.log(new LogContext(
                    LogType.ADD,
                    String.format("%s just re-joined the server!! He's registered with nation id %d.", event.getMember().getAsMention(), playerData.getNationId())
            ));
        }
    }

    @Override
    public void onGuildMemberLeave(@NotNull GuildMemberLeaveEvent event) {
        super.onGuildMemberLeave(event);
    }
}
