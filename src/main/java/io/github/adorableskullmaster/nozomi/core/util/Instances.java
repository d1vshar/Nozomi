package io.github.adorableskullmaster.nozomi.core.util;

import io.github.adorableskullmaster.nozomi.Bot;
import io.github.adorableskullmaster.pw4j.PoliticsAndWar;
import io.github.adorableskullmaster.pw4j.PoliticsAndWarBuilder;

public class Instances {

    public static PoliticsAndWar getDefaultPW() {
        return new PoliticsAndWarBuilder()
                .setApiKey(Bot.staticConfiguration.getMasterPWKey())
                .setEnableCache(true, 10, 300000)
                .build();
    }

}
