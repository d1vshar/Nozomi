package io.github.adorableskullmaster.nozomi.core.cache;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarAPIException;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.AllCities;
import io.github.adorableskullmaster.pw4j.domains.Alliances;
import io.github.adorableskullmaster.pw4j.domains.NationMilitary;
import io.github.adorableskullmaster.pw4j.domains.Nations;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CacheManager {

  private EntityCache<Nations> nationsCache;
  private EntityCache<Alliances> allianceCache;
  private EntityCache<AllCities> allCitiesCache;
  private EntityCache<NationMilitary> nationMilitaryCache;

  private PoliticsAndWar politicsAndWar;

  public CacheManager() {
    this.allianceCache = new EntityCache<>();
    this.allCitiesCache = new EntityCache<>();
    this.nationMilitaryCache = new EntityCache<>();
    this.nationsCache = new EntityCache<>();
    this.politicsAndWar = new PoliticsAndWarBuilder()
        .setApiKey(Bot.config.getCredentials().getMasterPWKey())
        .build();

    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    executorService.scheduleAtFixedRate(new CacheUpdater(), 0, 1800, TimeUnit.SECONDS);
    Bot.LOGGER.info("CacheManager initiated");
  }

  public Nations getNations() {
    return nationsCache.getEntity();
  }

  public Alliances getAlliance() {
    return allianceCache.getEntity();
  }

  public AllCities getAllCities() {
    return allCitiesCache.getEntity();
  }

  public NationMilitary getNationMilitary() {
    return nationMilitaryCache.getEntity();
  }

  private class CacheUpdater implements Runnable {
    @Override
    public void run() {
      try {
        Bot.LOGGER.info("CacheUpdater started");
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
        Bot.LOGGER.info(String.format("CacheUpdater completed in %ds.", Duration.between(start, end).getSeconds()));
      } catch (PoliticsAndWarAPIException e) {
        Bot.botExceptionHandler.captureException(e);
      }
    }
  }
}
