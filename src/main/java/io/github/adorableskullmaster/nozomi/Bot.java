package io.github.adorableskullmaster.nozomi;

import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import io.github.adorableskullmaster.nozomi.core.cache.Cache;
import io.github.adorableskullmaster.nozomi.core.config.Configuration;
import io.github.adorableskullmaster.nozomi.core.mongo.MongoBotConnection;
import io.github.adorableskullmaster.nozomi.features.services.NewWarService;
import io.github.adorableskullmaster.nozomi.features.services.VMBeigeService;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Bot {

    private static final Logger LOGGER;
    private static final Cache CACHE;
    private static JDA jda;
    private static MongoBotConnection mongoBotConnection;

    static {
        LOGGER = LoggerFactory.getLogger(Bot.class);
        CACHE = new Cache();

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applicationName("poscaBot")
                .codecRegistry(pojoCodecRegistry)
                .applyConnectionString(new ConnectionString(System.getenv("MONGO_URL")))
                .build();

        mongoBotConnection = new MongoBotConnection(mongoClientSettings);
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        getLOGGER().info("Starting Bot");

        EventWaiter eventWaiter = new EventWaiter();

        CommandClientBuilder clientBuilder = new CommandClientBuilder();
        clientBuilder.setPrefix(Configuration.getPrefix())
                .setOwnerId(Configuration.getOwnerId())
                .setEmojis("✔", "‼", "❌")
                .setServerInvite("https://discord.gg/GrnewCF")
                .setStatus(OnlineStatus.ONLINE)
                .setGame(Game.playing(Configuration.getPrefix() + "help"))
                .useHelpBuilder(false);

        jda = new JDABuilder(AccountType.BOT)
                .setToken(Configuration.getBotToken())
                .addEventListener(eventWaiter)
                .addEventListener(clientBuilder.build())
                .build()
                .awaitReady();

        initServices();
    }

    private static void initServices() {
        getLOGGER().info("Starting Services");
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(new NewWarService(), 0, Configuration.getNewWarFrequency(), TimeUnit.MINUTES);
        executorService.scheduleAtFixedRate(new VMBeigeService(), 0, 1, TimeUnit.MINUTES);
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public static Cache getCACHE() {
        return CACHE;
    }

    public static JDA getJda() {
        return jda;
    }

    public static MongoBotConnection getMongoBotConnection() {
        return mongoBotConnection;
    }
}