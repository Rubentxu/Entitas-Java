package com.ilargia.games.entitas;


import java.lang.reflect.Type;

public class PoolMetaData {

    public String poolName;
    public String[] componentNames;
    public Type[] componentTypes;

    public PoolMetaData(String poolName, String[] componentNames, Type[] componentTypes) {
        this.poolName = poolName;
        this.componentNames = componentNames;
        this.componentTypes = componentTypes;
    }
}
