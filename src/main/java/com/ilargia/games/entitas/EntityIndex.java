package com.ilargia.games.entitas;


import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.interfaces.IComponent;

public class EntityIndex<T> extends AbstractEntityIndex<T> {

    private ObjectMap<T, ObjectSet<Entity>> _index;

    protected EntityIndex(Group group, Func<Entity, IComponent, T> key) throws EntityIndexException {
        super(group, key);
        _index = new ObjectMap<T, ObjectSet<Entity>>();
        activate();
    }

    @Override
    public void activate() throws EntityIndexException {
        super.activate();
        indexEntities(_group);
    }

    public ObjectSet<Entity> getEntities(T key) {
        ObjectSet<Entity> entities = null;
        if (!_index.containsKey(key)) {
            entities = new ObjectSet<Entity>();
            _index.put(key, entities);
        } else {
            entities = _index.get(key);
        }
        return entities;
    }


    @Override
    protected void addEntity(Entity entity, IComponent component) throws EntityIndexException {
        getEntities(_key.getKey(entity, component)).add(entity);
        entity.retain(this);
    }

    @Override
    protected void removeEntity(Entity entity, IComponent component) {
        getEntities(_key.getKey(entity, component)).remove(entity);
        entity.release(this);
    }

    @Override
    protected void clear() {
        for (ObjectSet<Entity> entities : _index.values()) {
            for (Entity entity : entities) {
                entity.release(this);
            }
        }
        _index.clear();

    }
}
