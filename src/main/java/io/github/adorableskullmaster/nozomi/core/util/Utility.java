package io.github.adorableskullmaster.nozomi.core.util;

import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Utility {

    public static boolean isNumber(String number) {
        number = number.trim();
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isColorHex(String hex) {
        hex = hex.trim();
        if (hex.length() == 6) {
            try {
                Long.parseLong(hex, 16);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else
            return false;
    }

    public static Color getGuildSpecificRoleColor(CommandEvent commandEvent) {
        Color color = commandEvent.getSelfMember().getColor();
        if (color != null)
            return color;
        return Color.CYAN;
    }

    public static Color getGuildSpecificRoleColor(GuildMemberJoinEvent guildMemberJoinEvent) {
        Color color = guildMemberJoinEvent.getGuild().getMember(guildMemberJoinEvent.getJDA().getSelfUser()).getColor();
        if (color != null)
            return color;
        return Color.CYAN;
    }

    public static String getMinutesString(int min) {
        if (min >= 24 * 60 * 7)
            return Integer.toString((int) Math.floor(min / (24 * 60 * 7))) + 'w';
        else if (min >= 24 * 60)
            return Integer.toString((int) Math.floor(min / (24 * 60))) + 'd';
        else if (min >= 60)
            return Integer.toString((int) Math.floor(min / (24))) + 'h';
        else
            return Integer.toString(min) + 'm';
    }

    public static String getPWIcon() {
        return "https://cdn.discordapp.com/attachments/392736524308840448/485867309995524096/57ad65f5467e958a079d2ee44a0e80ce.png";
    }

    public static Permission[] getModerator() {
        return new Permission[]{Permission.MANAGE_ROLES};
    }

    public static TemporalAccessor convertISO8601(String dateString) {
        DateTimeFormatter dtm = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ISO_DATE_TIME)
                .toFormatter()
                .withZone(ZoneId.of("UTC"));
        return dtm.parse(dateString);
    }

    public static LinkedHashMap<Integer, Double> sortByValue(final HashMap<Integer, Double> unsorted, boolean desc) {

        if (desc)
            return unsorted.entrySet()
                    .stream()
                    .sorted((Map.Entry.<Integer, Double>comparingByValue().reversed()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return unsorted.entrySet()
                .stream()
                .sorted((Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
