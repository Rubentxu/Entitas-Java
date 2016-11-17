package com.ilargia.games.entitas;


import com.ilargia.games.entitas.interfaces.FactoryEntity;

public class Pools {

    static Pools _sharedInstance;

    public static Pool createPool(String poolName, int totalComponents, String[] componentNames
            , Class[] componentTypes, FactoryEntity factoryMethod ) {
        Pool pool = new Pool(totalComponents, 0, new PoolMetaData(poolName, componentNames, componentTypes), factoryMethod);

        return pool;
    }

    public static Pools getSharedInstance() {
        if (_sharedInstance == null) {
            _sharedInstance = new Pools();
        }
        return _sharedInstance;
    }

    public static void setSharedInstance(Pools pools) {
        _sharedInstance = pools;
    }

}
