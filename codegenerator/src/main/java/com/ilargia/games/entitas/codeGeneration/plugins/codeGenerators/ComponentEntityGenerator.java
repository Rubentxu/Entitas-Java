package com.ilargia.games.entitas.codeGeneration.plugins.codeGenerators;

import com.ilargia.games.entitas.codeGeneration.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;

import java.util.List;
import java.util.Properties;

public class ComponentEntityGenerator implements ICodeGenerator, IConfigurable {

    @Override
    public String getName() {
        return "Component (Entity API)";
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
        _ignoreNamespacesConfig.Configure(properties);
    }

    @Override
    public List<CodeGenFile> generate(List<CodeGeneratorData> data) {
        return null;
    }
}
