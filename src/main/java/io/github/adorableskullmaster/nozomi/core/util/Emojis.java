package io.github.adorableskullmaster.nozomi.core.util;

public enum Emojis {
  SETTINGS_ACCEPT("<:PAsettingscheckmark:578884559458926602>"),
  ACCEPT("<:PAacceptcircle:578884558615740446>"),
  WARNING("<:PAwarning:578892586329047048>"),
  CANCEL("<:PAcancelcircle:578884559219720202>"),
  ADD("<:PAaddcircle:578884553238642689>"),
  BALLOT("<:PAarchive:578884549212241920>"),
  AWARD("<:PAaward:578884560566091806>"),
  CHECKLIST("<:PAchecklist:578884549929598976>"),
  LETTER_CHECK("<:PAlettercheckbox:578884561849810945>");

  private String mention;

  Emojis(String mention) {
    this.mention = mention;
  }

  public String getAsMention() {
    return mention;
  }
}
