package ilargia.entitas.codeGeneration.gradle;

import ilargia.entitas.codeGeneration.CodeGenerator;
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.utils.CodeGeneratorUtil;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import java.util.List;
import java.util.Properties;

public class CodeGenerationTask extends DefaultTask {
    private EntitasGradleProject entitasProject;
    private CodeGeneratorConfig codeGeneratorConfig;
    private Properties properties;

    public CodeGenerationTask() {
        entitasProject = new EntitasGradleProject(getProject());
        properties = new Properties();
        codeGeneratorConfig = new CodeGeneratorConfig();
    }

    public CodeGenerator getCodeGenerator(CodeGenerationPluginExtension extension) {
        extension.configure(properties);
        List<Class> types = CodeGeneratorUtil.loadTypesFromPlugins(properties);

        List<ICodeGeneratorDataProvider> dataProviders = CodeGeneratorUtil.getEnabledInstances(types,
                extension.getDataProviders(), ICodeGeneratorDataProvider.class);
        List<ICodeGenerator> codeGenerators = CodeGeneratorUtil.getEnabledInstances(types,
                extension.getCodeGenerators(), ICodeGenerator.class);
        List<ICodeGenFilePostProcessor> postProcessors = CodeGeneratorUtil.getEnabledInstances(types,
                extension.getPostProcessors(), ICodeGenFilePostProcessor.class);

        CodeGeneratorUtil.configure(dataProviders, properties);
        CodeGeneratorUtil.configure(codeGenerators, properties);
        CodeGeneratorUtil.configure(postProcessors, properties);

        return new CodeGenerator(dataProviders, codeGenerators, postProcessors);
    }


    @TaskAction
    public void run() {
        CodeGenerationPluginExtension extension = getProject().getExtensions().findByType(CodeGenerationPluginExtension.class);
        if (extension == null) {
            extension = new CodeGenerationPluginExtension();
        }
        CodeGenerator codeGenerator = getCodeGenerator(extension);
        codeGenerator.generate();

    }

}
