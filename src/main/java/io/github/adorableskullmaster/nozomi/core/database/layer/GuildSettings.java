package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.layer.tables.*;
import org.jooq.DSLContext;

public class GuildSettings {
  private Long id;
  private DSLContext db;

  GuildSettings(DSLContext db, long id) {
    this.db = db;
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public ApplicantModule getApplicantModuleSettings() {
    return new ApplicantModule(db,id);
  }

  public BankModule getBankModuleSettings() {
    return new BankModule(db,id);
  }

  public ModuleSettings getModuleSettings() {
    return new ModuleSettings(db,id);
  }

  public TextModule getTextModuleSettings() {
    return new TextModule(db,id);
  }

  public VacModeModule getVacModeModuleSettings() {
    return new VacModeModule(db,id);
  }

  public WarModule getWarModuleSettings() {
    return new WarModule(db,id);
  }
}
