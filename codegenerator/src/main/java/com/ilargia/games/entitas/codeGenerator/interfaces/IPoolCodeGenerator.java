package com.ilargia.games.entitas.codeGenerator.interfaces;


import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;
import java.util.Set;

public interface IPoolCodeGenerator extends ICodeGenerator {
    public List<JavaClassSource> generate(Set<String> poolNames, String pkgDestiny);

}
