package io.github.adorableskullmaster.nozomi.core.apis.weather;

import com.google.gson.Gson;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.apis.weather.current.CurrentWeather;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class OpenWeatherMapAPI {

  private static String baseUrl = "http://api.openweathermap.org/data/2.5/weather";

  public static CurrentWeather fetchCurrentByName(String name) throws URISyntaxException, IOException {
    URIBuilder builder = new URIBuilder(baseUrl);
    builder.addParameter("q", name).addParameter("APPID", Bot.configuration.getOwmKey());

    return fetchCurrent(builder.build());
  }

  public static CurrentWeather fetchCurrentByName(String name, String country) throws URISyntaxException, IOException {
    URIBuilder builder = new URIBuilder(baseUrl);
    builder.addParameter("q", name + "," + country).addParameter("APPID", Bot.configuration.getOwmKey());

    return fetchCurrent(builder.build());
  }

  private static CurrentWeather fetchCurrent(URI url) throws IOException {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(url);
    HttpResponse response = client.execute(request);
    HttpEntity entity = response.getEntity();
    String content = EntityUtils.toString(entity);
    return new Gson().fromJson(content, CurrentWeather.class);
  }
}
