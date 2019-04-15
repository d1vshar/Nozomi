package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guilds;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.GuildsRecord;
import org.jooq.DSLContext;

public class Guild {

  private final long id;
  private final DSLContext db;
  private final int pwId;
  private final String pwKey;
  private final boolean joinTexts;
  private final boolean leaveTexts;
  private final boolean warNotifier;
  private final boolean applicantNotifier;
  private final boolean bankNotifier;
  private final boolean nationTracker;
  private final boolean vmBeigeTracker;
  private final boolean autoCounter;
  private final long memberRole;
  private final boolean setup;

  public Guild(DSLContext db, long id) {
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

  public long getId() {
    return id;
  }

  public boolean isJoinTexts() {
    return joinTexts;
  }

  public boolean isLeaveTexts() {
    return leaveTexts;
  }

  public boolean isWarNotifier() {
    return warNotifier;
  }

  public boolean isApplicantNotifier() {
    return applicantNotifier;
  }

  public boolean isBankNotifier() {
    return bankNotifier;
  }

  public boolean isNationTracker() {
    return nationTracker;
  }

  public boolean isVmBeigeTracker() {
    return vmBeigeTracker;
  }

  public boolean isAutoCounter() {
    return autoCounter;
  }

  public long getMemberRole() {
    return memberRole;
  }

  public boolean isSetup() {
    return setup;
  }
}
