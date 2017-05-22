package ilargia.entitas.index;


import ilargia.entitas.Entity;
import ilargia.entitas.SafeAERC;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.IGroup;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Map;
import java.util.Set;

public class EntityIndex<TEntity extends Entity, TKey> extends AbstractEntityIndex<TEntity, TKey> {

    private Map<TKey, Set<TEntity>> _index; // Object2ObjectArrayMap<ObjectOpenHashSet

    public EntityIndex(String name, Func<TEntity, IComponent, TKey> key, IGroup<TEntity> group) {
        super(name, key, group);
        _index = EntitasCollections.createMap(Object.class, Entity.class); //Object2ObjectArrayMap
        activate();
    }

    public EntityIndex(String name, IGroup<TEntity> group, Func<TEntity, IComponent, TKey[]> keys)  {
        super(name, group, keys);
        _index = EntitasCollections.createMap(Object.class, Entity.class); //Object2ObjectArrayMap
        activate();
    }

    @Override
    public void activate() {
        super.activate();
        indexEntities(_group);
    }

    public Set<TEntity> getEntities(TKey key) {
        if (!_index.containsKey(key)) {
            Set<TEntity> entities = EntitasCollections.createSet(Entity.class);
            _index.put(key, entities);
            return entities;
        }
        return _index.get(key);

    }

    @Override
    public void clear() {
        for (Set<TEntity> entities : _index.values()) {
            for (IEntity entity : entities) {
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
        _index.clear();

    }

    @Override
    public void addEntity(TKey tKey, TEntity entity) {
        getEntities(tKey).add(entity);
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
    public void removeEntity(TKey tKey, TEntity entity) {
        getEntities(tKey).remove(entity);
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
