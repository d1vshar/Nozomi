package io.github.adorableskullmaster.nozomi.core.database.models.modules;

public class TextModule {
  private Long textChannel;
  private String joinMessage;
  private String joinImage;
  private String leaveMessage;
  private String leaveImage;

  public TextModule() {
  }

  public TextModule(Long textChannel, String joinMessage, String joinImage, String leaveMessage, String leaveImage) {
    this.textChannel = textChannel;
    this.joinMessage = joinMessage;
    this.joinImage = joinImage;
    this.leaveMessage = leaveMessage;
    this.leaveImage = leaveImage;
  }

  public Long getTextChannel() {
    return textChannel;
  }

  public void setTextChannel(Long textChannel) {
    this.textChannel = textChannel;
  }

  public String getJoinMessage() {
    return joinMessage;
  }

  public void setJoinMessage(String joinMessage) {
    this.joinMessage = joinMessage;
  }

  public String getJoinImage() {
    return joinImage;
  }

  public void setJoinImage(String joinImage) {
    this.joinImage = joinImage;
  }

  public String getLeaveMessage() {
    return leaveMessage;
  }

  public void setLeaveMessage(String leaveMessage) {
    this.leaveMessage = leaveMessage;
  }

  public String getLeaveImage() {
    return leaveImage;
  }

  public void setLeaveImage(String leaveImage) {
    this.leaveImage = leaveImage;
  }
}
