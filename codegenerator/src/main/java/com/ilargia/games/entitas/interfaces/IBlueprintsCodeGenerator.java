package com.ilargia.games.entitas.interfaces;


import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public interface IBlueprintsCodeGenerator extends ICodeGenerator {

    public List<JavaClassSource> generate(List<String> blueprintNames, String pkgDestiny);

}
