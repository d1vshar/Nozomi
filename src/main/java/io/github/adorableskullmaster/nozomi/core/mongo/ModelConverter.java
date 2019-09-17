package io.github.adorableskullmaster.nozomi.core.mongo;

import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.AllianceProfile;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.DiscordServer;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance.BankModule;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance.NewApplicantModule;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.alliance.NewWarModule;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.server.TextModule;
import io.github.adorableskullmaster.nozomi.core.mongo.bridge.model.modules.server.VacModeModule;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.Alliance;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.Server;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.Bank;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.NewApplicant;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.alliance.NewWar;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.server.Text;
import io.github.adorableskullmaster.nozomi.core.mongo.morphia.modules.server.VacMode;

import java.util.List;
import java.util.stream.Collectors;

public class ModelConverter {

    public static DiscordServer convertIntoDiscordServer(Server server) {
        DiscordServer discordServer = new DiscordServer();
        discordServer.setServerId(server.getServerId());
        discordServer.setTextModule(convertIntoTextModule(server.getText()));
        discordServer.setMemberId(server.getMemberId());
        discordServer.setVacModeModule(convertIntoVacModeModule(server.getVacMode()));
        discordServer.setAllianceProfiles(server.getAlliances().stream().map(ModelConverter::convertIntoAllianceProfile).collect(Collectors.toList()));
        return discordServer;
    }

    public static List<DiscordServer> convertIntoDiscordServerList(List<Server> server) {
        return server.stream().map(ModelConverter::convertIntoDiscordServer).collect(Collectors.toList());
    }

    public static AllianceProfile convertIntoAllianceProfile(Alliance alliance) {
        AllianceProfile allianceProfile = new AllianceProfile();
        allianceProfile.setAaId(alliance.getAaId());
        allianceProfile.setServerId(alliance.getServerId());
        allianceProfile.setBankModule(convertIntoBankModule(alliance.getBank()));
        allianceProfile.setNewApplicantModule(convertIntoNewApplicantModule(alliance.getNewApplicant()));
        allianceProfile.setNewWarModule(convertIntoNewWarModule(alliance.getNewWar()));
        return allianceProfile;
    }

    public static List<AllianceProfile> convertIntoAllianceProfileList(List<Alliance> alliance) {
        return alliance.stream().map(ModelConverter::convertIntoAllianceProfile).collect(Collectors.toList());
    }

    public static Alliance convertIntoAlliance(AllianceProfile allianceProfile) {
        Alliance alliance = new Alliance();
        alliance.setAaId(allianceProfile.getAaId());
        alliance.setServerId(allianceProfile.getServerId());
        alliance.setBank(convertIntoBankMorphia(allianceProfile.getBankModule()));
        alliance.setNewApplicant(convertIntoNewApplicantMorphia(allianceProfile.getNewApplicantModule()));
        alliance.setNewWar(convertIntoNewWarMorphia(allianceProfile.getNewWarModule()));
        return alliance;
    }

    public static TextModule convertIntoTextModule(Text text) {
        TextModule textModule = new TextModule();
        textModule.setBroadcastChannel(text.getChannel());
        textModule.setJoinImage(text.getJoinImage());
        textModule.setLeaveImage(text.getLeaveImage());
        textModule.setJoinText(text.getJoinText());
        textModule.setLeaveText(text.getLeaveText());
        return textModule;
    }

    public static Text convertIntoTextMorphia(TextModule textModule) {
        Text text = new Text();
        text.setChannel(textModule.getBroadcastChannel());
        text.setJoinImage(textModule.getJoinImage());
        text.setLeaveImage(textModule.getLeaveImage());
        text.setJoinText(textModule.getJoinText());
        text.setLeaveText(textModule.getLeaveText());
        return text;
    }

    public static VacModeModule convertIntoVacModeModule(VacMode vacMode) {
        VacModeModule vacModeModule = new VacModeModule();
        vacModeModule.setBroadcastChannel(vacMode.getChannel());
        vacModeModule.setAllianceIds(vacMode.getAllianceIds());
        vacModeModule.setScoreFilter(vacMode.getScoreFilter());
        return vacModeModule;
    }

    public static VacMode convertIntoVacModeMorphia(VacModeModule vacModeModule) {
        VacMode vacMode = new VacMode();
        vacMode.setChannel(vacModeModule.getBroadcastChannel());
        vacMode.setAllianceIds(vacModeModule.getAllianceIds());
        vacMode.setScoreFilter(vacModeModule.getScoreFilter());
        return vacMode;
    }

    public static BankModule convertIntoBankModule(Bank bank) {
        BankModule bankModule = new BankModule();
        bankModule.setBroadcastChannel(bank.getChannel());
        bankModule.setGovAPIKey(bank.getKey());
        bankModule.setResourceLimits(bank.getResources());
        return bankModule;
    }

    public static Bank convertIntoBankMorphia(BankModule bankModule) {
        Bank bank = new Bank();
        bank.setChannel(bankModule.getBroadcastChannel());
        bank.setKey(bankModule.getGovAPIKey());
        bank.setResources(bankModule.getResourceLimits());
        return bank;
    }

    public static NewApplicantModule convertIntoNewApplicantModule(NewApplicant newApplicant) {
        NewApplicantModule newApplicantModule = new NewApplicantModule();
        newApplicantModule.setBroadcastChannel(newApplicant.getChannel());
        return newApplicantModule;
    }

    public static NewApplicant convertIntoNewApplicantMorphia(NewApplicantModule newApplicantModule) {
        NewApplicant newApplicant = new NewApplicant();
        newApplicant.setChannel(newApplicantModule.getBroadcastChannel());
        return newApplicant;
    }

    public static NewWarModule convertIntoNewWarModule(NewWar newWar) {
        NewWarModule newWarModule = new NewWarModule();
        newWarModule.setDefensiveChannel(newWar.getDefensiveChannel());
        newWarModule.setDefensiveChannel(newWar.getOffensiveChannel());
        newWarModule.setCounterSuggestion(newWar.isCounterSuggestion());
        return newWarModule;
    }

    public static NewWar convertIntoNewWarMorphia(NewWarModule newWarModule) {
        NewWar newWar = new NewWar();
        newWar.setDefensiveChannel(newWarModule.getDefensiveChannel());
        newWar.setDefensiveChannel(newWarModule.getOffensiveChannel());
        newWar.setCounterSuggestion(newWarModule.isCounterSuggestion());
        return newWar;
    }

}
