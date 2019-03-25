package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guilds;
import org.jooq.DSLContext;
import org.jooq.Record2;

public class Guild {

  private final long id;
  private final DSLContext db;
  private final int pwId;
  private final String pwKey;

  public Guild(DSLContext db, long id) {
    this.id = id;
    this.db = db;
    Record2<Integer, String> record = db.select(Guilds.GUILDS.PWID, Guilds.GUILDS.PWKEY)
        .from(Guilds.GUILDS)
        .where(Guilds.GUILDS.ID.eq(this.id))
        .fetchOne();
    this.pwId = record.value1();
    this.pwKey = record.value2();
  }

  public GuildTexts getGuildTexts() {
    return new GuildTexts(db, id);
  }

  public int getPwId() {
    return pwId;
  }

  public String getPwKey() {
    return pwKey;
  }
}
