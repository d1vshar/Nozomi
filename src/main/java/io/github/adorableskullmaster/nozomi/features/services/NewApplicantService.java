package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.database.ApplicantsDataSource;
import io.github.adorableskullmaster.nozomi.core.database.ConfigurationDataSource;
import io.github.adorableskullmaster.nozomi.core.util.Emojis;
import io.github.adorableskullmaster.pw4j.domains.subdomains.SNationContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NewApplicantService implements Runnable {

    @Override
    public void run() {
        if (ConfigurationDataSource.isSetup()) {
            try {
                Bot.LOGGER.info("Starting Applicant Thread");

                int pwId = Bot.staticConfiguration.getPWId();
                List<SNationContainer> newApplicants = getNewApplicants(pwId);

                for (SNationContainer applicant : newApplicants) {
                    String msg = Emojis.ADD.getAsMention() + " **New Applicant!** https://politicsandwar.com/alliance/id="
                            + pwId +
                            "\nName: " + applicant.getNation();

                    Bot.jda.getTextChannelById(ConfigurationDataSource.getConfiguration().getGovChannel())
                            .sendMessage(msg)
                            .queue();
                }
            } catch (Throwable e) {
                Bot.BOT_EXCEPTION_HANDLER.captureException(e);
            }
        }
    }

    private List<SNationContainer> getNewApplicants(int pwId) {
        List<SNationContainer> containerApplicants = getCurrentApplicants(pwId);

        List<Integer> currentApplicants = containerApplicants.stream()
                .map(SNationContainer::getNationId)
                .collect(Collectors.toList());
        List<Integer> loadedApplicants = getLoadedApplicants();

        update(currentApplicants);
        List<Integer> diff = getDiff(loadedApplicants, currentApplicants);

        return containerApplicants.stream()
                .filter(nationContainer -> diff.contains(nationContainer.getNationId()))
                .collect(Collectors.toList());
    }

    private List<Integer> getDiff(List<Integer> loaded, List<Integer> current) {
        List<Integer> diff = new ArrayList<>();
        for (Integer id : current) {
            if (!loaded.contains(id)) {
                diff.add(id);
            }
        }
        return diff;
    }

    private List<SNationContainer> getCurrentApplicants(int aid) {
        return Bot.CACHE.getNations()
                .getNationsContainer()
                .stream()
                .filter(nationContainer -> nationContainer.getAllianceid() == aid)
                .filter(nationContainer -> nationContainer.getAllianceposition() == 1)
                .collect(Collectors.toList());
    }

    private List<Integer> getLoadedApplicants() {
        return ApplicantsDataSource.getStoredApplicants();
    }

    private void update(List<Integer> list) {
        ApplicantsDataSource.setStoredApplicants(list);
    }

}
