package ilargia.entitas.codeGenerator.interfaces;


import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;
import java.util.Set;

public interface IContextCodeGenerator extends ICodeGenerator {
    public List<JavaClassSource> generate(Set<String> poolNames, String pkgDestiny);

}
