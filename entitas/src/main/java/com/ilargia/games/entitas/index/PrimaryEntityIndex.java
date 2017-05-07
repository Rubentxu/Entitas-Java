package com.ilargia.games.entitas.index;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.SafeAERC;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class PrimaryEntityIndex<TEntity extends Entity, TKey> extends AbstractEntityIndex<TEntity, TKey> {

    private Map<TKey, TEntity> _index; //Object2ObjectArrayMap


    public PrimaryEntityIndex(String name, Func<TEntity, IComponent, TKey> key, IGroup<TEntity> group) {
        super(name, key, group);
        _index = EntitasCollections.createMap(Object.class, Object.class);
         activate();
    }

    public PrimaryEntityIndex(String name, IGroup<TEntity> group, Func<TEntity, IComponent, TKey[]> keys) {
       super(name, group, keys);
        _index = EntitasCollections.createMap(Object.class, Object.class);
         activate();
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
    public void activate() {
        super.activate();
        indexEntities(_group);
    }

    @Override
    protected void clear() {
        for (TEntity entity :  _index.values()) {
            SafeAERC safeAerc = (SafeAERC) entity.getAERC() ;
            if (safeAerc != null) {
                if (safeAerc.owners().contains(this)) {
                    entity.release(this);
                }
            } else {
                entity.release(this);
            }
        }
        _index.clear();
    }

    @Override
    public void addEntity(TKey key, TEntity entity) {
        if(_index.containsKey(key)) {
            throw new EntityIndexException(
                    "Entity for key '" + key + "' already exists!",
                    "Only one entity for a primary key is allowed.");
        }
        _index.put(key, entity);
        SafeAERC safeAerc = (SafeAERC) entity.getAERC() ;
        if (safeAerc != null) {
            if (!safeAerc.owners().contains(this)) {
                entity.retain(this);
            }
        } else {
            entity.retain(this);
        }

    }

    @Override
    public void removeEntity(TKey key, TEntity entity) {
        _index.remove(key);
        SafeAERC safeAerc = (SafeAERC) entity.getAERC() ;
        if (safeAerc != null) {
            if (safeAerc.owners().contains(this)) {
                entity.release(this);
            }
        } else {
            entity.release(this);
        }

    }

}
