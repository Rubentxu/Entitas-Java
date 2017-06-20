package ilargia.entitas.codeGeneration.plugins.dataProviders.components;

import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class ComponentData extends CodeGeneratorData {
    private final JavaClassSource source;
    private String subDir;

    public ComponentData(JavaClassSource source, String subDir) {
        this.source = source;
        this.subDir = subDir;
    }

    public JavaClassSource getSource() {
        return source;
    }

    public String getSubDir() {
        return subDir;
    }

    @Override
    public String toString() {
        return "ComponentData{" +
                "source=" + source.getName() +
                ", subDir='" + subDir + '\'' +
                '}';
    }
}
