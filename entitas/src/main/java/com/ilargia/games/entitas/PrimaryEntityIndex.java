package com.ilargia.games.entitas;

import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.interfaces.IComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;

public class PrimaryEntityIndex<K, E extends Entity> extends AbstractEntityIndex<K, E> {

    private Object2ObjectArrayMap<K, E> _index;

    public PrimaryEntityIndex(Group group, Func<E, IComponent, K> getKey) {
        super(group, getKey);
        _index = new Object2ObjectArrayMap<K, E>();
        activate();
    }

    @Override
    public void activate() {
        super.activate();
        indexEntities(_group);
    }

    public boolean hasEntity(K key) {
        return _index.containsKey(key);
    }

    public E getEntity(K key) {
        E entity = tryGetEntity(key);
        if (entity == null) {
            throw new EntityIndexException("SplashEntity for key '" + key + "' doesn't exist!",
                    "You should check if an entity with that key exists before getting it."
            );
        }

        return entity;
    }

    public E tryGetEntity(K key) {
        E entity = null;
        _index.get(key);
        return entity;
    }


    @Override
    protected void addEntity(E entity, IComponent component) {
        K key = _key.getKey(entity, component);
        if (_index.containsKey(key)) {
            throw new EntityIndexException("SplashEntity for key '" + key + "' already exists!",
                    "Only one entity for a primary key is allowed.");
        }
        _index.put(key, entity);
        entity.retain(this);

    }


    @Override
    protected void removeEntity(E entity, IComponent component) {
        _index.remove(_key.getKey(entity, component));
        entity.release(this);
    }

    @Override
    protected void clear() {
        for (E entity : _index.values()) {
            entity.release(this);
        }
        _index.clear();

    }
}
