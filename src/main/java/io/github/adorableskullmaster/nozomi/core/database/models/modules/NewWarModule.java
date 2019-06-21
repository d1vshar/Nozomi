package io.github.adorableskullmaster.nozomi.core.database.models.modules;

public class NewWarModule {
  private Integer[] pwIds;
  private Long offChannel;
  private Long defChannel;
  private Boolean autoCounter;

  public NewWarModule() {
  }

  public NewWarModule(Integer[] pwIds, Long offChannel, Long defChannel, Boolean autoCounter) {
    this.pwIds = pwIds;
    this.offChannel = offChannel;
    this.defChannel = defChannel;
    this.autoCounter = autoCounter;
  }

  public Integer[] getPwIds() {
    return pwIds;
  }

  public void setPwIds(Integer[] pwIds) {
    this.pwIds = pwIds;
  }

  public Long getOffChannel() {
    return offChannel;
  }

  public void setOffChannel(Long offChannel) {
    this.offChannel = offChannel;
  }

  public Long getDefChannel() {
    return defChannel;
  }

  public void setDefChannel(Long defChannel) {
    this.defChannel = defChannel;
  }

  public Boolean getAutoCounter() {
    return autoCounter;
  }

  public void setAutoCounter(Boolean autoCounter) {
    this.autoCounter = autoCounter;
  }
}
