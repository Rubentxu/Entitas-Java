package com.ilargia.games.entitas.codeGenerator.interfaces;


import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public interface IBlueprintsCodeGenerator extends ICodeGenerator {

    public List<JavaClassSource> generate(List<String> blueprintNames, String pkgDestiny);

}
