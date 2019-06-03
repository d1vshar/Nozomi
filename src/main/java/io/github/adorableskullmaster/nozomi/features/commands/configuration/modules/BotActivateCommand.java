package io.github.adorableskullmaster.nozomi.features.commands.configuration.modules;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.CommandResponseHandler;
import io.github.adorableskullmaster.nozomi.core.util.Emojis;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.nozomi.core.util.QAProvider;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.util.function.BiConsumer;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guildconfig.GUILDCONFIG;

public class BotActivateCommand extends Command {

  private final EventWaiter eventWaiter;

  public BotActivateCommand(EventWaiter eventWaiter) {
    this.name = "activate";
    this.eventWaiter = eventWaiter;
  }

  @Override
  protected void execute(CommandEvent commandEvent) {
    String[] questions = {
        "What is your main member role? (id)",
        "What is your primary alliance id? (number)"
    };

    BiConsumer<String[],CommandEvent> callbackFunction = this::updateSettings;
    QAProvider qaProvider = new QAProvider(commandEvent, questions, callbackFunction, eventWaiter);
    qaProvider.execute();
  }

  private void updateSettings(String[] answers, CommandEvent commandEvent) {
    try {
      Long roleId = Long.parseLong(answers[0]);
      Integer aaId = Integer.parseInt(answers[1]);
      Boolean activate = true;

      DSLContext db = DSL.using(Instances.getConnection());

      boolean exists = db.fetchExists(db.selectFrom(GUILDCONFIG).where(GUILDCONFIG.ID.eq(commandEvent.getGuild().getIdLong())));
      if(!exists) {
        db.insertInto(GUILDCONFIG)
            .columns(GUILDCONFIG.ID, GUILDCONFIG.AAID,GUILDCONFIG.MEMBERROLE,GUILDCONFIG.ACTIVATED,
                GUILDCONFIG.WARMODULE,GUILDCONFIG.TEXTMODULE,GUILDCONFIG.APPLICANTMODULE,
                GUILDCONFIG.BANKMODULE,GUILDCONFIG.VACMODEMODULE)
            .values(commandEvent.getGuild().getIdLong(), aaId,roleId, true,false,false,false,false,false)
            .execute();
      }
      else {
        db.update(GUILDCONFIG)
            .set(GUILDCONFIG.AAID,aaId)
            .set(GUILDCONFIG.ACTIVATED, true)
            .set(GUILDCONFIG.MEMBERROLE,roleId)
            .where(GUILDCONFIG.ID.eq(commandEvent.getGuild().getIdLong()))
            .execute();
      }
      commandEvent.reply(Emojis.SETTINGS_ACCEPT.getAsMention() + " Settings Changed.");
    }
    catch (Exception e) {
      CommandResponseHandler.error(commandEvent,"Either you fucked up or me. Try again later after contacting Nasty. k thnx.");
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }
}
