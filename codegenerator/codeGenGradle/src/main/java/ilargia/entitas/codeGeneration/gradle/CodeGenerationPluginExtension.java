package ilargia.entitas.codeGeneration.gradle;

import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.plugins.config.TargetPackageConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class CodeGenerationPluginExtension extends CodeGeneratorConfig {

    private final TargetPackageConfig targetPackageConfig;

    public CodeGenerationPluginExtension() {
        targetPackageConfig = new TargetPackageConfig();
        configure(new Properties());
    }

    @Override
    public void configure(Properties properties) {
        super.configure(properties);
        targetPackageConfig.configure(properties);
    }

    @Override
    public Properties getDefaultProperties() {
        targetPackageConfig.getDefaultProperties();
        super.getDefaultProperties();
        return properties;
    }

    public String getTargetPackage() {
        return targetPackageConfig.getTargetPackage();
    }

    public void setTargetPackage(String target) {
        targetPackageConfig.setTargetPackage(target);
    }

    public void setSearchPackagesKey(String ...packages) {
        super.setPackages(Arrays.asList(packages));
    }

    public void setPluginsPackages(String ...plugins) {
        super.setPlugins(Arrays.asList(plugins));
    }

    public void setDataProviders(String ...plugins) {
        super.setDataProviders(Arrays.asList(plugins));
    }

    public void setGenerators(String ...plugins) {
        super.setCodeGenerators(Arrays.asList(plugins));
    }

    public void setPostProcessors(String ...plugins) {
        super.setPostProcessors(Arrays.asList(plugins));
    }

}
