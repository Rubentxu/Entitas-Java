package ilargia.entitas.codeGeneration.plugins.dataProviders.components;

import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public class ComponentData extends CodeGeneratorData {
    private final JavaClassSource source;
    private final String subDir;

    public ComponentData(JavaClassSource source, String subDir) {
        this.source = source;
        this.subDir = subDir;
    }

    public String toComponentName(String fullTypeName, boolean ignoreNamespaces) {
        try {
            return ignoreNamespaces
                    ? Class.forName(fullTypeName).getName()
                    : fullTypeName;
        } catch (ClassNotFoundException e) {
            System.out.println("Error to component name");

        }
        return fullTypeName;
    }

    public JavaClassSource getSource() {
        return source;
    }

    public String getSubDir() {
        return subDir;
    }

}
