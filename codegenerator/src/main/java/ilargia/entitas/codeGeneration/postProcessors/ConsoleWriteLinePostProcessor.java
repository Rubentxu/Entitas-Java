package ilargia.entitas.codeGeneration.postProcessors;


import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;

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
            System.out.println(String.format("%s - %s", file.getFileName(), file.getFileName()));
        }
        return files;
    }

}
