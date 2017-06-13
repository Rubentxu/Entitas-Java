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

    public List<String> getSearchPackagesKey() {
        return getPackages();
    }

    public void setSearchPackagesKey(String ...packages) {
        this.setPackages(Arrays.asList(packages));
    }

    public List<String> getPluginsPackages() {
        return getPlugins();
    }

    public void setPluginsPackages(String ...plugins) {
        setPlugins(Arrays.asList(plugins));
    }

    public List<String> getDataProviders() {
        return getDataProviders();
    }

    public void setDataProviders(String ...plugins) {
        setDataProviders(Arrays.asList(plugins));
    }

    public List<String> getGenerators() {
        return getCodeGenerators();
    }

    public void setGenerators(String ...plugins) {
        setCodeGenerators(Arrays.asList(plugins));
    }

    public List<String> getPostProcessors() {
        return getPostProcessors();
    }

    public void setPostProcessors(String ...plugins) {
        setPostProcessors(Arrays.asList(plugins));
    }

}
