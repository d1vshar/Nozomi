package io.github.adorableskullmaster.nozomi.features.hooks;

import io.github.adorableskullmaster.nozomi.core.util.Setup;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class DatabaseListener extends ListenerAdapter {

  //Add Guild
  @Override
  public void onGuildJoin(GuildJoinEvent event) {
    Setup.initGuild(event.getGuild().getIdLong());
  }

  //TODO Remove Guild
  @Override
  public void onGuildLeave(GuildLeaveEvent event) {

  }
}
