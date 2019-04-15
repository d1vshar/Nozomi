package io.github.adorableskullmaster.nozomi.core.models.animals;

import io.github.adorableskullmaster.nozomi.Bot;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class RandomAnimal {

  private static String fetch(String url) throws IOException {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet request = new HttpGet(url);
    HttpResponse response = client.execute(request);
    if (response.getStatusLine().getStatusCode() == 200) {
      HttpEntity entity = response.getEntity();
      return EntityUtils.toString(entity);
    } else
      throw new IOException();
  }

  public String getRandomCat() {
    try {
      String url = "https://aws.random.cat/meow";
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("file");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getRandomDog() {
    try {
      String url = "https://random.dog/woof.json";
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("url");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getRandomWolf() {
    try {
      String url = "https://randomfox.ca/floof/";
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("image");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getShibe() {
    try {
      String url = "http://shibe.online/api/shibes";
      JSONArray result = new JSONArray(fetch(url));
      return result.getString(0);
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweyCat() {
    try {
      String url = "https://api.chewey-bot.ga/cat?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweyBirb() {
    try {
      String url = "https://api.chewey-bot.ga/birb?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweyRabbit() {
    try {
      String url = "https://api.chewey-bot.ga/rabbit?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweySnake() {
    try {
      String url = "https://api.chewey-bot.ga/snake?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweyOtter() {
    try {
      String url = "https://api.chewey-bot.ga/otter?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweyDog() {
    try {
      String url = "https://api.chewey-bot.ga/dog?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweyDuck() {
    try {
      String url = "https://api.chewey-bot.ga/duck?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }

  public String getCheweyTurtle() {
    try {
      String url = "https://api.chewey-bot.ga/turtle?auth=" + Bot.configuration.getCheweyKey();
      JSONObject result = new JSONObject(fetch(url));
      return result.getString("data");
    } catch (IOException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      return null;
    }
  }
}
