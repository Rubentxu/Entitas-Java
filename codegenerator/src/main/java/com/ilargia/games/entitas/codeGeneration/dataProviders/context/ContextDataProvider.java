package com.ilargia.games.entitas.codeGeneration.dataProviders.context;

import com.ilargia.games.entitas.codeGeneration.config.ContextNamesConfig;
import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeDataProvider;
import com.ilargia.games.entitas.codeGeneration.interfaces.IConfigurable;

import java.util.List;
import java.util.Properties;


public class ContextDataProvider implements ICodeDataProvider, IConfigurable {

    public static String CONTEXT_NAME = "context_name";
    private ContextNamesConfig _contextNamesConfig = new ContextNamesConfig();
    List<SourceDataFile> sourceDataFiles;


    public ContextDataProvider(List<SourceDataFile> datas) {
        sourceDataFiles = datas;
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
        return null;
    }

    @Override
    public void configure(Properties properties) {
        _contextNamesConfig.configure(properties);
    }

    @Override
    public List<SourceDataFile> getData() {
       sourceDataFiles.stream()
                .forEach(data -> _contextNamesConfig.getContextNames().stream().forEach(name -> setContextName(data, name)));
        return  sourceDataFiles;
    }

    public static String getContextName(SourceDataFile data) {
        return (String) data.get(CONTEXT_NAME);
    }

    public static void setContextName(SourceDataFile data, String contextName) {
        data.put(CONTEXT_NAME, contextName);
    }

}
