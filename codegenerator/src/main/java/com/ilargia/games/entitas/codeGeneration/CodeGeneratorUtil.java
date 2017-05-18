package com.ilargia.games.entitas.codeGeneration;


import com.ilargia.games.entitas.codeGeneration.config.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeDataProvider;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGeneratorInterface;
import com.ilargia.games.entitas.codeGeneration.config.Preferences;
import com.ilargia.games.entitas.codeGeneration.interfaces.IConfigurable;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGeneratorUtil {

    public static CodeGenerator codeGeneratorFromProperties() {
        Properties properties = Preferences.loadProperties();

        CodeGeneratorConfig config = new CodeGeneratorConfig(properties);
        config.configure(properties);

        List<Class> types = loadTypesFromPlugins(properties);

        List<ICodeDataProvider> dataProviders = getEnabledInstances(types, config.getDataProviders(), ICodeDataProvider.class);
        List<ICodeGenerator> codeGenerators = getEnabledInstances(types, config.getCodeGenerators(), ICodeGenerator.class);
        List<ICodeGenFilePostProcessor> postProcessors = getEnabledInstances(types, config.getPostProcessors(), ICodeGenFilePostProcessor.class);

        configure(dataProviders, properties);
        configure(codeGenerators, properties);
        configure(postProcessors, properties);

        return new CodeGenerator(dataProviders, codeGenerators, postProcessors);
    }

    static <T extends ICodeGeneratorInterface> void configure(List<T> plugins, Properties properties) {
        plugins.stream()
                .filter(p -> p instanceof IConfigurable)
                .map(p -> (IConfigurable) p)
                .forEach(p -> p.configure(properties));
    }

    @SuppressWarnings("finally")
    private static boolean existClass(String className) {
        Class<?> classname = null;
        try {
            classname = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (classname == null)
                return false;
            else
                return true;
        }
    }

    @SuppressWarnings("finally")
    private static Class<? extends Object> getClass(String className) {
        Class<?> classname = null;
        try {
            classname = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (classname == null)
                return classname;
            else
                return classname;
        }
    }

    public static List<Class> loadTypesFromPlugins(Properties properties) {
        CodeGeneratorConfig config = new CodeGeneratorConfig(properties);
        config.configure(properties);

        return config.getPlugins().stream()
                .filter(s -> existClass(s))
                .map(s -> getClass(s))
                .collect(Collectors.toList());

    }

    public static List<String> getOrderedNames(List<String> types) {
        return types.stream()
                .sorted((typeA, typeB) -> typeA.compareTo(typeB))
                .collect(Collectors.toList());
    }

    public static <T extends ICodeGeneratorInterface> List<T> getOrderedInstances(List<Class> types, Class<T> clazz) {
        return types.stream()
                .filter(type -> type.isAssignableFrom(clazz))
                .filter(type -> Modifier.isAbstract(type.getModifiers()))
                .map(type -> (T) newClass(type))
                .sorted((a, b) -> a.gePriority().compareTo(b.gePriority()))
                .collect(Collectors.toList());

    }

    private static <T extends ICodeGeneratorInterface> T newClass(Class type) {
        T instance = null;
        try {
            instance = (T) type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return instance;
    }

    public static <T extends ICodeGeneratorInterface> List<String> getOrderedTypeNames(List<Class> types, Class<T> clazz) {
        return getOrderedInstances(types, clazz).stream()
                .map(instance -> instance.getClass().getSimpleName())
                .sorted()
                .collect(Collectors.toList());

    }

    public static <T extends ICodeGeneratorInterface> List<T> getEnabledInstances(List<Class> types, List<String> enabledTypeNames, Class<T> clazz) {
        return getOrderedInstances(types, clazz).stream()
                .filter(instance -> enabledTypeNames.contains(instance.getClass().getSimpleName()))
                .collect(Collectors.toList());
    }

    public static <T extends ICodeGeneratorInterface> List<String> getAvailable(List<Class> types, List<String> enabledTypeNames, Class<T> clazz) {
        return getOrderedTypeNames(types, clazz).stream()
                .filter(typeName -> !enabledTypeNames.contains(typeName))
                .collect(Collectors.toList());
    }

    public static <T extends ICodeGeneratorInterface> List<String> getUnavailable(List<Class> types, List<String> enabledTypeNames, Class<T> clazz) {
        List<String> typeNames = getOrderedTypeNames(types, clazz);
        return enabledTypeNames.stream()
                .filter(typeName -> !typeNames.contains(typeName))
                .collect(Collectors.toList());
    }

    public static <T extends ICodeGeneratorInterface> List<T> getUsed(List<Class> types, List<String> enabledTypeNames, Class<T> clazz) {
        return getOrderedInstances(types, clazz).stream()
                .filter(instance -> enabledTypeNames.contains(instance.getClass().getSimpleName()))
                .collect(Collectors.toList());

    }

    public static Map<String, String> getConfigurables(List<ICodeDataProvider> dataProviders, List<ICodeGenerator> codeGenerators,
                                                       List<ICodeGenFilePostProcessor> postProcessors) {
        return Stream.concat(Stream.concat(dataProviders.stream(), codeGenerators.stream()), postProcessors.stream())
                .filter(d -> d.getClass().isAssignableFrom(IConfigurable.class))
                .map(d -> (IConfigurable) d)
                .map(instance -> instance.getDefaultProperties())
                .flatMap(prop -> prop.<String, String>entrySet().stream())
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
    }

}
