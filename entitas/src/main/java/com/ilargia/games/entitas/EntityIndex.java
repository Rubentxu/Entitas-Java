package com.ilargia.games.entitas;


import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.interfaces.IComponent;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

public class EntityIndex<K, E extends Entity> extends AbstractEntityIndex<K, E> {

    private Object2ObjectArrayMap<K, ObjectOpenHashSet<E>> _index;

    public EntityIndex(Group group, Func<E, IComponent, K> key) {
        super(group, key);
        _index = new Object2ObjectArrayMap();
        activate();
    }

    @Override
    public void activate() throws EntityIndexException {
        super.activate();
        indexEntities(_group);
    }

    public ObjectOpenHashSet<E> getEntities(K key) {
        ObjectOpenHashSet<E> entities = null;
        if (!_index.containsKey(key)) {
            entities = new ObjectOpenHashSet<E>();
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
        for (ObjectOpenHashSet<E> entities : _index.values()) {
            for (Entity entity : entities) {
                entity.release(this);
            }
        }
        _index.clear();

    }
}
