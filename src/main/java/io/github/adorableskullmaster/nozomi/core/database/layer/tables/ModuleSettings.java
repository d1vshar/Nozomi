package io.github.adorableskullmaster.nozomi.core.database.layer.tables;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.GuildconfigRecord;
import org.jooq.DSLContext;

import static io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guildconfig.GUILDCONFIG;

public class ModuleSettings {
  private Long id;
  private Boolean activated;
  private Integer aaId;
  private Boolean warModule;
  private Boolean applicantModule;
  private Boolean bankModule;
  private Boolean vacModeModule;
  private Boolean textModule;
  private Long memberRole;

  public ModuleSettings(DSLContext db, Long id) {
    GuildconfigRecord record = db.selectFrom(GUILDCONFIG)
        .where(GUILDCONFIG.ID.eq(id))
        .fetchOne();

    this.id = record.getId();
    this.activated = record.getActivated();
    this.aaId = record.getAaid();
    this.warModule = record.getWarmodule();
    this.applicantModule = record.getApplicantmodule();
    this.bankModule = record.getBankmodule();
    this.vacModeModule = record.getVacmodemodule();
    this.textModule = record.getTextmodule();
    this.memberRole = record.getMemberrole();
  }

  public Long getId() {
    return id;
  }

  public Boolean isBotActivated() {
    return activated;
  }

  public Integer getAaId() {
    return aaId;
  }

  public Boolean isWarModuleEnabled() {
    return warModule;
  }

  public Boolean isApplicantModuleEnabled() {
    return applicantModule;
  }

  public Boolean isBankModuleEnabled() {
    return bankModule;
  }

  public Boolean isVacModeModuleEnabled() {
    return vacModeModule;
  }

  public Boolean isTextModuleEnabled() {
    return textModule;
  }

  public Long getMemberRole() {
    return memberRole;
  }
}
