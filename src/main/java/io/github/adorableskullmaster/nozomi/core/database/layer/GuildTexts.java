package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guilds;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Texts;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.Record3;

public class GuildTexts {

  private boolean join;
  private boolean leave;
  private String joinText;
  private String leaveText;
  private String joinImage;

  GuildTexts(DSLContext db, long id) {

    Record2<Boolean, Boolean> booleans = db.select(Guilds.GUILDS.JOINTEXTS, Guilds.GUILDS.LEAVETEXTS)
        .from(Guilds.GUILDS)
        .where(Guilds.GUILDS.ID.eq(id))
        .fetchOne();
    Record3<String, String, String> texts = db.select(Texts.TEXTS.JOIN, Texts.TEXTS.JOINIMG, Texts.TEXTS.LEAVE)
        .from(Texts.TEXTS)
        .where(Texts.TEXTS.ID.eq(id))
        .fetchOne();

    this.join = booleans.value1();
    this.leave = booleans.value2();
    this.joinText = texts.value1();
    this.joinImage = texts.value2();
    this.leaveText = texts.component3();
  }

  public boolean isJoin() {
    return join;
  }

  public boolean isLeave() {
    return leave;
  }

  public String getJoinText() {
    return joinText;
  }

  public String getLeaveText() {
    return leaveText;
  }

  public String getJoinImage() {
    return joinImage;
  }
}
