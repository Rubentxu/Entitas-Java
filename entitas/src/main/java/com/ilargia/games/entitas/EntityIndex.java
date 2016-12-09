package com.ilargia.games.entitas;


import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.interfaces.IComponent;

public class EntityIndex<K, E extends Entity> extends AbstractEntityIndex<K, E> {

    private ObjectMap<K, ObjectSet<E>> _index;

    public EntityIndex(Group group, Func<E, IComponent, K> key)  {
        super(group, key);
        _index = new ObjectMap<K, ObjectSet<E>>();
        activate();
    }

    @Override
    public void activate() throws EntityIndexException {
        super.activate();
        indexEntities(_group);
    }

    public  ObjectSet<E> getEntities(K key) {
        ObjectSet<E> entities = null;
        if (!_index.containsKey(key)) {
            entities = new ObjectSet<E>();
            _index.put(key, entities);
        } else {
            entities = _index.get(key);
        }
        return entities;
    }


    @Override
    protected void addEntity(E entity, IComponent component)  {
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
        for (ObjectSet<E> entities : _index.values()) {
            for (Entity entity : entities) {
                entity.release(this);
            }
        }
        _index.clear();

    }
}
