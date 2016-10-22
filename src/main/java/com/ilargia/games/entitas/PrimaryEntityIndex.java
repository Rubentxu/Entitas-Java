package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.ObjectMap;
import com.ilargia.games.entitas.exceptions.EntityIndexException;
import com.ilargia.games.entitas.interfaces.IComponent;

public class PrimaryEntityIndex<T> extends AbstractEntityIndex<T> {

    private ObjectMap<T, Entity> _index;

    protected PrimaryEntityIndex(Group group, Func<Entity, IComponent, T> getKey) throws EntityIndexException {
        super(group, getKey);
        _index = new ObjectMap<T, Entity>();
        activate();
    }

    @Override
    public void activate() throws EntityIndexException {
        super.activate();
        indexEntities(_group);
    }

    public boolean hasEntity(T key) {
        return _index.containsKey(key);
    }

    public Entity getEntity(T key) throws EntityIndexException {
        Entity entity = tryGetEntity(key);
        if (entity == null) {
            throw new EntityIndexException("Entity for key '" + key + "' doesn't exist!",
                    "You should check if an entity with that key exists before getting it."
            );
        }

        return entity;
    }

    public Entity tryGetEntity(T key) {
        Entity entity = null;
        _index.get(key, entity);
        return entity;
    }


    @Override
    protected void addEntity(Entity entity, IComponent component) throws EntityIndexException {
        T key = _key.getKey(entity, component);
        if (_index.containsKey(key)) {
            throw new EntityIndexException("Entity for key '" + key + "' already exists!",
                    "Only one entity for a primary key is allowed.");
        }
        _index.put(key, entity);
        entity.retain(this);

    }


    @Override
    protected void removeEntity(Entity entity, IComponent component) {
        _index.remove(_key.getKey(entity, component));
        entity.release(this);
    }

    @Override
    protected void clear() {
        for (Entity entity : _index.values()) {
            entity.release(this);
        }
        _index.clear();

    }
}
