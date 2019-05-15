package io.github.adorableskullmaster.nozomi.core.database.layer.tables;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.VacmodemoduleRecord;
import org.jooq.DSLContext;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Vacmodemodule.VACMODEMODULE;

public class VacModeModule {

  private Long vmTrackerChannel;
  private Integer scoreFilter;
  private Long id;

  public VacModeModule(DSLContext db, Long id) {
    VacmodemoduleRecord record = db.selectFrom(VACMODEMODULE)
        .where(VACMODEMODULE.ID.eq(id))
        .fetchOne();
    this.vmTrackerChannel = record.getVmtrackerchannel();
    this.scoreFilter = record.getScorefilter();
    this.id = record.getId();
  }

  public Long getVmTrackerChannel() {
    return vmTrackerChannel;
  }

  public Integer getScoreFilter() {
    return scoreFilter;
  }

  public Long getId() {
    return id;
  }
}
