package io.github.adorableskullmaster.nozomi.core.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoConnection {
  private MongoClient mongoClient;
  private String url;

  public MongoConnection() {
  }

  public MongoConnection(String url) {
    this.url = url;
  }

  public void init() {
    mongoClient = new MongoClient(new MongoClientURI(url));
  }

  public static MongoConnection getInstance() {
    return new MongoConnection();
  }

  public MongoClient getMongoClient() {
    return mongoClient;
  }

  public void setMongoClient(MongoClient mongoClient) {
    this.mongoClient = mongoClient;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
