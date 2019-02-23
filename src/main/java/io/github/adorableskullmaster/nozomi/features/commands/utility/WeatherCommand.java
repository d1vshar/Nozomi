package io.github.adorableskullmaster.nozomi.features.commands.utility;

import com.jagrosh.jdautilities.command.CommandEvent;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.models.weather.OpenWeatherMapAPI;
import io.github.adorableskullmaster.nozomi.core.models.weather.current.CurrentWeather;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.features.commands.UtilityCommand;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class WeatherCommand extends UtilityCommand {

  private JSONObject icons;

  public WeatherCommand() {
    this.name = "weather";
    this.aliases = new String[]{"w"};
    this.arguments = "++weather <city name>, [country name]";
    this.help = "Provides current weather data of a location";
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("owm-icons.json");
    StringWriter writer = new StringWriter();
    try {
      IOUtils.copy(inputStream, writer, "UTF-8");
    } catch (IOException e) {
      Bot.botExceptionHandler.captureException(e);
    }
    String colorString = writer.toString();
    this.icons = new JSONObject(colorString);
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    try {
      if (!commandEvent.getArgs().trim().isEmpty()) {
        commandEvent.getChannel().sendTyping().queue();
        String arg = commandEvent.getArgs();
        CurrentWeather currentWeather;
        if (arg.trim().split(",").length == 2) {
          String[] parts = arg.trim().split(",");
          currentWeather = OpenWeatherMapAPI.fetchCurrentByName(parts[0], parts[1]);
        } else
          currentWeather = OpenWeatherMapAPI.fetchCurrentByName(arg);
        if (currentWeather.getCod() == 200) {
          String name = currentWeather.getName();
          String current = currentWeather.getWeather().get(0).getMain();
          String icon = icons.getString(currentWeather.getWeather().get(0).getIcon());
          double temp = currentWeather.getMain().getTemp();
          double wind = currentWeather.getWind().getSpeed();
          int humidity = currentWeather.getMain().getHumidity();
          int cloud = currentWeather.getClouds().getAll();

          String line1 = String.format(":flag_%s: **| %s, %s**", currentWeather.getSys().getCountry().toLowerCase(), name, currentWeather.getSys().getCountry());
          String line2 = String.format("**Weather:** %s (%s) **Temp:** %d °F / %d °C", icon, current, (int) ((1.8 * (temp - 273)) + 32), (int) temp - 273);
          String line3 = String.format("**Wind:** %.2f mph / %.2f m/s towards %.2f°", wind * 2.23, wind, currentWeather.getWind().getDeg());
          String line4 = String.format("**Humidity:** %d%% **Cloudiness:** %d%%", humidity, cloud);

          commandEvent.reply(String.join("\n", line1, line2, line3, line4));
        } else if (currentWeather.getCod() == 404) {
          commandEvent.reply("City not found.");
        }
      } else {
        CommandResponseHandler.illegal(commandEvent, name);
      }
    } catch (Exception e) {
      Bot.botExceptionHandler.captureException(e, commandEvent);
    }
  }
}
