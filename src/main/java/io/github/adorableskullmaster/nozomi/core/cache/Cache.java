package io.github.adorableskullmaster.nozomi.core.cache;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarAPIException;
import io.github.adorableskullmaster.pw4j.domains.AllCities;
import io.github.adorableskullmaster.pw4j.domains.Alliances;
import io.github.adorableskullmaster.pw4j.domains.NationMilitary;
import io.github.adorableskullmaster.pw4j.domains.Nations;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Cache {

  private EntityCache<Nations> nationsCache;
  private EntityCache<Alliances> allianceCache;
  private EntityCache<AllCities> allCitiesCache;
  private EntityCache<NationMilitary> nationMilitaryCache;

  private PoliticsAndWar politicsAndWar;

  public Cache() {
    this.allianceCache = new EntityCache<>();
    this.allCitiesCache = new EntityCache<>();
    this.nationMilitaryCache = new EntityCache<>();
    this.nationsCache = new EntityCache<>();
    this.politicsAndWar = Instances.getDefaultPW();
    Bot.LOGGER.info(Bot.configuration.getMasterPWKey());

    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    update();
    executorService.scheduleAtFixedRate(new CacheUpdater(), 1800, 1800, TimeUnit.SECONDS);
    Bot.LOGGER.info("Cache initiated");
  }

  public Nations getNations() {
    return nationsCache.getEntity();
  }

  public Alliances getAlliances() {
    return allianceCache.getEntity();
  }

  public AllCities getAllCities() {
    return allCitiesCache.getEntity();
  }

  public NationMilitary getNationMilitary() {
    return nationMilitaryCache.getEntity();
  }

  private void update() {
    try {
      Bot.LOGGER.info("Updating cache...");
      Instant start = Instant.now();

      Nations nations = politicsAndWar.getNations(true);
      NationMilitary allMilitaries = politicsAndWar.getAllMilitaries();
      AllCities allCities = politicsAndWar.getAllCities();
      Alliances alliances = politicsAndWar.getAlliances();

      nationsCache.updateEntity(nations);
      allianceCache.updateEntity(alliances);
      allCitiesCache.updateEntity(allCities);
      nationMilitaryCache.updateEntity(allMilitaries);

      Instant end = Instant.now();
      Bot.LOGGER.info(String.format("Cache updated in %ds.", Duration.between(start, end).getSeconds()));
    } catch (PoliticsAndWarAPIException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  private class CacheUpdater implements Runnable {
    @Override
    public void run() {
      update();
    }
  }
}