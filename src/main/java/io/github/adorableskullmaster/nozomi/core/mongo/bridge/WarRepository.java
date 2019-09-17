package io.github.adorableskullmaster.nozomi.core.mongo.bridge;

import dev.morphia.query.Query;
import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.War;

import java.util.List;
import java.util.stream.Collectors;

public class WarRepository {

    public List<Integer> getAllStoredWars() {
        return Bot.DATA_STORE.createQuery(War.class)
                .find()
                .toList()
                .stream()
                .map(War::getWarId)
                .collect(Collectors.toList());
    }

    public void storeWars(List<Integer> wars) {
        Query<War> warQuery = Bot.DATA_STORE.createQuery(War.class);
        Bot.DATA_STORE.delete(warQuery);
        Bot.DATA_STORE.save(
                wars.stream()
                        .map(war -> {
                            War war1 = new War();
                            war1.setWarId(war);
                            return war1;
                        })
                        .collect(Collectors.toList())
        );
    }
}
