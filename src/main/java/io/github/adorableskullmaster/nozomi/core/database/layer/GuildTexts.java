package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Texts;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.TextsRecord;
import org.jooq.DSLContext;

public class GuildTexts {

  private boolean join;
  private boolean leave;
  private String joinText;
  private String leaveText;
  private String joinImage;

  GuildTexts(DSLContext db, long id) {

    TextsRecord texts = db.selectFrom(Texts.TEXTS)
        .where(Texts.TEXTS.ID.eq(id))
        .fetchOne();

    this.joinText = texts.getJoin();
    this.joinImage = texts.getJoinimg();
    this.leaveText = texts.getLeave();
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
