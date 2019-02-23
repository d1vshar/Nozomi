package io.github.adorableskullmaster.nozomi.core.models.weather.current;

import io.github.adorableskullmaster.nozomi.core.models.weather.current.components.*;

import java.util.List;

public class CurrentWeather {
  private Coord coord;
  private List<Weather> weather = null;
  private String base;
  private Main main;
  private int visibility;
  private Wind wind;
  private Clouds clouds;
  private Rain rain;
  private Snow snow;
  private int dt;
  private Sys sys;
  private int id;
  private String name;
  private int cod;

  public Coord getCoord() {
    return coord;
  }

  public List<Weather> getWeather() {
    return weather;
  }

  public String getBase() {
    return base;
  }

  public Main getMain() {
    return main;
  }

  public int getVisibility() {
    return visibility;
  }

  public Wind getWind() {
    return wind;
  }

  public Clouds getClouds() {
    return clouds;
  }

  public Rain getRain() {
    return rain;
  }

  public Snow getSnow() {
    return snow;
  }

  public int getDt() {
    return dt;
  }

  public Sys getSys() {
    return sys;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getCod() {
    return cod;
  }
}
