package com.ilargia.games.entitas.index;


import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.factories.Collections;

import java.util.Map;
import java.util.Set;

public class EntityIndex<TEntity extends Entity, TKey> extends AbstractEntityIndex<TEntity, TKey> {

    private Map<TKey, Set<TEntity>> _index; // Object2ObjectArrayMap<ObjectOpenHashSet

    public EntityIndex(IGroup<TEntity> group, Func<TEntity, IComponent, TKey> key) {
        super(group, key);
        _index = Collections.createMap(Object.class, Entity.class); //Object2ObjectArrayMap
        activate();
    }

    @Override
    public void activate() throws EntityIndexException {
        super.activate();
        indexEntities(_group);
    }

    public Set<TEntity> getEntities(TKey key) {
        Set<TEntity> entities = null;
        if (!_index.containsKey(key)) {
            entities = Collections.createSet(Entity.class);
            _index.put(key, entities);
        } else {
            entities = _index.get(key);
        }
        return entities;
    }

    @Override
    protected void clear() {
        for (Set<TEntity> entities : _index.values()) {
            for (IEntity entity : entities) {
                entity.release(this);
            }
        }
        _index.clear();

    }


    @Override
    protected void addEntity(TEntity entity, IComponent component) {
        getEntities(_key.getKey(entity, component)).add(entity);
        entity.retain(this);
    }

    @Override
    protected void removeEntity(TEntity entity, IComponent component) {
        getEntities(_key.getKey(entity, component)).remove(entity);
        entity.release(this);
    }


}
