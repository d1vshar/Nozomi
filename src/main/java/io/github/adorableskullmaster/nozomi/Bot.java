package io.github.adorableskullmaster.nozomi;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.core.cache.Cache;
import io.github.adorableskullmaster.nozomi.core.config.StaticConfiguration;
import io.github.adorableskullmaster.nozomi.core.util.BotExceptionHandler;
import io.github.adorableskullmaster.nozomi.features.commands.admin.ShutdownCommand;
import io.github.adorableskullmaster.nozomi.features.commands.admin.StatusCommand;
import io.github.adorableskullmaster.nozomi.features.commands.gov.RegisterCommand;
import io.github.adorableskullmaster.nozomi.features.commands.gov.WhoIsCommand;
import io.github.adorableskullmaster.nozomi.features.commands.member.*;
import io.github.adorableskullmaster.nozomi.features.hooks.JoinListener;
import io.github.adorableskullmaster.nozomi.features.services.BankCheckService;
import io.github.adorableskullmaster.nozomi.features.services.NewApplicantService;
import io.github.adorableskullmaster.nozomi.features.services.NewWarService;
import io.github.adorableskullmaster.nozomi.features.services.VMBeigeService;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
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
    public static final BotExceptionHandler BOT_EXCEPTION_HANDLER;
    public static final Cache CACHE;
    public static JDA jda;
    public static StaticConfiguration staticConfiguration;
    public static DataSource dataSource;

    static {
        LOGGER = LoggerFactory.getLogger(Bot.class);
        staticConfiguration = new StaticConfiguration();
        BOT_EXCEPTION_HANDLER = new BotExceptionHandler();
        CACHE = new Cache();
        dataSource = setupDataSource(staticConfiguration.getDbUrl());
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
                .setGame(Game.playing(staticConfiguration.getPrefix() + "help"))
                .useHelpBuilder(false);

        jda = new JDABuilder(AccountType.BOT)
                .setToken(staticConfiguration.getBotToken())
                .addEventListener(eventWaiter)
                .addEventListener(clientBuilder.build())
                .addEventListener(new JoinListener())
                .build()
                .awaitReady();
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

    private static void initServices() {
        LOGGER.info("Starting Services");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(new NewWarService(), 0, staticConfiguration.getNewWarFrequency(), TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new BankCheckService(), 0, staticConfiguration.getBankCheckFrequency(), TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new NewApplicantService(), 0, staticConfiguration.getNewApplicantFrequency(), TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new VMBeigeService(), 0, 1, TimeUnit.MINUTES);
    }
}