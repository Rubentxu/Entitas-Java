package ilargia.entitas.codeGeneration.plugins.dataProviders.context;

import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.config.ContextNamesConfig;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class ContextDataProvider implements ICodeGeneratorDataProvider, IConfigurable {

    public static String CONTEXT_NAME = "context_name";
    private ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();
    private IAppDomain appDomain;

    public static String getContextName(CodeGeneratorData data) {
        return (String) data.get(CONTEXT_NAME);
    }

    public static void setContextName(CodeGeneratorData data, String contextName) {
        data.put(CONTEXT_NAME, contextName);
    }

    @Override
    public String getName() {
        return "Context";
    }

    @Override
    public Integer gePriority() {
        return 0;
    }

    @Override
    public boolean isEnableByDefault() {
        return true;
    }

    @Override
    public boolean runInDryMode() {
        return true;
    }

    @Override
    public Properties getDefaultProperties() {
        return _contextNamesConfig.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        _contextNamesConfig.configure(properties);
    }

    @Override
    public void setAppDomain(IAppDomain appDomain) {
        this.appDomain = appDomain;
    }

    @Override
    public List<CodeGeneratorData> getData() {
        return _contextNamesConfig.getContextNames().stream()
                .map(contextName -> {
                    CodeGeneratorData data = new CodeGeneratorData();
                    setContextName(data, contextName);
                    return data;
                }).collect(Collectors.toList());
    }

}
