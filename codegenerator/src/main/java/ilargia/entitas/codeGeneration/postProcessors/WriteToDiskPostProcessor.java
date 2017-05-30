package ilargia.entitas.codeGeneration.postProcessors;


import ilargia.entitas.codeGeneration.CodeGeneratorUtil;
import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import ilargia.entitas.codeGeneration.config.TargetPackageConfig;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class WriteToDiskPostProcessor implements ICodeGenFilePostProcessor, IConfigurable {

    private CodeGeneratorUtil codeGeneratorUtil;
    private final TargetPackageConfig targetPackageConfig;

    public WriteToDiskPostProcessor(CodeGeneratorUtil codeGeneratorUtil) {
        this.codeGeneratorUtil = codeGeneratorUtil;
        targetPackageConfig = new TargetPackageConfig();
    }


    @Override
    public Properties getDefaultProperties() {
        return targetPackageConfig.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        targetPackageConfig.configure(properties);
    }

    @Override
    public String getName() {
        return "Write to disk";
    }

    @Override
    public Integer gePriority() {
        return 100;
    }

    @Override
    public boolean isEnableByDefault() {
        return true;
    }

    @Override
    public boolean runInDryMode() {
        return false;
    }

    @Override
    public List<CodeGenFile> postProcess(List<CodeGenFile> files) {
        files.stream().forEach(f -> createFile(f.getFileName(), f.getSubDir(), f.getFileContent()));
        return files;

    }

    public File createFile(String className, String subDir, JavaClassSource content) {
        String targetPackage = targetPackageConfig.targetPackage();
        String finalPackage = subDir.equals("") ?
                String.format("%s", targetPackage) :
                String.format("%s.%s", targetPackage, subDir);
        content.setPackage(finalPackage);

        String pathPackage = targetPackage.replace(".", "/");
        String pathFile = subDir.equals("") ?
                String.format("%s/%s.java", pathPackage, className) :
                String.format("%s/%s/%s.java", pathPackage, subDir, className);

        File file = new File(pathFile);
        codeGeneratorUtil.writeFile(file, content.toString());
        return file;
    }


}
