package com.ilargia.games.entitas.codeGeneration.plugins.data;


import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public class MethodData {

    public AnnotationSource<JavaClassSource> annotation;
    public Type<JavaClassSource> returnType;
    public String methodName;
    public List<MemberData> parameters;

    public MethodData(Type<JavaClassSource> returnType, String methodName, List<MemberData> parameters, AnnotationSource<JavaClassSource> annotation) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.parameters = parameters;
        this.annotation = annotation;
    }
}
