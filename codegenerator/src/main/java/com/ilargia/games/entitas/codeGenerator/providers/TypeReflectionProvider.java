package com.ilargia.games.entitas.codeGenerator.providers;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.codeGenerator.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;

public class TypeReflectionProvider implements ICodeGeneratorDataProvider {

    private List<ComponentInfo> _componentInfos= new ArrayList<>();
    private List<String> _poolNames;
    private List<String> _blueprintNames;
    private String path;

    public TypeReflectionProvider(List<String> poolNames, List<String> blueprintNames, String componentsDirectory) throws IOException {
        path = componentsDirectory;
        Set pools = null;
        if(poolNames !=null ) pools= new HashSet<String>(poolNames);
        if (poolNames ==null || poolNames.size() == 0) {
            poolNames = new ArrayList<>();
            pools = new HashSet();
            pools.add(CodeGenerator.DEFAULT_POOL_NAME);
        }
        _componentInfos = componentInfos();
        poolNames = ((List) pools.stream()
                .map(poolName -> capitalize(poolName.toString()))
                .sorted(naturalOrder())
                .collect(Collectors.toList()));

        _blueprintNames = blueprintNames;


    }


    @Override
    public List<ComponentInfo> componentInfos() throws IOException {
        if(_componentInfos == null || _componentInfos.size() == 0) {
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
                    .collect(Collectors.toList()));

        }
        return _componentInfos;

    }

    @Override
    public List<String> poolNames() {
        return new ArrayList<>();
    }

    @Override
    public List<String> blueprintNames() {
        return new ArrayList<>();
    }

    public static ComponentInfo createComponentInfo(JavaClassSource component) {

        String name = component.getName();
        String fullName = component.getCanonicalName();
        List<FieldSource<JavaClassSource>> fields = component.getFields();
        AnnotationSource<JavaClassSource> annotation = component.getAnnotation(Component.class);

        List<String> poolNames = (annotation.toString().contains("pools"))
                ?Arrays.asList(annotation.getStringArrayValue("pools"))
                : null ;

        List<String> customComponentName = (annotation.toString().contains("customComponentName"))
                ?Arrays.asList(annotation.getStringArrayValue("customComponentName"))
                : null ;

        Boolean isSingleEntity = Boolean.getBoolean(annotation.getStringValue("isSingleEntity"));
        String customPrefix = annotation.getStringValue("customPrefix");

        return new ComponentInfo(
                fullName,
                name,
                fields,
                poolNames,
                isSingleEntity,
                customPrefix,
                false,
                false,
                false,
                false
        );
    }

    public static List<File> readFileComponents(String pathname) throws IOException {
        List<File> recursiveList = new ArrayList<File>();
        File d = new File(pathname);
        if (d.isDirectory()) {
            File[] listFiles = d.listFiles();
            for (int i=0; i < listFiles.length; i++) {
                recursiveList.addAll(readFileComponents(listFiles[i].getCanonicalPath()));
            }
        } else {
            recursiveList.add(d);
        }
        return recursiveList;

    }


    private String capitalize(final String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }


}
