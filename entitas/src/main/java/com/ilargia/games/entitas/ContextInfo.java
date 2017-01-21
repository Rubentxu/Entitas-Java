package com.ilargia.games.entitas;


public class ContextInfo {

    public String contextName;
    public String[] componentNames;
    public Class[] componentTypes;

    public ContextInfo(String contextName, String[] componentNames, Class[] componentTypes) {
        this.contextName = contextName;
        this.componentNames = componentNames;
        this.componentTypes = componentTypes;

    }
}
