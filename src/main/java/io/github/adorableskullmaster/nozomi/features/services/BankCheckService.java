package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.layer.BotDatabase;
import io.github.adorableskullmaster.nozomi.core.database.layer.GuildSettings;
import io.github.adorableskullmaster.nozomi.core.database.layer.tables.BankModule;
import io.github.adorableskullmaster.nozomi.core.database.layer.tables.ModuleSettings;
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
  private BotDatabase db;

  public BankCheckService() {
    try {
      db = Instances.getBotDatabaseLayer();
    } catch (SQLException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  @Override
  public void run() {
    try {
      Bot.LOGGER.info("Starting Bank Thread");
      List<Long> allGuildIds = db.getAllActivatedGuildIds();
      for (Long guildId : allGuildIds) {
        GuildSettings guildSettings = db.getGuildSettings(guildId);
        ModuleSettings moduleSettings = guildSettings.getModuleSettings();
        BankModule bankModuleSettings = guildSettings.getBankModuleSettings();

        if (moduleSettings.isBankModuleEnabled() && isBankNotEmpty(moduleSettings.getAaId(),bankModuleSettings.getApiKey())) {
          TextChannel channel = Bot.jda.getTextChannelById(bankModuleSettings.getBankNotificationChannel());
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
          Bot.jda.getGuildById(guildId)
              .getTextChannelById(bankModuleSettings.getBankNotificationChannel())
              .sendMessage(":moneybag: Bank is currently not empty! <https://politicsandwar.com/alliance/id="+moduleSettings.getAaId()+"&display=bank>")
              .queue();
        }
      }
    } catch (Throwable e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }

  private boolean isBankNotEmpty(int aid, String apiKey) throws PoliticsAndWarAPIException {

    PoliticsAndWar politicsAndWar = new PoliticsAndWarBuilder()
        .setApiKey(apiKey)
        .build();

    AllianceBankContainer allianceBankContent = politicsAndWar.getBank(aid).getAllianceBanks().get(0);
    return !(allianceBankContent.getMoney() <= 100000) || !(allianceBankContent.getBauxite() <= 100) || !(allianceBankContent.getFood() <= 1000) ||
        !(allianceBankContent.getCoal() <= 100) || !(allianceBankContent.getIron() <= 100) || !(allianceBankContent.getLead() <= 100) || !(allianceBankContent.getGasoline() <= 100) ||
        !(allianceBankContent.getOil() <= 100) || !(allianceBankContent.getMunitions() <= 100) || !(allianceBankContent.getAluminum() <= 100) || !(allianceBankContent.getUranium() <= 100) ||
        !(allianceBankContent.getSteel() <= 100);
  }

}
