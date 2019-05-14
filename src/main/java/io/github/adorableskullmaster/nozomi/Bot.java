package io.github.adorableskullmaster.nozomi;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.core.cache.Cache;
import io.github.adorableskullmaster.nozomi.core.config.Configuration;
import io.github.adorableskullmaster.nozomi.core.util.BotExceptionHandler;
import io.github.adorableskullmaster.nozomi.core.util.Setup;
import io.github.adorableskullmaster.nozomi.features.hooks.DatabaseListener;
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

import javax.security.auth.login.LoginException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Bot {

  public static final Logger LOGGER;
  public static final BotExceptionHandler BOT_EXCEPTION_HANDLER;
  public static final Cache CACHE;
  public static JDA jda;
  public static Configuration configuration;

  static {
    LOGGER = LoggerFactory.getLogger(Bot.class);
    configuration = new Configuration();
    BOT_EXCEPTION_HANDLER = new BotExceptionHandler();
    CACHE = new Cache();
    Setup.initDatabase();
  }

  public static void main(String[] args) throws LoginException, InterruptedException {
    LOGGER.info("Starting Bot");

    EventWaiter eventWaiter = new EventWaiter();

    CommandClientBuilder clientBuilder = new CommandClientBuilder();
    clientBuilder.setPrefix(configuration.getPrefix())
        .setOwnerId(configuration.getOwnerId())
        .setEmojis("✔", "‼", "❌")
        .setServerInvite("https://discord.gg/GrnewCF")
        .setStatus(OnlineStatus.ONLINE)
        .setGame(Game.playing(configuration.getPrefix()+"help"))
        .useHelpBuilder(false)
        .addCommands(Setup.initCommands(eventWaiter));

    jda = new JDABuilder(AccountType.BOT)
        .setToken(configuration.getBotToken())
        .addEventListener(eventWaiter)
        .addEventListener(clientBuilder.build())
        .addEventListener(new GenericListener())
        .addEventListener(new DatabaseListener())
        .build()
        .awaitReady();

    initServices();
  }

  private static void initServices() {
    LOGGER.info("Starting Services");
    ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
    executorService.scheduleAtFixedRate(new NewWarService(), 0, configuration.getNewWarFrequency(), TimeUnit.MINUTES);
    executorService.scheduleAtFixedRate(new BankCheckService(), 0, configuration.getBankCheckFrequency(), TimeUnit.MINUTES);
    executorService.scheduleAtFixedRate(new NewApplicantService(), 0, configuration.getNewApplicantFrequency(), TimeUnit.MINUTES);
    executorService.scheduleAtFixedRate(new VMBeigeService(), 0, 1, TimeUnit.MINUTES);
  }
}