package io.github.adorableskullmaster.nozomi.core.database.models;

import io.github.adorableskullmaster.nozomi.core.database.models.modules.*;

public class ModuleSettings {
  private NewWarModule newWarModule;
  private VacModeModule vacModeModule;
  private TextModule textModule;
  private ApplicantsModule applicantsModule;
  private BankModule bankModule;

  public ModuleSettings() {
  }

  public ModuleSettings(NewWarModule newWarModule, VacModeModule vacModeModule, TextModule textModule, ApplicantsModule applicantsModule, BankModule bankModule) {
    this.newWarModule = newWarModule;
    this.vacModeModule = vacModeModule;
    this.textModule = textModule;
    this.applicantsModule = applicantsModule;
    this.bankModule = bankModule;
  }

  public NewWarModule getNewWarModule() {
    return newWarModule;
  }

  public void setNewWarModule(NewWarModule newWarModule) {
    this.newWarModule = newWarModule;
  }

  public VacModeModule getVacModeModule() {
    return vacModeModule;
  }

  public void setVacModeModule(VacModeModule vacModeModule) {
    this.vacModeModule = vacModeModule;
  }

  public TextModule getTextModule() {
    return textModule;
  }

  public void setTextModule(TextModule textModule) {
    this.textModule = textModule;
  }

  public ApplicantsModule getApplicantsModule() {
    return applicantsModule;
  }

  public void setApplicantsModule(ApplicantsModule applicantsModule) {
    this.applicantsModule = applicantsModule;
  }

  public BankModule getBankModule() {
    return bankModule;
  }

  public void setBankModule(BankModule bankModule) {
    this.bankModule = bankModule;
  }
}
