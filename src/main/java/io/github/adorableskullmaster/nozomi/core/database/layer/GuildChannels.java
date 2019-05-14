package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Channels;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.ChannelsRecord;
import org.jooq.DSLContext;

public class GuildChannels {
  private final Long offensiveChannel;
  private final Long defensiveChannel;
  private final Long mainChannel;
  private final Long govChannel;
  private final Long vmBeigeChannel;
  private final Long nationTrackerChannel;
  private final Long logChannel;

  GuildChannels(DSLContext db, Long id) {
    ChannelsRecord channelsRecord = db.selectFrom(Channels.CHANNELS)
        .where(Channels.CHANNELS.ID.eq(id))
        .fetchOne();

    offensiveChannel = channelsRecord.getOffensivechannel();
    defensiveChannel = channelsRecord.getDefensivechannel();
    mainChannel = channelsRecord.getMainchannel();
    govChannel = channelsRecord.getGovchannel();
    vmBeigeChannel = channelsRecord.getVmbeigechannel();
    nationTrackerChannel = channelsRecord.getNationtrackerchannel();
    logChannel = channelsRecord.getLogchannel();
  }

  public Long getOffensiveChannel() {
    return offensiveChannel;
  }

  public Long getDefensiveChannel() {
    return defensiveChannel;
  }

  public Long getMainChannel() {
    return mainChannel;
  }

  public Long getGovChannel() {
    return govChannel;
  }

  public Long getVmBeigeChannel() {
    return vmBeigeChannel;
  }

  public Long getNationTrackerChannel() {
    return nationTrackerChannel;
  }

  public Long getLogChannel() {
    return logChannel;
  }
}
