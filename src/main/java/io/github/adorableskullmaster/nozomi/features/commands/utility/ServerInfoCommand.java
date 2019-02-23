package io.github.adorableskullmaster.nozomi.features.commands.utility;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.config.Config;
import io.github.adorableskullmaster.nozomi.features.commands.UtilityCommand;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.Alliance;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Member;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ServerInfoCommand extends UtilityCommand {

  public ServerInfoCommand() {
    this.name = "serverinfo";
    this.aliases = new String[]{"info"};
    this.help = "Information about the server";
    this.arguments = "++serverinfo";
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      commandEvent.getChannel().sendTyping().queue();
      List<Config.ConfigGuild> collect = Bot.config.getGuilds().stream().filter(guild -> guild.getDiscordId() == commandEvent.getGuild().getIdLong()).collect(Collectors.toList());
      Config.ConfigGuild guild = collect.get(0);
      int aid = guild.getPwId();

      PoliticsAndWar politicsAndWar = new PoliticsAndWarBuilder().build();
      Alliance alliance = politicsAndWar.getAlliance(aid);
      int amembers = alliance.getMembers();

      int online = 0;
      for (Member member : commandEvent.getGuild().getMembers()) {
        if (member.getOnlineStatus() == OnlineStatus.ONLINE || member.getOnlineStatus() == OnlineStatus.DO_NOT_DISTURB || member.getOnlineStatus() == OnlineStatus.IDLE)
          online++;
      }

      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setTitle(commandEvent.getGuild().getName())
          .setColor(Color.BLACK)
          .setAuthor("https://politicsandwar.com/alliance/id="+aid, "https://politicsandwar.com/alliance/id="+aid)
          .setThumbnail(commandEvent.getSelfUser().getAvatarUrl())
          .addField("Discord Users", online + "/" + commandEvent.getGuild().getMembers().size(), true)
          .addField("In-game Members", Integer.toString(amembers), true)
          .addField("Owner",
              commandEvent.getGuild().getOwner().getUser().getName() + "#" + commandEvent.getGuild().getOwner().getUser().getDiscriminator(), true)
          .addField("Score", alliance.getScore(), true)
          .setFooter("Guild Creation Date: " + commandEvent.getGuild().getCreationTime().format(DateTimeFormatter.RFC_1123_DATE_TIME),
              commandEvent.getGuild().getIconUrl())
          .build();
      commandEvent.reply(embedBuilder.build());
    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e, commandEvent);
    }
  }
}
