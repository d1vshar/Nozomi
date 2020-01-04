package io.github.adorableskullmaster.nozomi.hooks;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.ConfigurationDataSource;
import io.github.adorableskullmaster.nozomi.core.database.models.Configuration;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Long mainChannel = ConfigurationDataSource.getConfiguration().getMainChannel();
        Objects.requireNonNull(event.getJDA()
                .getTextChannelById(mainChannel))
                        .sendMessage(getJoinEmbed(event))
                        .queue();
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        super.onGuildJoin(event);
    }

    private Message getJoinEmbed(GuildMemberJoinEvent event) {
        Configuration configuration = ConfigurationDataSource.getConfiguration();

        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setContent(String.format(event.getUser().getAsMention() + ", welcome to %s!", event.getGuild().getName()));
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Utility.getGuildSpecificRoleColor(event))
                .setAuthor(event.getGuild().getName(), "https://politicsandwar.com/alliance/id=" + Bot.staticConfiguration.getPWId(), event.getGuild().getIconUrl())
                .setDescription(configuration.getJoinText());
        messageBuilder.setEmbed(embedBuilder.build());
        return messageBuilder.build();
    }
}
