package com.ilargia.games.entitas.caching;


import com.ilargia.games.entitas.factories.Collections;

import java.util.Map;

public class ObjectCache<V> {
    private Map<Class, ObjectPool<V>> _objectPools;

    public ObjectCache() {
        _objectPools = Collections.createMap(Object.class, Object.class);
    }

    public ObjectPool<V> getObjectPool(Class<V> clazz) {
        ObjectPool<V> objectPool;

        if(!_objectPools.TryGetValue(clazz, out objectPool)) {
            objectPool = new ObjectPool<V>(() => new V());
            _objectPools.put(clazz, objectPool);
        }

        return objectPool;
    }

    public  void Push(V obj) {
        getObjectPool((Class<V>) obj.getClass()).push(obj);
    }

    public void registerCustomObjectPool(ObjectPool<V> objectPool) {
        _objectPools.put(objectPool.getClass().getComponentType(), objectPool);
    }

    public void Reset() {
        _objectPools.clear();
    }
}
}
