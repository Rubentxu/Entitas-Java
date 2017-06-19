package ilargia.entitas.codeGenerator.interfaces;


import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public interface IBlueprintsCodeGenerator extends ICodeGenerator {

    public List<JavaClassSource> generate(List<String> blueprintNames, String pkgDestiny);

}
