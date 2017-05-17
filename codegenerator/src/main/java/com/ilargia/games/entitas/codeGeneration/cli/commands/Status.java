package com.ilargia.games.entitas.codeGeneration.cli.commands;


import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGeneratorUtil;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Status extends AbstractCommand {
    @Override
    public String trigger() {
        return "status";
    }

    @Override
    public String description() {
        return "Lists available and unavailable plugins";
    }

    @Override
    public String example() {
        return "entitas status";
    }

    @Override
    public void run(String[] args) {
        System.out.println("Entitas Code Generator version " + 1);

        if (assertProperties()) {

            Properties properties = null;
            try {
                properties = loadProperties();
                CodeGeneratorConfig config = new CodeGeneratorConfig();
                config.configure(properties);

                System.out.println(config.toString());

                List<Class> types = null;
                Map<String, String> configurables = null;

                try {
                    types = CodeGeneratorUtil.loadTypesFromPlugins(properties);
                    configurables = CodeGeneratorUtil.getConfigurables(
                            CodeGeneratorUtil.getUsed(types, config.getDataProviders(), ICodeGeneratorDataProvider.class),
                            CodeGeneratorUtil.getUsed(types, config.getCodeGenerators(), ICodeGenerator.class),
                            CodeGeneratorUtil.getUsed(types, config.getPostProcessors(), ICodeGenFilePostProcessor.class)
                    );

                } catch (Exception ex) {
                    printKeyStatus(config.getDefaultProperties().keySet(), properties);
                    throw ex;
                }

                config.getDefaultProperties().putAll(configurables);
                Set<Object> requiredKeys = config.getDefaultProperties().keySet();

                printKeyStatus(requiredKeys, properties);
                printPluginStatus(types, config);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static void printKeyStatus(Set<Object> requiredKeys, Properties properties) {
        foreach(var key in Helper.GetUnusedKeys(requiredKeys, properties)) {
            fabl.Info("Unused key: " + key);
        }

        foreach(var key in Helper.GetMissingKeys(requiredKeys, properties)) {
            fabl.Warn("Missing key: " + key);
        }
    }

    static void printPluginStatus(Type[] types, CodeGeneratorConfig config) {
        var unavailableDataProviders = CodeGeneratorUtil.GetUnavailable < ICodeGeneratorDataProvider > (types, config.
        dataProviders);
        var unavailableCodeGenerators = CodeGeneratorUtil.GetUnavailable < ICodeGenerator > (types, config.
        codeGenerators);
        var unavailablePostProcessors = CodeGeneratorUtil.GetUnavailable < ICodeGenFilePostProcessor > (types, config.
        postProcessors);

        var availableDataProviders = CodeGeneratorUtil.GetAvailable < ICodeGeneratorDataProvider > (types, config.
        dataProviders);
        var availableCodeGenerators = CodeGeneratorUtil.GetAvailable < ICodeGenerator > (types, config.codeGenerators);
        var availablePostProcessors = CodeGeneratorUtil.GetAvailable < ICodeGenFilePostProcessor > (types, config.
        postProcessors);

        printUnavailable(unavailableDataProviders);
        printUnavailable(unavailableCodeGenerators);
        printUnavailable(unavailablePostProcessors);

        printAvailable(availableDataProviders);
        printAvailable(availableCodeGenerators);
        printAvailable(availablePostProcessors);
    }

    static void printUnavailable(string[] names) {
        foreach(var name in names) {
            fabl.Warn("Unavailable: " + name);
        }
    }

    static void printAvailable(string[] names) {
        foreach(var name in names) {
            fabl.Info("Available: " + name);
        }
    }
}
