package io.github.adorableskullmaster.nozomi.features.services;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.AllianceProfileRepository;

public class NewApplicantService implements Runnable {

    private AllianceProfileRepository allianceProfileRepository;

  @Override
  public void run() {
    try {
      Bot.LOGGER.info("Starting Applicant Thread");

        allianceProfileRepository.findAllAllianceProfiles();

    } catch (Throwable e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }
/*
  private List<SNationContainer> getNewApplicants(long id, int aid) {
    List<SNationContainer> containerApplicants = getCurrentApplicants(aid);

    List<Integer> currentApplicants = containerApplicants.stream()
        .map(SNationContainer::getNationId)
        .collect(Collectors.toList());
    List<Integer> loadedApplicants = getLoadedApplicants(id);

    update(currentApplicants, id);
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

  private List<Integer> getLoadedApplicants(long id) {
    ArrayList<Integer> result = new ArrayList<>();
    try {
      List<Integer> applicants = botDatabase.getApplicants(id);
      if (applicants != null)
        return applicants;
    } catch (DataAccessException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
      result.add(0);
    }
    return result;
  }

  private void update(BotDatabase botDatabase, List<Integer> list, long id) {
    try {
      botDatabase.updateApplicants(list,id);
    } catch (DataAccessException e) {
      Bot.BOT_EXCEPTION_HANDLER.captureException(e);
    }
  }*/

}
