package io.github.adorableskullmaster.nozomi.core.database.layer.tables;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.ApplicantmoduleRecord;
import org.jooq.DSLContext;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Applicantmodule.APPLICANTMODULE;

public class ApplicantModule {
  private Long applicantNotificationChannel;
  private Long id;

  public ApplicantModule(DSLContext db, Long id) {
    ApplicantmoduleRecord record = db.selectFrom(APPLICANTMODULE)
        .where(APPLICANTMODULE.ID.eq(id))
        .fetchOne();
    this.applicantNotificationChannel = record.getApplicantnotificationchannel();
    this.id = record.getId();
  }

  public Long getApplicantNotificationChannel() {
    return applicantNotificationChannel;
  }

  public Long getId() {
    return id;
  }
}
