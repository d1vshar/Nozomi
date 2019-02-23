package io.github.adorableskullmaster.nozomi.core.util;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.config.Config;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class AuthUtility {

  public static Config.ConfigGuild getGuildConfig(long discordId) {
    List<Config.ConfigGuild> guilds = Bot.config.getGuilds();
    List<Config.ConfigGuild> filtered = guilds.stream().filter(guilds1 -> guilds1.getDiscordId()==discordId).collect(Collectors.toList());
    if(filtered.size()==1)
      return filtered.get(0);
    else
      return null;
  }

  public static boolean checkCommand(String name, long discordId) {
    Config.ConfigGuild guild = getGuildConfig(discordId);
    if(guild==null)
      return false;
    return guild.getExcludedCommands() != null && !guild.getExcludedCommands().contains(name);
  }

  public static boolean checkCommand(String name, long discordId, Member member) {
    Config.ConfigGuild guild = getGuildConfig(discordId);
    return guild != null && checkCommand(name, discordId) && member.getRoles().stream().map(Role::getIdLong).anyMatch(id -> id == guild.getMemberRole());
  }

  public static boolean checkService(String name, long discordId) {
    Config.ConfigGuild guild = getGuildConfig(discordId);
    if(guild==null)
      return false;
    return guild.getExcludedServices() != null && !guild.getExcludedServices().contains(name);
  }

}
