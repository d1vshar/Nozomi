package io.github.adorableskullmaster.nozomi.core.util;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.*;
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

  public static boolean hasRoles(Member member, String... roles) {
    List<String> roleList = Arrays.asList(roles);
    return member.getRoles()
        .stream()
        .map(Role::getName)
        .collect(Collectors.toList())
        .containsAll(roleList);
  }

  public static Permission[] getModerator() {
    return new Permission[]{Permission.MANAGE_ROLES};
  }

  public static TemporalAccessor convertISO8601(String datestring) {
    DateTimeFormatter dtm = new DateTimeFormatterBuilder()
        .append(DateTimeFormatter.ISO_DATE_TIME)
        .toFormatter()
        .withZone(ZoneId.of("UTC"));
    return dtm.parse(datestring);
  }

  public static <K, V extends Comparable<? super V>> LinkedHashMap<K, V> sortByValue(HashMap<K, V> map, boolean ascending) {
    ArrayList<HashMap.Entry<K, V>> list = new ArrayList<>(map.entrySet());

    if (ascending)
      list.sort(HashMap.Entry.comparingByValue());
    else
      list.sort(HashMap.Entry.comparingByValue(Comparator.reverseOrder()));

    LinkedHashMap<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : list) {
      result.put(entry.getKey(), entry.getValue());
    }

    return result;
  }

}
