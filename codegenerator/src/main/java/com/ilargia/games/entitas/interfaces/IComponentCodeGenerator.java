package com.ilargia.games.entitas.interfaces;


import com.ilargia.games.entitas.intermediate.ComponentInfo;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public interface IComponentCodeGenerator extends ICodeGenerator {
    public List<JavaClassSource> generate(List<ComponentInfo> componentInfos, String pkgDestiny);

}
