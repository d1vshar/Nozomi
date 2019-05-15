package io.github.adorableskullmaster.nozomi.core.database.layer.tables;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.WarmoduleRecord;
import org.jooq.DSLContext;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Warmodule.WARMODULE;

public class WarModule {

  private Long offensiveWarChannel;
  private Long defensiveWarChannel;
  private Boolean autoCounter;
  private Integer[] aaIds;
  private Long id;

  public WarModule(DSLContext db, long id) {
    WarmoduleRecord record = db.selectFrom(WARMODULE)
        .where(WARMODULE.ID.eq(id))
        .fetchOne();
    this.offensiveWarChannel = record.getOffensivewarchannel();
    this.defensiveWarChannel = record.getDefensivewarchannel();
    this.autoCounter = record.getAutocounter();
    this.aaIds = record.getAaids();
    this.id = record.getId();
  }

  public Long getOffensiveWarChannel() {
    return offensiveWarChannel;
  }

  public Long getDefensiveWarChannel() {
    return defensiveWarChannel;
  }

  public Boolean getAutoCounter() {
    return autoCounter;
  }

  public Integer[] getAaIds() {
    return aaIds;
  }

  public Long getId() {
    return id;
  }
}
