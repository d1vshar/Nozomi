package io.github.adorableskullmaster.nozomi.features.commands.types;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.layer.GuildSettings;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import net.dv8tion.jda.core.entities.Role;

import java.sql.SQLException;
import java.util.stream.Collectors;

public abstract class MemberPoliticsAndWarCommand extends PoliticsAndWarCommand {
  protected MemberPoliticsAndWarCommand() {
    this.category = new Category(
        "PW",
        "❌ You do not have the Member role",
        event -> {
          try {
            GuildSettings guildSettings = Instances.getBotDatabaseLayer().getGuildSettings(event.getGuild().getIdLong());
            return guildSettings!=null &&
                event.getMember()
                .getRoles()
                .stream()
                .map(Role::getIdLong)
                .collect(Collectors.toList())
                .contains(guildSettings.getModuleSettings().getMemberRole());
          } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
            return false;
          }
        }
    );
  }
}
