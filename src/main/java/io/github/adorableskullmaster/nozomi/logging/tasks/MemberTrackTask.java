package io.github.adorableskullmaster.nozomi.logging.tasks;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.ConfigurationDataSource;
import io.github.adorableskullmaster.nozomi.core.database.PlayerDataSource;
import io.github.adorableskullmaster.nozomi.core.database.models.Player;
import io.github.adorableskullmaster.nozomi.logging.LogContext;
import io.github.adorableskullmaster.nozomi.logging.LogType;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Optional;

public class MemberTrackTask implements Runnable {
    @Override
    public void run() {
        List<Player> allPlayerData = PlayerDataSource.getAllPlayerData();
        long serverId = Bot.staticConfiguration.getServerId();
        int pwId = Bot.staticConfiguration.getPWId();
        Long memberRole = ConfigurationDataSource.getConfiguration().getMemberRole();

        updateAllFromAPI(allPlayerData);

        for (Player player : allPlayerData) {
            check(player, pwId, serverId, memberRole);
        }
    }

    // TODO applicant condition(s)
    private void check(Player player, int pwId, long serverId, Long memberRole) {
        Guild guildById = Bot.jda.getGuildById(serverId);
        assert guildById != null;
        Member memberById = guildById.getMemberById(player.getDiscordId());

        if (memberById != null) {
            if (memberById.getRoles().stream().anyMatch(role -> role.getIdLong() == memberRole)) {
                if (player.getAllianceId() != pwId) {
                    Bot.BOT_LOGGER.log(new LogContext(LogType.REMOVE, "%s is no more in our alliance. Unmasking them as member/applicant."));
                } else if (player.getAllianceId() == pwId && !player.isMember()) {
                    Bot.BOT_LOGGER.log(new LogContext(LogType.CHANGE, "%s has been demoted to applicant in-game. Masking as applicant instead of member."));
                }
            }
        }

    }

    private void updateAllFromAPI(List<Player> allPlayerData) {

        List<SNationContainer> nationsContainer = Bot.CACHE.getNations().getNationsContainer();

        for (Player player : allPlayerData) {
            Optional<SNationContainer> result = nationsContainer.stream().filter(sNationContainer -> sNationContainer.getNationId() == player.getNationId()).findFirst();
            if (result.isEmpty())
                PlayerDataSource.deletePlayerData(player.getDiscordId());
            else {
                SNationContainer sNationContainer = result.get();
                PlayerDataSource.insertPlayerData(
                        new Player(
                                player.getDiscordId(),
                                player.getNationId(),
                                sNationContainer.getAllianceposition() > 1,
                                sNationContainer.getAllianceid()
                        )
                );
            }
        }
    }
}
