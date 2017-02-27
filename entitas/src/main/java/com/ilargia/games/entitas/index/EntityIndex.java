package com.ilargia.games.entitas.index;


import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.IGroup;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.factories.CollectionFactories;

import java.util.Map;
import java.util.Set;

public class EntityIndex<TEntity extends Entity, TKey> extends AbstractEntityIndex<TEntity, TKey> {

    private Map<TKey, Set<TEntity>> _index; // Object2ObjectArrayMap<ObjectOpenHashSet

    public EntityIndex( Func<TEntity, IComponent, TKey> key, IGroup<TEntity> group) {
        super(key, group);
        _index = CollectionFactories.createMap(Object.class, Entity.class); //Object2ObjectArrayMap
        activate();
    }


    public EntityIndex(IGroup<TEntity> group, Func<TEntity, IComponent, TKey[]> keys)  {
        super(group, keys);
        _index = CollectionFactories.createMap(Object.class, Entity.class); //Object2ObjectArrayMap
        activate();
    }
    @Override
    public void activate() throws EntityIndexException {
        super.activate();
        indexEntities(_group);
    }

    public Set<TEntity> getEntities(TKey key) {
        if (!_index.containsKey(key)) {
            Set<TEntity> entities = CollectionFactories.createSet(Entity.class);
            _index.put(key, entities);
            return entities;
        }
        return _index.get(key);

    }

    @Override
    protected void clear() {
        for (Set<TEntity> entities : _index.values()) {
            for (IEntity entity : entities) {
                if(entity.owners().contains(this)) {
                    entity.release(this);
                }
            }
        }
        _index.clear();

    }

    @Override
    protected void addEntity(TKey tKey, TEntity entity) {
        getEntities(tKey).add(entity);
        if(!entity.owners().contains(this)) {
            entity.retain(this);
        }
    }

    @Override
    protected void removeEntity(TKey tKey, TEntity entity) {
        getEntities(tKey).remove(entity);
        if(entity.owners().contains(this)) {
            entity.release(this);
        }
    }


}
