package ilargia.entitas.codeGeneration.plugins.postProcessors;


import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;

public class ConsoleWriteLinePostProcessor implements ICodeGenFilePostProcessor<JavaClassSource> {


    @Override
    public String getName() {
        return "Console.WriteLine generated files";
    }

    @Override
    public Integer gePriority() {
        return 200;
    }

    @Override
    public boolean isEnableByDefault() {
        return false;
    }

    @Override
    public boolean runInDryMode() {
        return true;
    }

    @Override
    public void setAppDomain(IAppDomain appDomain) {

    }

    @Override
    public List<CodeGenFile<JavaClassSource>> postProcess(List<CodeGenFile<JavaClassSource>> files) {
        for (CodeGenFile file : files) {
            System.out.println(String.format("%s - %s", file.getFileName(), file.getFileName()));
        }
        return files;
    }

}
