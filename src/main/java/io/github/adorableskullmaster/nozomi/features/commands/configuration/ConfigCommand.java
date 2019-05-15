package io.github.adorableskullmaster.nozomi.features.commands.configuration;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.core.database.layer.BotDatabase;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.ConfigurationCommand;
import net.dv8tion.jda.core.EmbedBuilder;

import java.sql.SQLException;
import java.time.Instant;

public class ConfigCommand extends ConfigurationCommand {

  public ConfigCommand() {
    super();
    this.name = "config";
    this.aliases = new String[]{"setting","settings"};
    this.arguments = "++settings [module]";
    this.children = new Command[]{};
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    commandEvent.async(()-> {
      try {
        BotDatabase db = new BotDatabase();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Current Server Configuration")
            .setColor(Utility.getGuildSpecificRoleColor(commandEvent))
            .setAuthor(commandEvent.getGuild().getName(),null,commandEvent.getGuild().getIconUrl())
            .addField("Bot","",true)
            .addField("Texts Module","",true)
            .addField("War Module","",true)
            .addField("VacMode Module","",true)
            .addField("Applicants Module","",true)
            .addField("Bank Module","",true)
            .setFooter("Politics And War",Utility.getPWIcon())
            .setTimestamp(Instant.now());
      } catch (SQLException e) {
        e.printStackTrace();
      }
    });
  }


}
