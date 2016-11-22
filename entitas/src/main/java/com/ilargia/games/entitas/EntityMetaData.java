package com.ilargia.games.entitas;


public class EntityMetaData {

    public String poolName;
    public String[] componentNames;
    public Class[] componentTypes;

    public EntityMetaData(String poolName, String[] componentNames, Class[] componentTypes) {
        this.poolName = poolName;
        this.componentNames = componentNames;
        this.componentTypes = componentTypes;

    }
}
