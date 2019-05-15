package io.github.adorableskullmaster.nozomi.core.database.layer.tables;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.TextsmoduleRecord;
import org.jooq.DSLContext;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Textsmodule.TEXTSMODULE;

public class TextModule {

  private String join;
  private String joinImage;
  private String leave;
  private String leaveImage;
  private Long textChannel;
  private Long id;

  public TextModule(DSLContext db, long id) {
    TextsmoduleRecord record = db.selectFrom(TEXTSMODULE)
        .where(TEXTSMODULE.ID.eq(id))
        .fetchOne();

    this.join = record.getJoin();
    this.joinImage = record.getJoinimage();
    this.leave = record.getLeave();
    this.leaveImage = record.getLeaveimage();
    this.textChannel = record.getTextschannel();
    this.id = record.getId();
  }

  public String getJoin() {
    return join;
  }

  public String getJoinImage() {
    return joinImage;
  }

  public String getLeave() {
    return leave;
  }

  public String getLeaveImage() {
    return leaveImage;
  }

  public long getId() {
    return id;
  }

  public Long getTextChannel() {
    return textChannel;
  }
}
