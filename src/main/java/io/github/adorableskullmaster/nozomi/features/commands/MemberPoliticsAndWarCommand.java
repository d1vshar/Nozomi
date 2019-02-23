package io.github.adorableskullmaster.nozomi.features.commands;

import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;

public abstract class MemberPoliticsAndWarCommand extends PoliticsAndWarCommand {
  protected MemberPoliticsAndWarCommand() {
    this.category = new Category("Utility",
        "❌ You do not have the Member role", event -> AuthUtility.checkCommand(name,event.getGuild().getIdLong(),event.getMember()));
  }
}
