package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.AllianceProfileRepository;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.AllianceProfile;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.Bank;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarAPIException;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;
import io.github.adorableskullmaster.pw4j.domains.subdomains.AllianceBankContainer;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.List;

public class BankCheckService implements Runnable {

    private AllianceProfileRepository allianceProfileRepository;

  public BankCheckService() {
      allianceProfileRepository = new AllianceProfileRepository();
  }

  @Override
  public void run() {
    try {
      Bot.LOGGER.info("Starting Bank Thread");
        List<AllianceProfile> allAllianceProfiles = allianceProfileRepository.findAllAllianceProfiles();
        for (AllianceProfile allianceProfile : allAllianceProfiles) {
            if (allianceProfile.getBankModule() != null &&
                    isBankNotEmpty(
                            allianceProfile.getAaId(),
                            allianceProfile.getBankModule().getGovAPIKey(),
                            allianceProfile.getBankModule().getResourceLimits()
                    )
            ) {
                TextChannel channel = Bot.jda.getTextChannelById(allianceProfile.getBankModule().getBroadcastChannel());
          MessageHistory history = channel.getHistory();
                history.retrievePast(25).queue(
                        c -> {
                            for (Message message : c) {
                                if (message.getAuthor().getIdLong() == Bot.jda.getSelfUser().getIdLong()) {
                                    if (message.getContentDisplay().contains("Bank"))
                                        message.delete().queue();
                                }
                            }
                        }
          );
                Bot.jda.getGuildById(allianceProfile.getServerId())
                        .getTextChannelById(allianceProfile.getBankModule().getBroadcastChannel())
                        .sendMessage(":moneybag: Bank is currently not empty! <https://politicsandwar.com/alliance/id=" + allianceProfile.getAaId() + "&display=bank>")
                        .queue();
        }
      }
    } catch (Throwable e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

    private boolean isBankNotEmpty(int pwId, String key, Bank.Resources resources) throws PoliticsAndWarAPIException {

    PoliticsAndWar politicsAndWar = new PoliticsAndWarBuilder()
            .setApiKey(key)
        .build();

        AllianceBankContainer allianceBankContent = politicsAndWar.getBank(pwId).getAllianceBanks().get(0);
        return !(allianceBankContent.getMoney() <= resources.getMoney()) ||
                !(allianceBankContent.getBauxite() <= resources.getBauxite()) ||
                !(allianceBankContent.getFood() <= resources.getFood()) ||
                !(allianceBankContent.getCoal() <= resources.getCoal()) ||
                !(allianceBankContent.getIron() <= resources.getIron()) ||
                !(allianceBankContent.getLead() <= resources.getLead()) ||
                !(allianceBankContent.getGasoline() <= resources.getGasoline()) ||
                !(allianceBankContent.getOil() <= resources.getOil()) ||
                !(allianceBankContent.getMunitions() <= resources.getMunitions()) ||
                !(allianceBankContent.getAluminum() <= resources.getAluminum()) ||
                !(allianceBankContent.getUranium() <= resources.getUranium()) ||
                !(allianceBankContent.getSteel() <= resources.getSteel());
  }

}
