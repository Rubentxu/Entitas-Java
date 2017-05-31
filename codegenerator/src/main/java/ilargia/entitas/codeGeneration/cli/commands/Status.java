package ilargia.entitas.codeGeneration.cli.commands;


import ilargia.entitas.codeGeneration.CodeGeneratorUtil;
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.interfaces.ICodeDataProvider;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Status extends AbstractCommand {
    static void printKeyStatus(Set<Object> requiredKeys, Properties properties) {
        for (String key : Helper.getUnusedKeys(requiredKeys, properties)) {
            System.out.println("Unused key: " + key);
        }

        for (String key : Helper.getMissingKeys(requiredKeys, properties)) {
            System.out.println("Missing key: " + key);
        }
    }

    static void printPluginStatus(List<Class> types, CodeGeneratorConfig config) {
        List<String> unavailableDataProviders = CodeGeneratorUtil.getUnavailable(types, config.getDataProviders(), ICodeDataProvider.class);

        List<String> unavailableCodeGenerators = CodeGeneratorUtil.getUnavailable(types, config.getCodeGenerators(), ICodeGenerator.class);
        List<String> unavailablePostProcessors = CodeGeneratorUtil.getUnavailable(types, config.getPostProcessors(), ICodeGenFilePostProcessor.class);

        List<String> availableDataProviders = CodeGeneratorUtil.getAvailable(types, config.getDataProviders(), ICodeDataProvider.class);
        List<String> availableCodeGenerators = CodeGeneratorUtil.getAvailable(types, config.getCodeGenerators(), ICodeGenerator.class);
        List<String> availablePostProcessors = CodeGeneratorUtil.getAvailable(types, config.getPostProcessors(), ICodeGenFilePostProcessor.class);


        printUnavailable(unavailableDataProviders);
        printUnavailable(unavailableCodeGenerators);
        printUnavailable(unavailablePostProcessors);

        printAvailable(availableDataProviders);
        printAvailable(availableCodeGenerators);
        printAvailable(availablePostProcessors);
    }

    static void printUnavailable(List<String> names) {
        for (String name : names) {
            System.out.println("Unavailable: " + name);
        }
    }

    static void printAvailable(List<String> names) {
        for (String name : names) {
            System.out.println("Available: " + name);
        }
    }

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
                            CodeGeneratorUtil.getUsed(types, config.getDataProviders(), ICodeDataProvider.class),
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
}
