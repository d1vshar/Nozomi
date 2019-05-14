package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guilds;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.GuildsRecord;
import org.jooq.DSLContext;

public class Guild {

  private final Long id;
  private final DSLContext db;
  private final int pwId;
  private final String pwKey;
  private final Boolean joinTexts;
  private final Boolean leaveTexts;
  private final Boolean warNotifier;
  private final Boolean applicantNotifier;
  private final Boolean bankNotifier;
  private final Boolean nationTracker;
  private final Boolean vmBeigeTracker;
  private final Integer nationScoreFilter;
  private final Boolean autoCounter;
  private final Long memberRole;
  private final Boolean setup;

  public Guild(DSLContext db, Long id) {
    this.id = id;
    this.db = db;
    GuildsRecord record = db.selectFrom(Guilds.GUILDS)
        .where(Guilds.GUILDS.ID.eq(id))
        .fetchOne();

    pwId = record.getPwid();
    pwKey = record.getPwkey();
    joinTexts = record.getJointexts();
    leaveTexts = record.getLeavetexts();
    warNotifier = record.getWarnotifier();
    applicantNotifier = record.getApplicantnotifier();
    bankNotifier = record.getBanknotifier();
    nationTracker = record.getNationtracker();
    vmBeigeTracker = record.getVmbeigetracker();
    nationScoreFilter = record.getNationscorefilter();
    autoCounter = record.getAutocounter();
    memberRole = record.getMemberrole();
    setup = record.getSetup();
  }

  public GuildTexts getGuildTexts() {
    return new GuildTexts(db, id);
  }

  public GuildApplicants getGuildApplicants() {
    return new GuildApplicants(db, id);
  }

  public GuildChannels getGuildChannels() {
    return new GuildChannels(db, id);
  }

  public int getPwId() {
    return pwId;
  }

  public String getPwKey() {
    return pwKey;
  }

  public Long getId() {
    return id;
  }

  public Boolean isJoinTexts() {
    return joinTexts;
  }

  public Boolean isLeaveTexts() {
    return leaveTexts;
  }

  public Boolean isWarNotifier() {
    return warNotifier;
  }

  public Boolean isApplicantNotifier() {
    return applicantNotifier;
  }

  public Boolean isBankNotifier() {
    return bankNotifier;
  }

  public Boolean isNationTracker() {
    return nationTracker;
  }

  public Boolean isVmBeigeTracker() {
    return vmBeigeTracker;
  }

  public Boolean isAutoCounter() {
    return autoCounter;
  }

  public Long getMemberRole() {
    return memberRole;
  }

  public Boolean isSetup() {
    return setup;
  }

  public Integer getNationScoreFilter() {
    return nationScoreFilter;
  }
}
