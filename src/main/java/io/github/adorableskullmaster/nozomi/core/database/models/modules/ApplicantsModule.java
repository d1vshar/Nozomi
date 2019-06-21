package io.github.adorableskullmaster.nozomi.core.database.models.modules;

public class ApplicantsModule {
  private Long applicantsChannel;
  private Integer[] pwIds;

  public ApplicantsModule() {
  }

  public ApplicantsModule(Long applicantsChannel, Integer[] pwIds) {
    this.applicantsChannel = applicantsChannel;
    this.pwIds = pwIds;
  }

  public Long getApplicantsChannel() {
    return applicantsChannel;
  }

  public void setApplicantsChannel(Long applicantsChannel) {
    this.applicantsChannel = applicantsChannel;
  }

  public Integer[] getPwIds() {
    return pwIds;
  }

  public void setPwIds(Integer[] pwIds) {
    this.pwIds = pwIds;
  }
}
