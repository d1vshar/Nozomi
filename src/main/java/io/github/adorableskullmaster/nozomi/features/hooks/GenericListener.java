package io.github.adorableskullmaster.nozomi.features.hooks;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.layer.GuildSettings;
import io.github.adorableskullmaster.nozomi.core.database.layer.tables.TextModule;
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
    GuildSettings guildSettings;
    try {
      guildSettings = Instances.getBotDatabaseLayer().getGuildSettings(event.getGuild().getIdLong());
      if (guildSettings.getModuleSettings().isTextModuleEnabled()) {
        TextModule textModuleSettings = guildSettings.getTextModuleSettings();
        if(textModuleSettings.getJoin()!=null || textModuleSettings.getJoinImage()!=null)
        event.getGuild()
            .getTextChannelById(textModuleSettings.getId())
            .sendMessage(getJoinEmbed(event, guildSettings))
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

  private Message getJoinEmbed(GuildMemberJoinEvent event, GuildSettings guildSettings) {
    TextModule textModuleSettings = guildSettings.getTextModuleSettings();

    MessageBuilder messageBuilder = new MessageBuilder();
    messageBuilder.setContent(String.format(event.getUser().getAsMention() + ", welcome to %s!", event.getGuild().getName()));

    if(textModuleSettings.getJoinImage()!=null || textModuleSettings.getJoin()!=null) {
      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setColor(Utility.getGuildSpecificRoleColor(event))
        .setAuthor(event.getGuild().getName(), "https://politicsandwar.com/alliance/id=" + guildSettings.getModuleSettings().getAaId(),
            event.getGuild().getIconUrl());
      if(textModuleSettings.getJoin()!=null)
        embedBuilder.setDescription(textModuleSettings.getJoin());
      if(textModuleSettings.getJoinImage()!=null)
        embedBuilder.setImage(textModuleSettings.getJoinImage());
      messageBuilder.setEmbed(embedBuilder.build());
    }
    return messageBuilder.build();
  }
}
