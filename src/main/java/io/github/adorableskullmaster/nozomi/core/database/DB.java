package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.WarsRecord;
import io.github.adorableskullmaster.nozomi.core.database.layer.Guild;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Wars.WARS;

public class DB {

  private final DSLContext db;

  public DB() {
    this.db = DSL.using(Bot.pg.getConn(), SQLDialect.POSTGRES);
  }

  public Guild getGuild(long id) {
    return new Guild(db, id);
  }

  public Result<WarsRecord> getWarsDir() {
    DSLContext db = DSL.using(Bot.pg.getConn(), SQLDialect.POSTGRES);
    return db.selectFrom(WARS).fetch();
  }

}
