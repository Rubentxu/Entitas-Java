package com.ilargia.games.entitas.interfaces;


import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;
import java.util.Set;

public interface IContextCodeGenerator extends ICodeGenerator {
    public List<JavaClassSource> generate(Set<String> poolNames, String pkgDestiny);

}
