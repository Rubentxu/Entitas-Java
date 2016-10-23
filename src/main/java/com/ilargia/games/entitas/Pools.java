package com.ilargia.games.entitas;


import java.lang.reflect.Type;

public class Pools {

    static Pools _sharedInstance;

    public static Pool CreatePool(String poolName,
                                  int totalComponents,
                                  String[] componentNames,
                                  Type[] componentTypes) {
        Pool pool = new Pool(totalComponents, 0, new PoolMetaData(poolName, componentNames, componentTypes));

        return pool;
    }

    public static Pools getSharedInstance() {
        if(_sharedInstance == null) {
            _sharedInstance = new Pools();
        }
        return _sharedInstance;
    }

    public static void setSharedInstance(Pools pools) {
        _sharedInstance = pools;
    }

}
