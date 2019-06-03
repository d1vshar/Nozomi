package io.github.adorableskullmaster.nozomi.features.commands.configuration;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.layer.GuildSettings;
import io.github.adorableskullmaster.nozomi.core.database.layer.tables.ModuleSettings;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.Utility;
import io.github.adorableskullmaster.nozomi.features.commands.configuration.modules.BotActivateCommand;
import io.github.adorableskullmaster.nozomi.features.commands.types.ConfigurationCommand;
import net.dv8tion.jda.core.EmbedBuilder;

import java.sql.SQLException;
import java.time.Instant;

public class ConfigCommand extends ConfigurationCommand {

  public ConfigCommand(EventWaiter eventWaiter) {
    this.name = "config";
    this.aliases = new String[]{"setting", "settings"};
    this.arguments = "++settings [module]";
    this.children = new Command[] {new BotActivateCommand(eventWaiter)};
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      String[] config = fetchModuleConfig(commandEvent.getGuild().getIdLong());

      EmbedBuilder embedBuilder = new EmbedBuilder();
      embedBuilder.setTitle("Current Server Configuration")
          .setColor(Utility.getGuildSpecificRoleColor(commandEvent))
          .setAuthor(commandEvent.getGuild().getName(), null, commandEvent.getGuild().getIconUrl())
          .addField("Bot", config[0], true)
          .addField("Texts Module", config[1], true)
          .addField("War Module", config[2], true)
          .addField("VacMode Module", config[3], true)
          .addField("Applicants Module", config[4], true)
          .addField("Bank Module", config[5], true)
          .setFooter("Politics And War", Utility.getPWIcon())
          .setTimestamp(Instant.now());

      commandEvent.reply(embedBuilder.build());
    } catch (SQLException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  private String[] fetchModuleConfig(long id) throws SQLException {
    String[] config = new String[]{"Inactivated","Disabled","Disabled","Disabled","Disabled","Disabled"};

    GuildSettings guildSettings = Instances.getBotDatabaseLayer().getGuildSettings(id);
    if (guildSettings == null)
      return config;
    else {
      ModuleSettings moduleSettings = guildSettings.getModuleSettings();
      System.out.println(moduleSettings.isBotActivated());

      if (moduleSettings.isBotActivated() != null && moduleSettings.isBotActivated())
        config[0] = "Activated";
      if (moduleSettings.isTextModuleEnabled() != null && moduleSettings.isTextModuleEnabled())
        config[1] = "Enabled";
      if (moduleSettings.isWarModuleEnabled() != null && moduleSettings.isWarModuleEnabled())
        config[2] = "Enabled";
      if (moduleSettings.isVacModeModuleEnabled() != null && moduleSettings.isVacModeModuleEnabled())
        config[3] = "Enabled";
      if (moduleSettings.isApplicantModuleEnabled() != null && moduleSettings.isApplicantModuleEnabled())
        config[4] = "Enabled";
      if (moduleSettings.isBankModuleEnabled() != null && moduleSettings.isBankModuleEnabled())
        config[5] = "Enabled";
    }
    return config;
  }

}
