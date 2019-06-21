package io.github.adorableskullmaster.nozomi.core.database.models.modules;

public class VacModeModule {
  private Integer scoreFilter;
  private Integer[] allianceFilter;

  public VacModeModule() {
  }

  public VacModeModule(Integer scoreFilter, Integer[] allianceFilter) {
    this.scoreFilter = scoreFilter;
    this.allianceFilter = allianceFilter;
  }

  public Integer getScoreFilter() {
    return scoreFilter;
  }

  public void setScoreFilter(Integer scoreFilter) {
    this.scoreFilter = scoreFilter;
  }

  public Integer[] getAllianceFilter() {
    return allianceFilter;
  }

  public void setAllianceFilter(Integer[] allianceFilter) {
    this.allianceFilter = allianceFilter;
  }
}
