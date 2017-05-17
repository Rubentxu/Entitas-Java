package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.context;

import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGeneration.plugins.config.ContextNamesConfig;
import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;


public class ContextDataProvider implements ICodeGeneratorDataProvider, IConfigurable {

    public static String CONTEXT_NAME = "context_name";
    private ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();

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
        return null;
    }

    @Override
    public void configure(Properties properties) {
        _contextNamesConfig.configure(properties);
    }

    @Override
    public List<CodeGeneratorData> getData() {
        return _contextNamesConfig.getContextNames().stream()
                .map(name -> {
                    ContextData data = new ContextData();
                    setContextName(data, name);
                    return data;
                }).collect(Collectors.toList());
    }

    public static String getContextName(ContextData data) {
        return (String) data.get(CONTEXT_NAME);
    }

    public static void setContextName(ContextData data, String contextName) {
        data.put(CONTEXT_NAME, contextName);
    }

}
