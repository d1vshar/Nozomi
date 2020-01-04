package io.github.adorableskullmaster.nozomi.core.database.models;

import java.util.Objects;

public class WarTrackedEntity {
    private Boolean nation;
    private Integer id;

    public WarTrackedEntity(Boolean nation, Integer id) {
        this.nation = nation;
        this.id = id;
    }

    public Boolean getNation() {
        return nation;
    }

    public void setNation(Boolean nation) {
        this.nation = nation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "WarTrackedEntity{" +
                "nation=" + nation +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WarTrackedEntity that = (WarTrackedEntity) o;
        return Objects.equals(nation, that.nation) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nation, id);
    }
}
