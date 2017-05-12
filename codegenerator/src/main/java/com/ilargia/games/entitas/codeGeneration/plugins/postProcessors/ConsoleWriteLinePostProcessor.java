package com.ilargia.games.entitas.codeGeneration.plugins.postProcessors;


import com.ilargia.games.entitas.codeGeneration.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.JavaDocSource;

import java.util.List;

public class ConsoleWriteLinePostProcessor implements ICodeGenFilePostProcessor {


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
    public List<CodeGenFile> postProcess(List<CodeGenFile> files) {
        for (CodeGenFile file : files) {
            System.out.println(String.format("%s - %s", file.fileName, file.generatorName));
        }
        return files;
    }

}
