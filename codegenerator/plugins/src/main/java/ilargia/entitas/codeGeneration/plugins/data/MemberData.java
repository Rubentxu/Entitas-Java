package ilargia.entitas.codeGeneration.plugins.data;


import org.jboss.forge.roaster.model.Type;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.lang.annotation.Annotation;

public class MemberData {
    public Class annotation;
    public Class type;
    public String name;

    public MemberData(Class type, String name, Annotation[] annotation) {
        this.type = type;
        this.name = name;
        this.annotation = annotation != null ? annotation[0].annotationType() : null;
    }
}
