package com.ilargia.games.entitas.codeGenerator.interfaces;


import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGenerator.data.ComponentInfo;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public interface IComponentCodeGenerator extends ICodeGenerator {
    public List<JavaClassSource> generate(List<ComponentInfo> componentInfos, String pkgDestiny);

}
