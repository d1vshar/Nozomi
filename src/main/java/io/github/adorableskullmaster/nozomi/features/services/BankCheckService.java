package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.ConfigurationDataSource;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarAPIException;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.subdomains.AllianceBankContainer;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;

public class BankCheckService implements Runnable {

    @Override
    public void run() {
        if (ConfigurationDataSource.isSetup()) {
            try {
                Bot.LOGGER.info("Starting Bank Thread");
                if (isBankNotEmpty()) {
                    Long govChannel = ConfigurationDataSource.getConfiguration().getGovChannel();
                    int pwId = Bot.staticConfiguration.getPWId();

                    TextChannel channel = Bot.jda.getTextChannelById(govChannel);
                    MessageHistory history = channel.getHistory();
                    history.retrievePast(10).queue(
                            c -> {
                                for (Message message : c) {
                                    if (message.getAuthor().getIdLong() == Bot.jda.getSelfUser().getIdLong()) {
                                        if (message.getContentDisplay().contains("Bank")) {
                                            message.delete().queue();
                                        }
                                    }
                                }
                            }
                    );

                    Bot.jda.getGuildById(pwId)
                            .getTextChannelById(govChannel)
                            .sendMessage(":moneybag: Bank is currently not empty! <https://politicsandwar.com/alliance/id=" + pwId + "&display=bank>")
                            .queue();
                }
            } catch (Throwable e) {
                Bot.BOT_EXCEPTION_HANDLER.captureException(e);
            }
        }
    }

    private boolean isBankNotEmpty() throws PoliticsAndWarAPIException {

        String apiKey = ConfigurationDataSource.getConfiguration().getApiKeys()[0];

        PoliticsAndWar politicsAndWar = new PoliticsAndWarBuilder()
                .setApiKey(apiKey)
                .build();

        AllianceBankContainer allianceBankContent = politicsAndWar.getBank(Bot.staticConfiguration.getPWId()).getAllianceBanks().get(0);
        return !(allianceBankContent.getMoney() <= 100000) || !(allianceBankContent.getBauxite() <= 100) || !(allianceBankContent.getFood() <= 1000) ||
                !(allianceBankContent.getCoal() <= 100) || !(allianceBankContent.getIron() <= 100) || !(allianceBankContent.getLead() <= 100) || !(allianceBankContent.getGasoline() <= 100) ||
                !(allianceBankContent.getOil() <= 100) || !(allianceBankContent.getMunitions() <= 100) || !(allianceBankContent.getAluminum() <= 100) || !(allianceBankContent.getUranium() <= 100) ||
                !(allianceBankContent.getSteel() <= 100);
    }

}
