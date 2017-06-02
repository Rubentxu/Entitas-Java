package ilargia.entitas.codeGeneration.plugins.postProcessors;


import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.plugins.config.TargetPackageConfig;
import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.utils.CodeGeneratorUtil;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.File;
import java.util.List;
import java.util.Properties;

public class WriteToDiskPostProcessor implements ICodeGenFilePostProcessor<JavaClassSource>, IConfigurable {

    private final TargetPackageConfig targetPackageConfig;
    private final IAppDomain projectConfig;
    private CodeGeneratorUtil codeGeneratorUtil;

    public WriteToDiskPostProcessor(CodeGeneratorUtil codeGeneratorUtil, IAppDomain preferences) {
        this.codeGeneratorUtil = codeGeneratorUtil;
        targetPackageConfig = new TargetPackageConfig();
        projectConfig = preferences;
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
    public List<CodeGenFile<JavaClassSource>> postProcess(List<CodeGenFile<JavaClassSource>> files) {
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
                String.format("%s/%s/%s.java", projectConfig.getFirtsSrcDir(), pathPackage, className) :
                String.format("%s/%s/%s/%s.java", projectConfig.getFirtsSrcDir(), pathPackage, subDir, className);

        File file = new File(pathFile);
        codeGeneratorUtil.writeFile(file, content.toString());
        return file;
    }


}
