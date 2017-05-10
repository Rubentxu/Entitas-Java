package com.ilargia.games.entitas.codeGeneration.plugins.data;


import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class MemberData {

    public Type<JavaClassSource> type;
    public String name;

    public MemberData(Type<JavaClassSource> type, String name) {
        this.type = type;
        this.name = name;
    }
}
