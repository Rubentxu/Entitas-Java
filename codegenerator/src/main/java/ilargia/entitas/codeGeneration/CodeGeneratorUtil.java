package ilargia.entitas.codeGeneration;


import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.interfaces.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGeneratorUtil {

    public static <T extends ICodeGeneratorInterface> void configure(List<T> plugins, Properties properties) {
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
        CodeGeneratorConfig config = new CodeGeneratorConfig();
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
                .filter(d -> d instanceof IConfigurable)
                .map(d -> (IConfigurable) d)
                .map(instance -> instance.getDefaultProperties())
                .flatMap(prop -> prop.<String, String>entrySet().stream())
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
    }

    public void writeFile(File file, String content) {
        try {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            System.out.println("Unable to create file " + file.getName());
        }
    }
}
