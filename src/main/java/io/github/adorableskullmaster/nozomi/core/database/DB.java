package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guilds;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.WarsRecord;
import io.github.adorableskullmaster.nozomi.core.database.layer.Guild;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Applicants.APPLICANTS;
import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Channels.CHANNELS;
import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guilds.GUILDS;
import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Texts.TEXTS;
import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Wars.WARS;

public class DB {

  private Connection conn;
  private DSLContext db;

  public DB() throws SQLException {
    this.conn = Instances.getConnection();
    this.db = DSL.using(conn, SQLDialect.POSTGRES);
  }

  public Guild getGuild(long id) {
    return new Guild(db, id);
  }

  public void initGuild(long id) {
    db.insertInto(GUILDS)
        .columns(GUILDS.ID, GUILDS.BANKNOTIFIER, GUILDS.WARNOTIFIER, GUILDS.JOINTEXTS, GUILDS.LEAVETEXTS,
            GUILDS.APPLICANTNOTIFIER, GUILDS.AUTOCOUNTER, GUILDS.SETUP, GUILDS.NATIONTRACKER, GUILDS.VMBEIGETRACKER)
        .values(id, false, false, false, false, false, false, false, false, false)
        .execute();

    db.insertInto(APPLICANTS)
        .columns(APPLICANTS.ID)
        .values(id)
        .execute();

    db.insertInto(CHANNELS)
        .columns(CHANNELS.ID, CHANNELS.LOGCHANNEL, CHANNELS.MAINCHANNEL)
        .values(id, 0L, 0L)
        .execute();

    db.insertInto(TEXTS)
        .columns(TEXTS.ID)
        .values(id)
        .execute();
  }

  public Result<WarsRecord> getWarsDir() {
    return db.selectFrom(WARS).fetch();
  }

  public int storeWarsDir(List<Integer> newWars) {

    db.delete(WARS).execute();

    InsertValuesStep1<WarsRecord, Integer> step = db.insertInto(WARS).columns(WARS.WARID);
    for (Integer newWar : newWars)
      step.values(newWar);
    return step.execute();
  }

  public List<Long> getAllSetupGuildIds() {
    Result<Record1<Long>> fetch = db.select(Guilds.GUILDS.ID)
        .from(Guilds.GUILDS)
        .where(GUILDS.SETUP.eq(true))
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
    Result<Record1<Long>> fetch = db.select(Guilds.GUILDS.ID)
        .from(Guilds.GUILDS)
        .fetch();

    List<Long> result = new ArrayList<>();
    if (fetch != null && fetch.size() != 0) {

      for (Record1<Long> value : fetch)
        result.add(value.value1());

      return result;
    }
    return result;
  }

  public void close() throws SQLException {
    conn.close();
  }
}
