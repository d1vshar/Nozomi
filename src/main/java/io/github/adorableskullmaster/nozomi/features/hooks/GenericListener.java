package io.github.adorableskullmaster.nozomi.features.hooks;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.sql.SQLException;

public class GenericListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild;
        try {
            guild = Instances.getDBLayer().getGuild(event.getGuild().getIdLong());
            if (guild.isJoinTexts()) {
                event.getJDA()
                        .getGuildById(guild.getId())
                        .getTextChannelById(guild.getGuildChannels().getMainChannel())
                        .sendMessage(getJoinEmbed(event, guild))
                        .queue();
            }
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        super.onGuildJoin(event);
    }

    private Message getJoinEmbed(GuildMemberJoinEvent event, Guild guild) {
        GuildTexts guildTexts = guild.getGuildTexts();
        MessageBuilder messageBuilder = new MessageBuilder();
        messageBuilder.setContent(String.format(event.getUser().getAsMention() + ", welcome to %s!", event.getGuild().getName()));
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Utility.getGuildSpecificRoleColor(event))
                .setAuthor(event.getGuild().getName(), "https://politicsandwar.com/alliance/id=" + guild.getPwId(), event.getGuild().getIconUrl())
                .setDescription(guildTexts.getJoinText());
        if (guildTexts.getJoinImage() != null)
            embedBuilder.setImage(guildTexts.getJoinImage());
        messageBuilder.setEmbed(embedBuilder.build());
        return messageBuilder.build();
    }
}
