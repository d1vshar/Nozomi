package io.github.adorableskullmaster.nozomi.features.commands.utility;

import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.JDAUtilitiesInfo;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.UtilityCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDAInfo;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AboutCommand extends UtilityCommand {

  public AboutCommand() {
    this.name = "about";
    this.aliases = new String[]{"bot"};
    this.help = "Information about the bot";
    this.arguments = "++about";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      List<String> dependencies = new ArrayList<>();
      dependencies.add("OpenJDK 8");
      dependencies.add("jda " + JDAInfo.VERSION);
      dependencies.add("jda-Utilities " + JDAUtilitiesInfo.VERSION);
      dependencies.add("PostgreSQL 10.5 (PostgreSQL Connector 42.2.1)");
      EmbedBuilder embed = new EmbedBuilder();
      embed.setTitle("Nozomi")
          .setThumbnail(commandEvent.getSelfUser().getAvatarUrl())
          .setDescription("A Java bot built for Politics And War. It has a wide array of stupid (and some useful) commands.")
          .addField("Version", getClass().getPackage().getImplementationVersion(), true)
          .addField("Author", commandEvent.getGuild().getMemberById(commandEvent.getClient().getOwnerId()).getAsMention(), true)
          .addField("Built using", String.join(", ", dependencies), false)
          .setColor(Utility.getGuildSpecificRoleColor(commandEvent))
          .setFooter("Uwu", commandEvent.getGuild().getIconUrl())
          .setTimestamp(Instant.now());
      commandEvent.reply(embed.build());
    } catch (Exception e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e, commandEvent);
    }
  }
}
