package com.ilargia.games.entitas;


public class Pools {

    static Pools _sharedInstance;

    public static Pool createPool(String poolName, int totalComponents, String[] componentNames, Class[] componentTypes) {
        Pool pool = new Pool(totalComponents, 0, new PoolMetaData(poolName, componentNames, componentTypes));

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
