package ilargia.entitas.codeGeneration.utils;


import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.interfaces.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGeneratorUtil {

    public static <T extends ICodeGeneratorInterface> void configure(List<T> plugins, Properties properties) {
        plugins.stream()
                .filter(p -> p instanceof IConfigurable)
                .map(p -> (IConfigurable) p)
                .forEach(p -> p.setProperties(properties));
    }

    public static List<Class> loadTypesFromPlugins(CodeGeneratorConfig config) {
        List<String> plugins = new ArrayList<String>() {{
            addAll(config.getDataProviders());
            addAll(config.getCodeGenerators());
            addAll(config.getPostProcessors());
        }};
        return config.getPlugins().stream()
                .flatMap(s-> CodeFinder.findClassRecursive(s).stream())
                .filter(s-> s.getCanonicalName() != null )
                .filter(s-> plugins.contains(s.getCanonicalName()))
                .sorted((typeA, typeB) -> {
                   return typeA.getCanonicalName().compareTo(typeB.getCanonicalName());
                })
                .collect(Collectors.toList());

    }

    public static <T extends ICodeGeneratorInterface> List<T> getOrderedInstances(List<Class> types, Class<T> clazz) {
        return types.stream()
                .filter(type -> Stream.of(type.getInterfaces()).anyMatch(c-> c.getSimpleName().contains(clazz.getSimpleName())))
                .filter(type -> !Modifier.isAbstract(type.getModifiers()))
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
                .filter(instance ->{
                    return enabledTypeNames.contains(instance.getClass().getCanonicalName());

                })
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

    public static Map<String, String> getConfigurables(List<ICodeGeneratorDataProvider> dataProviders, List<ICodeGenerator> codeGenerators,
                                                       List<ICodeGenFilePostProcessor> postProcessors) {
        return Stream.concat(Stream.concat(dataProviders.stream(), codeGenerators.stream()), postProcessors.stream())
                .filter(d -> d instanceof IConfigurable)
                .map(d -> (IConfigurable) d)
                .map(instance -> instance.defaultProperties())
                .flatMap(prop -> prop.<String, String>entrySet().stream())
                .collect(Collectors.toMap(e -> (String) e.getKey(), e -> (String) e.getValue()));
    }

    public static void writeFile(File file, String content) {
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

    public static Map<String, List<File>> readFileComponents(String pathname, String pkg) {
        String finalPathName = pathname+ "/" + pkg.replace(".", "/");
        Map<String, List<File>> recursiveList = new HashMap() {{
            put("", new ArrayList<>());
        }};
        File d = new File(finalPathName);
        if (d.exists() && d.isDirectory()) {
            for (File listFile : d.listFiles()) {
                if (listFile.isDirectory()) {
                    List<File> listSubDir = Arrays.asList(listFile.listFiles());
                    if (listSubDir.size() > 0) {
                        Path path = Paths.get(listSubDir.get(0).getAbsolutePath());
                        String subDir = path.getName(path.getNameCount() - 2).toString();
                        recursiveList.put(subDir, listSubDir);
                    }

                } else {
                    recursiveList.get("").add(listFile);
                }
            }

        }
        return recursiveList;

    }
}
