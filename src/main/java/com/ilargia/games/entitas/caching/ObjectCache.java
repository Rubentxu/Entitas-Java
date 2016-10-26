package com.ilargia.games.entitas.caching;

import com.badlogic.gdx.utils.ObjectMap;

import java.lang.reflect.ParameterizedType;

public class ObjectCache {
    private ObjectMap<Class, ObjectPool> _objectPools;

    public <T> ObjectPool<T> getObjectPool(Class<T> clazz) {
        ObjectPool<T> objectPool = _objectPools.get(clazz);
        if (objectPool == null) {
            objectPool = new ObjectPool<T>();
            _objectPools.put(clazz, objectPool);
        }
        return objectPool;
    }

    public <T> T get() {
        Class<T> clazz = (Class<T>)
                ((ParameterizedType) getClass().getGenericSuperclass())
                        .getActualTypeArguments()[0];
        return getObjectPool(clazz).get(clazz);
    }

    public <T> void push(T obj) {
        ((ObjectPool<T>) _objectPools.get(obj.getClass())).push(obj);
    }

    public <T> void registerCustomObjectPool(ObjectPool<T> objectPool) {
        _objectPools.put(objectPool.getClass(), objectPool);
    }

    public void reset() {
        _objectPools.clear();
    }
}
