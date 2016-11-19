package com.ilargia.games.entitas;


import com.ilargia.games.entitas.interfaces.FactoryEntity;

public class Pools<E extends Entity> {

    public Pool createPool(String poolName, int totalComponents, String[] componentNames
            , Class[] componentTypes, FactoryEntity factoryMethod ) {
        Pool<E> pool = new Pool<E>(totalComponents, 0, new PoolMetaData(poolName, componentNames, componentTypes), factoryMethod);

        return pool;
    }


}
