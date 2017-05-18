package com.ilargia.games.entitas.codeGeneration.data;


import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class MemberData {

    public AnnotationSource<JavaClassSource> annotation;
    public Type<JavaClassSource> type;
    public String name;

    public MemberData(Type<JavaClassSource> type, String name, AnnotationSource<JavaClassSource> annotation) {
        this.type = type;
        this.name = name;
        this.annotation = annotation;
    }
}
