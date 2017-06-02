package ilargia.entitas.codeGeneration.plugins.dataProviders.context;

import ilargia.entitas.codeGeneration.config.ContextNamesConfig;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class ContextDataProvider implements ICodeGeneratorDataProvider<String, HashMap<String, String>>, IConfigurable {

    public static String CONTEXT_NAME = "context_name";
    private ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();

    public static String getContextName(HashMap<String, String> data) {
        return data.get(CONTEXT_NAME);
    }

    public static void setContextName(HashMap<String, String> data, String contextName) {
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
    public List<HashMap<String, String>> getData() {
        return _contextNamesConfig.getContextNames().stream()
                .map(contextName -> {
                    HashMap<String, String> data = new HashMap<>();
                    setContextName(data, contextName);
                    return data;
                }).collect(Collectors.toList());
    }

}
