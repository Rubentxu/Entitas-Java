package ilargia.entitas.codeGeneration.dataProviders.context;

import ilargia.entitas.codeGeneration.config.ContextNamesConfig;
import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.ICodeDataProvider;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public class ContextDataProvider implements ICodeDataProvider<HashMap<String,Object>>, IConfigurable {

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
        return _contextNamesConfig.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        _contextNamesConfig.configure(properties);
    }

    @Override
    public List<HashMap<String,Object>> getData() {
        _contextNamesConfig.getContextNames().stream().forEach(name ->
                sourceDataFiles.stream().forEach(data ->  {
                    setContextName(data, name);
                }));
        return  sourceDataFiles;
    }

    public static String getContextName(SourceDataFile data) {
        return (String) data.get(CONTEXT_NAME);
    }

    public static void setContextName(SourceDataFile data, String contextName) {
        data.put(CONTEXT_NAME, contextName);
    }

}
