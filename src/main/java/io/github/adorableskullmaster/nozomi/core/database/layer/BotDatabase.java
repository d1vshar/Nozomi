package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.ApplicantsRecord;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.WarsRecord;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Applicants.APPLICANTS;
import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guildconfig.GUILDCONFIG;
import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Wars.WARS;

public class BotDatabase {
  private final Connection conn;
  private final DSLContext db;

  public BotDatabase() throws SQLException {
    this.conn = Instances.getConnection();
    this.db = DSL.using(conn, SQLDialect.POSTGRES);
  }

  public GuildSettings getGuildSettings(long id) {
    boolean exists = db.fetchExists(
        db.selectFrom(GUILDCONFIG)
            .where(GUILDCONFIG.ID.eq(id))
    );
    if(exists)
      new GuildSettings(db,id);
    return null;
  }

  public Result<WarsRecord> getWarsDir() {
    return db.selectFrom(WARS).fetch();
  }

  public void storeWarsDir(List<Integer> newWars) {

    db.delete(WARS).execute();

    InsertValuesStep1<WarsRecord, Integer> step = db.insertInto(WARS).columns(WARS.WARID);
    for (Integer newWar : newWars)
      step.values(newWar);
    step.execute();
  }

  public List<Long> getAllActivatedGuildIds() {
    Result<Record1<Long>> fetch = db.select(GUILDCONFIG.ID)
        .from(GUILDCONFIG)
        .where(GUILDCONFIG.ACTIVATED.eq(true))
        .fetch();

    List<Long> result = new ArrayList<>();
    if (fetch != null && fetch.size() != 0) {

      for (Record1<Long> value : fetch)
        result.add(value.value1());

      return result;
    }
    return result;
  }

  public List<Long> getAllGuildIds() {
    Result<Record1<Long>> fetch = db.select(GUILDCONFIG.ID)
        .from(GUILDCONFIG)
        .fetch();

    List<Long> result = new ArrayList<>();
    if (fetch != null && fetch.size() != 0) {

      for (Record1<Long> value : fetch)
        result.add(value.value1());

      return result;
    }
    return result;
  }

  public List<Integer> getApplicants(long id) {
    List<Integer> result = new ArrayList<>();
    result.add(0);
    ApplicantsRecord fetch = db.selectFrom(APPLICANTS).where(APPLICANTS.ID.eq(id)).fetchOne();
    if (fetch != null && fetch.getApplicantids() != null)
      result.addAll(Arrays.asList(fetch.getApplicantids()));
    return result;
  }

  public void updateApplicants(List<Integer> list, long id) {
    db.delete(APPLICANTS).execute();
    db.insertInto(APPLICANTS)
        .columns(APPLICANTS.ID, APPLICANTS.APPLICANTIDS)
        .values(id, list.toArray(new Integer[0]))
        .execute();
  }


  public void close() throws SQLException {
    conn.close();
  }
}
