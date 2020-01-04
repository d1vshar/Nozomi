package io.github.adorableskullmaster.nozomi;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.commands.admin.ShutdownCommand;
import io.github.adorableskullmaster.nozomi.commands.admin.StatusCommand;
import io.github.adorableskullmaster.nozomi.commands.gov.RegisterCommand;
import io.github.adorableskullmaster.nozomi.commands.gov.WhoIsCommand;
import io.github.adorableskullmaster.nozomi.commands.member.*;
import io.github.adorableskullmaster.nozomi.core.cache.Cache;
import io.github.adorableskullmaster.nozomi.core.config.StaticConfiguration;
import io.github.adorableskullmaster.nozomi.core.database.ConfigurationDataSource;
import io.github.adorableskullmaster.nozomi.core.database.models.Configuration;
import io.github.adorableskullmaster.nozomi.core.util.BotExceptionHandler;
import io.github.adorableskullmaster.nozomi.hooks.JoinListener;
import io.github.adorableskullmaster.nozomi.logging.BotLogger;
import io.github.adorableskullmaster.nozomi.services.BankCheckService;
import io.github.adorableskullmaster.nozomi.services.NewApplicantService;
import io.github.adorableskullmaster.nozomi.services.NewWarService;
import io.github.adorableskullmaster.nozomi.services.VMBeigeService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Bot {

    public static final Logger LOGGER;
    public static final BotLogger BOT_LOGGER;
    public static final BotExceptionHandler BOT_EXCEPTION_HANDLER;
    public static final Cache CACHE;
    public static JDA jda;
    public static StaticConfiguration staticConfiguration;
    public static DataSource dataSource;

    static {
        LOGGER = LoggerFactory.getLogger(Bot.class);
        BOT_LOGGER = new BotLogger();
        staticConfiguration = new StaticConfiguration();
        BOT_EXCEPTION_HANDLER = new BotExceptionHandler();
        CACHE = new Cache();
        dataSource = setupDataSource(staticConfiguration.getDbUrl());
        BOT_LOGGER.setChannelId(ConfigurationDataSource.getConfiguration().getLogChannel());
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        LOGGER.info("Starting Bot");

        EventWaiter eventWaiter = new EventWaiter();

        CommandClientBuilder clientBuilder = new CommandClientBuilder();
        clientBuilder.setPrefix(staticConfiguration.getPrefix())
                .setOwnerId(staticConfiguration.getOwnerId())
                .setEmojis("✔", "‼", "❌")
                .addCommand(new HelpCommand())
                .addCommand(new CounterCommand())
                .addCommand(new StatusCommand())
                .addCommand(new ShutdownCommand(eventWaiter))
                .addCommand(new AllianceSearchCommand(eventWaiter))
                .addCommand(new AnalyzeCommand())
                .addCommand(new NationSearchCommand(eventWaiter))
                .addCommand(new RegisterCommand())
                .addCommand(new WhoIsCommand())
                .setServerInvite("https://discord.gg/GrnewCF")
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.playing(staticConfiguration.getPrefix() + "help"))
                .useHelpBuilder(false);

        jda = new JDABuilder()
                .setToken(staticConfiguration.getBotToken())
                .addEventListeners(eventWaiter, clientBuilder.build(), new JoinListener())
                .build()
                .awaitReady();

        printConfig();
        initServices();
    }

    private static DataSource setupDataSource(String uri) {
        BasicDataSource bds = new BasicDataSource();
        bds.setUrl(uri);
        bds.setMinIdle(2);
        bds.setMaxIdle(4);
        bds.setDriverClassName("org.postgresql.Driver");
        return bds;
    }

    private static void printConfig() {
        LOGGER.info("Bot set for alliance ID: {}", staticConfiguration.getPWId());
        if (ConfigurationDataSource.isSetup()) {
            System.out.println("Found configuration in database");
            Configuration configuration1 = ConfigurationDataSource.getConfiguration();
            System.out.println(configuration1);
        } else
            System.out.println("No configuration found in database or database corrupt");
    }

    private static void initServices() {
        LOGGER.info("Starting Services");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(new NewWarService(), 0, staticConfiguration.getNewWarFrequency(), TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new BankCheckService(), 0, staticConfiguration.getBankCheckFrequency(), TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new NewApplicantService(), 0, staticConfiguration.getNewApplicantFrequency(), TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new VMBeigeService(), 0, 1, TimeUnit.MINUTES);
    }
}