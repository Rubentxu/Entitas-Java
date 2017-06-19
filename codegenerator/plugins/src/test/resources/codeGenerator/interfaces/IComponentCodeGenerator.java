package ilargia.entitas.codeGenerator.interfaces;


import ilargia.entitas.codeGenerator.data.ComponentInfo;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public interface IComponentCodeGenerator extends ICodeGenerator {
    public List<JavaClassSource> generate(List<ComponentInfo> componentInfos, String pkgDestiny);

}
