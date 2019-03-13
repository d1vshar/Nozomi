package io.github.adorableskullmaster.nozomi;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.core.cache.CacheManager;
import io.github.adorableskullmaster.nozomi.core.config.Config;
import io.github.adorableskullmaster.nozomi.core.config.ConfigManager;
import io.github.adorableskullmaster.nozomi.core.database.PGHandler;
import io.github.adorableskullmaster.nozomi.core.util.BotExceptionHandler;
import io.github.adorableskullmaster.nozomi.features.commands.fun.AnimalCommand;
import io.github.adorableskullmaster.nozomi.features.commands.fun.ChooseCommand;
import io.github.adorableskullmaster.nozomi.features.commands.fun.CowsayCommand;
import io.github.adorableskullmaster.nozomi.features.commands.moderator.EchoCommand;
import io.github.adorableskullmaster.nozomi.features.commands.moderator.PurgeCommand;
import io.github.adorableskullmaster.nozomi.features.commands.owner.ShutdownCommand;
import io.github.adorableskullmaster.nozomi.features.commands.owner.StatusCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.AllianceSearchCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.NationSearchCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.member.AnalyzeCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.member.CounterCommand;
import io.github.adorableskullmaster.nozomi.features.commands.utility.*;
import io.github.adorableskullmaster.nozomi.features.hooks.GenericListener;
import io.github.adorableskullmaster.nozomi.features.services.BankCheckService;
import io.github.adorableskullmaster.nozomi.features.services.NewApplicantService;
import io.github.adorableskullmaster.nozomi.features.services.NewWarService;
import io.github.adorableskullmaster.nozomi.features.services.VMBeigeService;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Bot {

  public static final Logger LOGGER;
  public static final Config config;
  public static final BotExceptionHandler botExceptionHandler;
  public static final CacheManager cacheManager;
  public static final PGHandler pg;
  public static JDA jda;
  public static final Boolean dev;
  private static ConfigManager configManager;

  static {
    LOGGER = LoggerFactory.getLogger(Bot.class);
    configManager = new ConfigManager();
    config = configManager.getConfig();
    dev = configManager.isDevMode();
    botExceptionHandler = new BotExceptionHandler();
    cacheManager = new CacheManager();
    if(dev)
      pg = new PGHandler(config.getCredentials().getTestDB());
    else
      pg = new PGHandler(configManager.getRealDB());
  }

  public static void main(String[] args) {
    try {
      LOGGER.info("Starting Bot");
      if (dev)
        LOGGER.info("DEV MODE");

      EventWaiter eventWaiter = new EventWaiter();

      CommandClientBuilder clientBuilder = new CommandClientBuilder();
      String prefix = dev? config.getCredentials().getTestPrefix():config.getCredentials().getPrefix();
      String token = dev? config.getCredentials().getTestBotToken():config.getCredentials().getBotToken();
      clientBuilder.setPrefix(prefix)
          .setOwnerId(Long.toString(config.getCredentials().getOwnerId()))
          .setEmojis("✔", "‼", "❌")
          .setServerInvite("https://discord.gg/CrCeTWm")
          .setStatus(OnlineStatus.ONLINE)
          .setGame(Game.watching("over Sheepy"))
          .useHelpBuilder(false)
          .addCommands(
              new HelpCommand(),
              new PingCommand(),
              new ChooseCommand(),
              new WeatherCommand(),
              new EchoCommand(),
              new CounterCommand(),
              new ColorCommand(),
              new ServerInfoCommand(),
              new StatusCommand(),
              new ShutdownCommand(eventWaiter),
              new PurgeCommand(),
              new AllianceSearchCommand(eventWaiter),
              new AboutCommand(),
              new AnalyzeCommand(),
              new NationSearchCommand(eventWaiter),
              new AnimalCommand(),
              new CowsayCommand()
          );
      jda = new JDABuilder(AccountType.BOT)
          .setToken(token)
          .addEventListener(eventWaiter)
          .addEventListener(clientBuilder.build())
          .addEventListener(new GenericListener())
          .build()
          .awaitReady();

      if (!configManager.isDevMode()) {
        LOGGER.info("Starting Timer Threads");

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(new NewWarService(), 250, 300, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new BankCheckService(), 250, 1800, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new VMBeigeService(), 250, 60, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new NewApplicantService(), 250, 3600, TimeUnit.SECONDS);
      }
    } catch (Exception e) {
      botExceptionHandler.captureException(e);
    }
  }
}