package entitas.gradle;

import ilargia.entitas.codeGeneration.CodeGenerator;
import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.utils.CodeGeneratorUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import java.util.List;
import java.util.Properties;

public class CodeGenerationTask extends DefaultTask {


    public CodeGenerator getCodeGenerator(CodeGenerationPluginExtension extension, IAppDomain appDomain) {
        Properties properties = new Properties();
        extension.configure(properties);
        extension.getDefaultProperties();
        List<Class> types = CodeGeneratorUtil.loadTypesFromPlugins(extension);

        List<ICodeGeneratorDataProvider> dataProviders = CodeGeneratorUtil.getEnabledInstances(types,
                extension.getDataProviders(), ICodeGeneratorDataProvider.class);
        List<ICodeGenerator> codeGenerators = CodeGeneratorUtil.getEnabledInstances(types,
                extension.getCodeGenerators(), ICodeGenerator.class);
        List<ICodeGenFilePostProcessor> postProcessors = CodeGeneratorUtil.getEnabledInstances(types,
                extension.getPostProcessors(), ICodeGenFilePostProcessor.class);

        dataProviders.stream().forEach(p-> p.setAppDomain(appDomain));
        postProcessors.stream().forEach(p-> p.setAppDomain(appDomain));

        CodeGeneratorUtil.configure(dataProviders, properties);
        CodeGeneratorUtil.configure(codeGenerators, properties);
        CodeGeneratorUtil.configure(postProcessors, properties);

        return new CodeGenerator(dataProviders, codeGenerators, postProcessors);
    }


    @TaskAction
    public void run() {
        EntitasGradleProject entitasProject = new EntitasGradleProject(getProject());
        CodeGenerationPluginExtension extension = getProject().getExtensions().findByType(CodeGenerationPluginExtension.class);
        if (extension == null) {
            extension = new CodeGenerationPluginExtension();
        }
        CodeGenerator codeGenerator = getCodeGenerator(extension, entitasProject);
        List<CodeGenFile> gen = codeGenerator.generate();

    }

}
