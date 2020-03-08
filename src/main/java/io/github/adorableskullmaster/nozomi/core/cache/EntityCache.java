package io.github.adorableskullmaster.nozomi.core.cache;

class EntityCache<K> {
    private K entity;

    K getEntity() {
        return entity;
    }

    void updateEntity(K entity) {
        this.entity = entity;
    }
}
