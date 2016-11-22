package com.ilargia.games.entitas.codeGenerator.providers;


import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.codeGenerator.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class TypeReflectionProvider implements ICodeGeneratorDataProvider {

    private List<ComponentInfo> _componentInfos;
    private Set<String> _poolNames;
    private String path;

    public TypeReflectionProvider(String componentsDirectory) throws IOException {
        path = componentsDirectory;
        _poolNames = new HashSet<>();
        _componentInfos = new ArrayList<>();

    }

    public static List<File> readFileComponents(String pathname) {
        List<File> recursiveList = new ArrayList<File>();
        File d = new File(pathname);
        if (d.isDirectory()) {
            File[] listFiles = d.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                try {
                    recursiveList.addAll(readFileComponents(listFiles[i].getCanonicalPath()));
                } catch (IOException e) {
                    System.out.println("Error : " + e.getMessage());
                }
            }
        } else {
            recursiveList.add(d);
        }
        return recursiveList;

    }

    @Override
    public List<ComponentInfo> componentInfos() {
        if (_componentInfos == null || _componentInfos.size() == 0) {
            _componentInfos.addAll(readFileComponents(path).stream()
                    .map((file) -> {
                        try {
                            return Roaster.parse(JavaClassSource.class, file);
                        } catch (FileNotFoundException e) {
                            return null;
                        }
                    }).filter((source) -> source != null)
                    .filter((source) -> source.getInterfaces().toString().matches(".*\\bIComponent\\b.*"))
                    .map((source) -> createComponentInfo(source))
                    .filter(info-> info != null)
                    .collect(Collectors.toList()));
        }
        return _componentInfos;

    }

    @Override
    public Set<String> poolNames() {
        return _poolNames;
    }

    @Override
    public List<String> blueprintNames() {
        return new ArrayList<>();
    }

    public ComponentInfo createComponentInfo(JavaClassSource component) {

        String name = component.getName();
        String fullName = component.getCanonicalName();
        List<FieldSource<JavaClassSource>> fields = component.getFields();
        List<MethodSource<JavaClassSource>> contructores = component.getMethods().stream()
                .filter(method -> method.isPublic())
                .filter(method -> method.isConstructor())
                .filter(method -> method.getParameters().size() > 0)
                .collect(Collectors.toList());

        List<String> enums = component.getNestedTypes().stream()
                .filter(method -> method.isPublic())
                .filter(method -> method.isEnum())
                .map(method -> method.getCanonicalName())
                .collect(Collectors.toList());



        AnnotationSource<JavaClassSource> annotation = component.getAnnotation(Component.class);

        if(annotation != null) {
            List<String> poolNames = (annotation.toString().contains("pools"))
                    ? Arrays.asList(annotation.getStringArrayValue("pools"))
                    : null;

            List<String> customComponentName = (annotation.toString().contains("customComponentName"))
                    ? Arrays.asList(annotation.getStringArrayValue("customComponentName"))
                    : null;

            Boolean isSingleEntity = Boolean.parseBoolean(annotation.getStringValue("isSingleEntity"));
            String customPrefix = annotation.getStringValue("customPrefix");

            _poolNames.addAll(poolNames);

            return new ComponentInfo(
                    fullName,
                    name,
                    fields,
                    poolNames,
                    isSingleEntity,
                    customPrefix,
                    true,
                    true,
                    false,
                    false,
                    contructores,
                    enums

            );
        }
        return null;

    }


}
