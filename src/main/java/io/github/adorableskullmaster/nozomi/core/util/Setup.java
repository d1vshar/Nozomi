package io.github.adorableskullmaster.nozomi.core.util;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.DB;
import io.github.adorableskullmaster.nozomi.core.database.DBSetup;
import io.github.adorableskullmaster.nozomi.features.commands.SetupCommand;
import io.github.adorableskullmaster.nozomi.features.commands.fun.AnimalCommand;
import io.github.adorableskullmaster.nozomi.features.commands.fun.ChooseCommand;
import io.github.adorableskullmaster.nozomi.features.commands.fun.CowsayCommand;
import io.github.adorableskullmaster.nozomi.features.commands.fun.EchoCommand;
import io.github.adorableskullmaster.nozomi.features.commands.owner.ShutdownCommand;
import io.github.adorableskullmaster.nozomi.features.commands.owner.StatusCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.AllianceSearchCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.NationSearchCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.member.AnalyzeCommand;
import io.github.adorableskullmaster.nozomi.features.commands.pw.member.CounterCommand;
import io.github.adorableskullmaster.nozomi.features.commands.utility.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Setup {

  public static void initDatabase() {
    try (Connection conn = Instances.getConnection()) {
      DBSetup dbSetup = new DBSetup(DSL.using(conn));
      dbSetup.setupApplicants();
      dbSetup.setupChannels();
      dbSetup.setupGuilds();
      dbSetup.setupTexts();
      dbSetup.setupWars();
    } catch (SQLException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  public static Command[] initCommands(EventWaiter eventWaiter) {
    boolean cheweyEnabled = Bot.configuration.isCheweyEnabled();
    boolean weatherEnabled = Bot.configuration.isWeatherEnabled();

    ArrayList<Command> commands = new ArrayList<>();
    commands.add(new HelpCommand());
    commands.add(new PingCommand());
    commands.add(new ChooseCommand());
    commands.add(new EchoCommand());
    commands.add(new CounterCommand());
    commands.add(new ColorCommand());
    commands.add(new ServerInfoCommand());
    commands.add(new StatusCommand());
    commands.add(new ShutdownCommand(eventWaiter));
    commands.add(new AllianceSearchCommand(eventWaiter));
    commands.add(new AboutCommand());
    commands.add(new AnalyzeCommand());
    commands.add(new NationSearchCommand(eventWaiter));
    commands.add(new CowsayCommand());
    commands.add(new SetupCommand(eventWaiter));

    if (cheweyEnabled)
      commands.add(new AnimalCommand());
    if (weatherEnabled)
      commands.add(new WeatherCommand());

    return commands.toArray(new Command[0]);
  }

  public static void initGuild(long id) {
    try {
      DB db = new DB();
      db.initGuild(id);
      db.close();
    } catch (SQLException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

}
