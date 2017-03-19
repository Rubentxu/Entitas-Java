package com.ilargia.games.entitas.index;


import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.*;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;
import java.util.Set;

public class EntityIndex<TEntity extends Entity, TKey> implements IEntityIndex {

    private Map<TKey, Set<TEntity>> _index; // Object2ObjectArrayMap<ObjectOpenHashSet

    public EntityIndex() {
        _index = EntitasCollections.createMap(Object.class, Entity.class); //Object2ObjectArrayMap

    }

    public Set<TEntity> getEntities(TKey key) {
        if (!_index.containsKey(key)) {
            Set<TEntity> entities = EntitasCollections.createSet(Entity.class);
            _index.put(key, entities);
            return entities;
        }
        return _index.get(key);

    }

    public void addEntity(TKey tKey, TEntity entity) {
        getEntities(tKey).add(entity);
        if(!entity.owners().contains(this)) {
            entity.retain(this);
        }
    }

    public void removeEntity(TKey tKey, TEntity entity) {
        getEntities(tKey).remove(entity);
        if(entity.owners().contains(this)) {
            entity.release(this);
        }
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {
        for (Set<TEntity> entities : _index.values()) {
            for (IEntity entity : entities) {
                if(entity.owners().contains(this)) {
                    entity.release(this);
                }
            }
        }
        _index.clear();
    }

}
