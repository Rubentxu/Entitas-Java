package com.ilargia.games.entitas.index;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntityIndex;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class PrimaryEntityIndex<TEntity extends Entity, TKey> implements IEntityIndex {

    private Map<TKey, TEntity> _index; //Object2ObjectArrayMap

    public PrimaryEntityIndex() {
        _index = EntitasCollections.createMap(Object.class, Object.class);

    }

    public TEntity getEntity(TKey key) {
        TEntity entity =  _index.get(key);
        if (entity == null) {
            throw new EntityIndexException("Entity for key '" + key + "' doesn't exist!",
                    "You should check if an entity with that key exists before getting it."
            );
        }
        return entity;
    }

    @Override
    public void activate() {}

    @Override
    public void deactivate() {
        for (TEntity entity : _index.values()) {
            if(entity.owners().contains(this)) {
                entity.release(this);
            }
        }
        _index.clear();

    }

    public void addEntity(TKey key, TEntity entity) {
        if(_index.containsKey(key)) {
            throw new EntityIndexException(
                    "Entity for key '" + key + "' already exists!",
                    "Only one entity for a primary key is allowed.");
        }
        _index.put(key, entity);
        if(!entity.owners().contains(this)) {
            entity.retain(this);
        }
    }

    public void removeEntity(TKey key, TEntity entity) {
        _index.remove(key);
        if(entity.owners().contains(this)) {
            entity.release(this);
        }
    }

}
