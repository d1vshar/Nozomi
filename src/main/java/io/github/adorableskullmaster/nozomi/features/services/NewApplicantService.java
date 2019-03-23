package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.config.Config;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.ApplicantsRecord;
import io.github.adorableskullmaster.nozomi.core.util.AuthUtility;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.Applicants;
import io.github.adorableskullmaster.pw4j.domains.subdomains.ApplicantNationsContainer;
import net.dv8tion.jda.core.EmbedBuilder;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Applicants.APPLICANTS;

public class NewApplicantService implements Runnable {

  private final PoliticsAndWar politicsAndWar;

  public NewApplicantService() {
    this.politicsAndWar = new PoliticsAndWarBuilder()
        .setApiKey(Bot.config.getCredentials().getMasterPWKey())
        .build();
  }

  @Override
  public void run() {
    try {
      List<Config.ConfigGuild> guilds = Bot.config.getGuilds();
      for(Config.ConfigGuild guild : guilds) {
        if(AuthUtility.checkService("NewApplicantService",guild.getDiscordId())) {
          if (politicsAndWar.getAlliance(guild.getPwId()).getApplicants() > 0) {
            List<Integer> currentApplicants = getCurrentApplicants(guild.getPwId());
            List<Integer> diff = getDiff(getLoadedApplicants(guild.getPwId()), currentApplicants);
            update(guild.getPwId(), currentApplicants);

            for (Integer id : diff) {
              EmbedBuilder embedBuilder = new EmbedBuilder().setTitle("New Applicant!")
                  .setColor(Color.CYAN)
                  .setAuthor("https://politicsandwar.com/nation/id=" + id, "https://politicsandwar.com/nation/id=" + id);

              Bot.jda.getTextChannelById(guild.getGovChannel())
                  .sendMessage(embedBuilder.build())
                  .queue();
            }
          }
        }
      }
    } catch (Throwable e) {
      Bot.botExceptionHandler.captureException(e);
    }
  }

  private List<Integer> getDiff(List<Integer> loaded, List<Integer> current) {
    List<Integer> diff = new ArrayList<>();
    for (Integer id : current) {
      if (!loaded.contains(id)) {
        diff.add(id);
      }
    }
    return diff;
  }

  private List<Integer> getCurrentApplicants(int aid) {
    Applicants applicants = politicsAndWar.getApplicants(aid);

    return applicants.getApplicants()
        .stream()
        .map(ApplicantNationsContainer::getNationid)
        .collect(Collectors.toList());
  }

  private List<Integer> getLoadedApplicants(int aid) {
    List<Integer> result = new ArrayList<>();
    result.add(0);
    try (DSLContext db = DSL.using(Bot.pg.getConn(), SQLDialect.POSTGRES)) {
      ApplicantsRecord fetch = db.selectFrom(APPLICANTS).where(APPLICANTS.AID.eq(aid)).fetchOne();
      if (fetch != null)
        result.addAll(Arrays.asList(fetch.getApplicants()));
    } catch (DataAccessException e) {
      Bot.botExceptionHandler.captureException(e);
    }
    return result;
  }

  private void update(int aid, List<Integer> list) {
    try (DSLContext db = DSL.using(Bot.pg.getConn(), SQLDialect.POSTGRES)) {
      db.delete(APPLICANTS).execute();
      db.insertInto(APPLICANTS)
          .columns(APPLICANTS.AID, APPLICANTS.APPLICANTS_)
          .values(aid, list.toArray(new Integer[0]))
          .execute();
    } catch (DataAccessException e) {
      Bot.botExceptionHandler.captureException(e);
    }
  }

}
