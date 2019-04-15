package io.github.adorableskullmaster.nozomi.core.database;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.*;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public class DBSetup {

  private final DSLContext db;

  public DBSetup(DSLContext db) {
    this.db = db;
  }

  public void setupApplicants() {
    db.createTableIfNotExists(Applicants.APPLICANTS)
        .columns(Guilds.GUILDS.fields())
        .constraint(DSL.constraint("applicants_pkey").primaryKey(Applicants.APPLICANTS.ID))
        .execute();
  }

  public void setupChannels() {
    db.createTableIfNotExists(Channels.CHANNELS)
        .columns(Channels.CHANNELS.fields())
        .constraint(DSL.constraint("channel_pkey").primaryKey(Guilds.GUILDS.ID))
        .execute();
  }

  public void setupGuilds() {
    db.createTableIfNotExists(Guilds.GUILDS)
        .columns(Guilds.GUILDS.fields())
        .constraint(DSL.constraint("guilds_pkey").primaryKey(Guilds.GUILDS.ID))
        .execute();
  }

  public void setupTexts() {
    db.createTableIfNotExists(Texts.TEXTS)
        .columns(Texts.TEXTS.fields())
        .constraint(DSL.constraint("texts_pkey").primaryKey(Guilds.GUILDS.ID))
        .execute();
  }

  public void setupWars() {
    db.createTableIfNotExists(Wars.WARS)
        .columns(Wars.WARS.fields())
        .execute();
  }
}
