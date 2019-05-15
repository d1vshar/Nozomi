package io.github.adorableskullmaster.nozomi.core.database.layer.tables;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.BankmoduleRecord;
import org.jooq.DSLContext;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Bankmodule.BANKMODULE;

public class BankModule {

  private Long bankNotificationChannel;
  private String resources;
  private String apiKey;
  private Long id;

  public BankModule(DSLContext db, Long id) {
    BankmoduleRecord record = db.selectFrom(BANKMODULE)
        .where(BANKMODULE.ID.eq(id))
        .fetchOne();

    this.bankNotificationChannel = record.getBanknotificationchannel();
    this.resources = record.getResources();
    this.apiKey = record.getApikey();
    this.id = record.getId();
  }

  public Long getBankNotificationChannel() {
    return bankNotificationChannel;
  }

  public String getResources() {
    return resources;
  }

  public String getApiKey() {
    return apiKey;
  }

  public Long getId() {
    return id;
  }
}
