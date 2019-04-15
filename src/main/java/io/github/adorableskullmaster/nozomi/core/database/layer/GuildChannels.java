package io.github.adorableskullmaster.nozomi.core.database.layer;

import io.github.adorableskullmaster.nozomi.core.database.generated.tables.Channels;
import io.github.adorableskullmaster.nozomi.core.database.generated.tables.records.ChannelsRecord;
import org.jooq.DSLContext;

public class GuildChannels {
  private final long offensiveChannel;
  private final long defensiveChannel;
  private final long mainChannel;
  private final long govChannel;
  private final long vmBeigeChannel;
  private final long nationTrackerChannel;
  private final long logChannel;

  GuildChannels(DSLContext db, long id) {
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

  public long getOffensiveChannel() {
    return offensiveChannel;
  }

  public long getDefensiveChannel() {
    return defensiveChannel;
  }

  public long getMainChannel() {
    return mainChannel;
  }

  public long getGovChannel() {
    return govChannel;
  }

  public long getVmBeigeChannel() {
    return vmBeigeChannel;
  }

  public long getNationTrackerChannel() {
    return nationTrackerChannel;
  }

  public long getLogChannel() {
    return logChannel;
  }
}
