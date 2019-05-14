package io.github.adorableskullmaster.nozomi.core.cache;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarAPIException;
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
  private EntityCache<NationMilitary> nationMilitaryCache;

  private PoliticsAndWar politicsAndWar;

  public Cache() {
    this.allianceCache = new EntityCache<>();
    this.nationMilitaryCache = new EntityCache<>();
    this.nationsCache = new EntityCache<>();
    this.politicsAndWar = Instances.getDefaultPW();

    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
    update();
    executorService.scheduleAtFixedRate(new CacheUpdater(), 1, 1, TimeUnit.HOURS);
    Bot.LOGGER.info("Cache initiated");
  }

  public Nations getNations() {
    return nationsCache.getEntity();
  }

  public Alliances getAlliances() {
    return allianceCache.getEntity();
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
      Alliances alliances = politicsAndWar.getAlliances();

      nationsCache.updateEntity(nations);
      allianceCache.updateEntity(alliances);
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
