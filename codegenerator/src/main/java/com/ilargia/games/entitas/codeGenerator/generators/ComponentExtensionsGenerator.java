package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ComponentExtensionsGenerator implements IComponentCodeGenerator {

    @Override
    public CodeGenFile[] generate(ComponentInfo[] componentInfos, String pkgDestiny) {
        String generatorName = this.getClass().getName();

        return (CodeGenFile[]) ((List) Arrays.stream(componentInfos)
                .filter(info -> info.generateMethods)
                .map(info -> new CodeGenFile(info.fullTypeName + "GeneratedExtension",
                        generateComponentExtension(info),
                        generatorName))
                .collect(Collectors.toList()))
                .toArray(new CodeGenFile[0]);


    }

    private JavaClassSource generateComponentExtension(ComponentInfo info) {
        return null;
    }


}
