package io.github.adorableskullmaster.nozomi.features.commands;

import io.github.adorableskullmaster.nozomi.Bot;
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
            return event.getMember()
                .getRoles()
                .stream()
                .map(Role::getIdLong)
                .collect(Collectors.toList())
                .contains(Instances.getDBLayer().getGuild(event.getGuild().getIdLong()).getMemberRole());
          } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
            return false;
          }
        }
    );
  }
}
