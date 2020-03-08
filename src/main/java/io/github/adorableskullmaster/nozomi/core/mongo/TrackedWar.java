package io.github.adorableskullmaster.nozomi.core.mongo;

public class TrackedWar {
    private Integer warId;

    public TrackedWar(Integer warId) {
        this.warId = warId;
    }

    public Integer getWarId() {
        return warId;
    }

    public void setWarId(Integer warId) {
        this.warId = warId;
    }
}
