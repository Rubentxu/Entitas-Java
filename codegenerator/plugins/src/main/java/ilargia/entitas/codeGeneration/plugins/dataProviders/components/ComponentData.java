package ilargia.entitas.codeGeneration.plugins.dataProviders.components;

import ilargia.entitas.codeGeneration.data.CodeGeneratorData;

public class ComponentData extends CodeGeneratorData {

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
}
