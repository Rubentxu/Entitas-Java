package ilargia.entitas.codeGeneration.plugins.cli.commands;


import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.interfaces.ICodeDataProvider;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.utils.CodeGeneratorUtil;

import java.util.*;

public class FixConfig extends AbstractCommand {
    static Map<String, String> getConfigurables(List<Class> Classs, CodeGeneratorConfig config) {
        return CodeGeneratorUtil.getConfigurables(
                CodeGeneratorUtil.getUsed(Classs, config.getDataProviders(), ICodeDataProvider.class),
                CodeGeneratorUtil.getUsed(Classs, config.getCodeGenerators(), ICodeGenerator.class),
                CodeGeneratorUtil.getUsed(Classs, config.getPostProcessors(), ICodeGenFilePostProcessor.class)
        );
    }

    static void forceAddKeys(Properties requiredProperties, Properties properties) {
        Set<Object> requiredKeys = requiredProperties.keySet();
        List<String> missingKeys = Helper.getMissingKeys(requiredKeys, properties);

        for (String key : missingKeys) {
            Helper.forceAddKey("Will add missing key", key, requiredProperties.getProperty(key), properties);
        }
    }

    static boolean fix(Set<String> askedRemoveKeys, Set<String> askedAddKeys, List<Class> Classs, CodeGeneratorConfig config, Properties properties) {
        boolean changed = fixPlugins(askedRemoveKeys, askedAddKeys, Classs, config, properties);

        Properties prop = new Properties();
        prop.putAll(getConfigurables(Classs, config));
        forceAddKeys(prop, properties);

        Properties requiredKeys = config.getDefaultProperties();
        requiredKeys.putAll(getConfigurables(Classs, config));
        removeUnusedKeys(askedRemoveKeys, requiredKeys, properties);

        return changed;
    }

    static boolean fixPlugins(Set<String> askedRemoveKeys, Set<String> askedAddKeys, List<Class> Classs, CodeGeneratorConfig config, Properties properties) {
        boolean changed = false;

        List<String> unavailableDataProviders = CodeGeneratorUtil.getUnavailable(Classs, config.getDataProviders(), ICodeDataProvider.class);
        List<String> unavailableCodeGenerators = CodeGeneratorUtil.getUnavailable(Classs, config.getCodeGenerators(), ICodeGenerator.class);
        List<String> unavailablePostProcessors = CodeGeneratorUtil.getUnavailable(Classs, config.getPostProcessors(), ICodeGenFilePostProcessor.class);

        List<String> availableDataProviders = CodeGeneratorUtil.getAvailable(Classs, config.getDataProviders(), ICodeDataProvider.class);
        List<String> availableCodeGenerators = CodeGeneratorUtil.getAvailable(Classs, config.getCodeGenerators(), ICodeGenerator.class);
        List<String> availablePostProcessors = CodeGeneratorUtil.getAvailable(Classs, config.getPostProcessors(), ICodeGenFilePostProcessor.class);

        for (String key : unavailableDataProviders) {
            if (!askedRemoveKeys.contains(key)) {
                Helper.removeValue("Remove unavailable source provider", key, config.getDataProviders(),
                        values -> config.setDataProviders(values), properties);
                askedRemoveKeys.add(key);
                changed = true;
            }
        }

        for (String key : unavailableCodeGenerators) {
            if (!askedRemoveKeys.contains(key)) {
                Helper.removeValue("Remove unavailable code generator", key, config.getCodeGenerators(),
                        values -> config.setCodeGenerators(values), properties);
                askedRemoveKeys.add(key);
                changed = true;
            }
        }

        for (String key : unavailablePostProcessors) {
            if (!askedRemoveKeys.contains(key)) {
                Helper.removeValue("Remove unavailable post processor", key, config.getPostProcessors(),
                        values -> config.setPostProcessors(values), properties);
                askedRemoveKeys.add(key);
                changed = true;
            }
        }

        for (String key : availableDataProviders) {
            if (!askedAddKeys.contains(key)) {
                Helper.addValue("Add available source provider", key, config.getDataProviders(),
                        values -> config.setDataProviders(values), properties);
                askedAddKeys.add(key);
                changed = true;
            }
        }

        for (String key : availableCodeGenerators) {
            if (!askedAddKeys.contains(key)) {
                Helper.addValue("Add available code generator", key, config.getCodeGenerators(),
                        values -> config.setCodeGenerators(values), properties);
                askedAddKeys.add(key);
                changed = true;
            }
        }

        for (String key : availablePostProcessors) {
            if (!askedAddKeys.contains(key)) {
                Helper.addValue("Add available post processor", key, config.getPostProcessors(),
                        values -> config.setPostProcessors(values), properties);
                askedAddKeys.add(key);
                changed = true;
            }
        }

        return changed;
    }

    static void removeUnusedKeys(Set<String> askedRemoveKeys, Properties requiredKeys, Properties properties) {
        List<String> unused = Helper.getUnusedKeys(requiredKeys.keySet(), properties);
        for (String key : unused) {
            if (!askedRemoveKeys.contains(key)) {
                Helper.removeKey("Remove unused key", key, properties);
                askedRemoveKeys.add(key);
            }
        }
    }

    @Override
    public String trigger() {
        return "fix";
    }

    @Override
    public String description() {
        return "Adds missing or removes unused keys interactively";
    }

    @Override
    public String example() {
        return "entitas fix";
    }

    @Override
    public void run(String[] args) {
        System.out.println("Entitas Code Generator version " + 1);

        if (assertProperties()) {
            try {
                Properties properties = null;//loadProperties();

                CodeGeneratorConfig config = new CodeGeneratorConfig();
                config.configure(properties);

                forceAddKeys(config.getDefaultProperties(), properties);

                List<Class> Classs = null;

                try {
                    Classs = CodeGeneratorUtil.loadTypesFromPlugins(properties);
                    getConfigurables(Classs, config);
                } catch (Exception ex) {
                    throw ex;
                }

                Set<String> askedRemoveKeys = new HashSet<String>();
                Set<String> askedAddKeys = new HashSet<String>();
                while (fix(askedRemoveKeys, askedAddKeys, Classs, config, properties)) {
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
