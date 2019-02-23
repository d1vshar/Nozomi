package io.github.adorableskullmaster.nozomi.core.config;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class ConfigManager {

  private Gson gson = new Gson();
  private Config config;

  public ConfigManager() {
    try {
      load();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private void load() throws IOException {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("config/config.json");
    if (inputStream == null)
      throw new IOException("config.json is missing");
    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer, "UTF-8");
    String json = writer.toString();
    this.parse(json);
  }

  private void parse(String json) throws JsonSyntaxException {
    Config config = gson.fromJson(json, Config.class);
    this.config = config;
  }

  public Config getConfig() {
    return config;
  }

  public boolean isDevMode() {
    return Boolean.parseBoolean(System.getenv("DEV"));
  }

  public String getRealDB() { return System.getenv("JDBC_DATABASE_URL");}
}
