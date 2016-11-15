package com.ilargia.games.entitas.codeGenerator.interfaces;


import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public interface IPoolCodeGenerator extends ICodeGenerator {
    public List<JavaClassSource> generate(List<String> poolNames, String pkgDestiny);

}
