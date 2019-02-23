package io.github.adorableskullmaster.nozomi.features.hooks;

import io.github.adorableskullmaster.nozomi.core.config.Config;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;

public class GenericListener extends ListenerAdapter {

  @Override
  public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    Config.ConfigGuild guild = AuthUtility.getGuildConfig(event.getGuild().getIdLong());
    if(guild!=null && guild.getTexts()!=null) {
      event.getJDA()
          .getGuildById(guild.getDiscordId())
          .getTextChannelById(guild.getMainChannel())
          .sendMessage(getJoinEmbed(event, guild))
          .queue();
    }
  }

  private Message getJoinEmbed(GuildMemberJoinEvent event, Config.ConfigGuild guild) {
    MessageBuilder messageBuilder = new MessageBuilder();
    messageBuilder.setContent(String.format(event.getUser().getAsMention() + ", welcome to %s!",event.getGuild().getName()));
    EmbedBuilder embedBuilder = new EmbedBuilder();
    embedBuilder.setColor(Color.CYAN)
        .setAuthor(event.getGuild().getName(), "https://politicsandwar.com/alliance/id="+guild.getPwId(), event.getGuild().getIconUrl())
        .setDescription(guild.getTexts().getJoin());
    if(guild.getTexts().getJoinImg()!=null)
      embedBuilder.setImage(guild.getTexts().getJoinImg());
    messageBuilder.setEmbed(embedBuilder.build());
    return messageBuilder.build();
  }
}
