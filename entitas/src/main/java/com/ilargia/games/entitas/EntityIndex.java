package com.ilargia.games.entitas;


import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.interfaces.IComponent;
import java.util.Map;
import java.util.Set;

public class EntityIndex<K, E extends Entity> extends AbstractEntityIndex<K, E> {

    private Map<K, Set<E>> _index; // Object2ObjectArrayMap<ObjectOpenHashSet

    public EntityIndex(Group group, Func<E, IComponent, K> key) {
        super(group, key);
        _index = Collections.createMap(); //Object2ObjectArrayMap
        activate();
    }

    @Override
    public void activate() throws EntityIndexException {
        super.activate();
        indexEntities(_group);
    }

    public Set<E> getEntities(K key) {
        Set<E> entities = null;
        if (!_index.containsKey(key)) {
            entities = Collections.createSet();
            _index.put(key, entities);
        } else {
            entities = _index.get(key);
        }
        return entities;
    }


    @Override
    protected void addEntity(E entity, IComponent component) {
        getEntities(_key.getKey(entity, component)).add(entity);
        entity.retain(this);
    }

    @Override
    protected void removeEntity(E entity, IComponent component) {
        getEntities(_key.getKey(entity, component)).remove(entity);
        entity.release(this);
    }

    @Override
    protected void clear() {
        for (Set<E> entities : _index.values()) {
            for (Entity entity : entities) {
                entity.release(this);
            }
        }
        _index.clear();

    }
}
