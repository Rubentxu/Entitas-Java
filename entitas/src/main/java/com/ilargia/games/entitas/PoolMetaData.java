package com.ilargia.games.entitas;


public class PoolMetaData {

    public String poolName;
    public String[] componentNames;
    public Class[] componentTypes;

    public PoolMetaData(String poolName, String[] componentNames, Class[] componentTypes) {
        this.poolName = poolName;
        this.componentNames = componentNames;
        this.componentTypes = componentTypes;
    }
}
