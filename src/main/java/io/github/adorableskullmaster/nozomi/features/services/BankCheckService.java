package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.util.Instances;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarAPIException;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.subdomains.AllianceBankContainer;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;

import java.sql.SQLException;
import java.util.List;

public class BankCheckService implements Runnable {
    private DB db;

    public BankCheckService() {
        try {
            db = Instances.getDBLayer();
        } catch (SQLException e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }

    @Override
    public void run() {
        try {
            Bot.LOGGER.info("Starting Bank Thread");
            List<Long> allGuildIds = db.getAllSetupGuildIds();
            for (Long id : allGuildIds) {
                Guild guild = db.getGuild(id);

                if (guild.isSetup() && guild.isBankNotifier() && isBankNotEmpty(id)) {
                    TextChannel channel = Bot.jda.getTextChannelById(guild.getGuildChannels().getGovChannel());
                    MessageHistory history = channel.getHistory();
                    history.retrievePast(10).queue(
                            c -> {
                                for (Message message : c) {
                                    if (message.getAuthor().getIdLong() == Bot.jda.getSelfUser().getIdLong()) {
                                        if (message.getContentDisplay().contains("Bank"))
                                            message.delete().queue();
                                    }
                                }
                            }
                    );
                    Bot.jda.getGuildById(guild.getId())
                            .getTextChannelById(guild.getGuildChannels().getGovChannel())
                            .sendMessage(":moneybag: Bank is currently not empty! <https://politicsandwar.com/alliance/id=" + guild.getPwId() + "&display=bank>")
                            .queue();
                }
            }
        } catch (Throwable e) {
            Bot.BOT_EXCEPTION_HANDLER.captureException(e);
        }
    }

    private boolean isBankNotEmpty(long id) throws PoliticsAndWarAPIException {

        Guild guild = db.getGuild(id);

        PoliticsAndWar politicsAndWar = new PoliticsAndWarBuilder()
                .setApiKey(guild.getPwKey())
                .build();

        AllianceBankContainer allianceBankContent = politicsAndWar.getBank(guild.getPwId()).getAllianceBanks().get(0);
        return !(allianceBankContent.getMoney() <= 100000) || !(allianceBankContent.getBauxite() <= 100) || !(allianceBankContent.getFood() <= 1000) ||
                !(allianceBankContent.getCoal() <= 100) || !(allianceBankContent.getIron() <= 100) || !(allianceBankContent.getLead() <= 100) || !(allianceBankContent.getGasoline() <= 100) ||
                !(allianceBankContent.getOil() <= 100) || !(allianceBankContent.getMunitions() <= 100) || !(allianceBankContent.getAluminum() <= 100) || !(allianceBankContent.getUranium() <= 100) ||
                !(allianceBankContent.getSteel() <= 100);
    }

}
