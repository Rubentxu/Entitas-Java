package com.ilargia.games.entitas.codeGenerator.providers;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;

public class TypeReflectionProvider implements ICodeGeneratorDataProvider {

    private ComponentInfo[] _componentInfos;
    private String[] _poolNames;
    private String[] _blueprintNames;

    TypeReflectionProvider(String[] poolNames, String[] blueprintNames, String componentsDirectory) {
        Set pools = new HashSet<String>(Arrays.asList(poolNames));
        if (poolNames.length == 0) {
            pools.add(CodeGenerator.DEFAULT_POOL_NAME);
        }
        _componentInfos = componentInfos(componentsDirectory);
        ((List) pools.stream()
                .map(poolName -> capitalize(poolName.toString()))
                .sorted(naturalOrder())
                .collect(Collectors.toList())).toArray(poolNames);

        _blueprintNames = blueprintNames;

    }


    @Override
    public ComponentInfo[] componentInfos(String path) {
        List<ComponentInfo> list = readFileComponents(path).stream()
                .map((file) -> {
                    try {
                        return Roaster.parse(JavaClassSource.class, file);
                    } catch (FileNotFoundException e) {
                        return null;
                    }
                }).filter((source) -> source != null)
                .filter((source) -> source.getInterfaces().toString().matches(".*\\bIComponent\\b.*"))
                .map((source) -> createComponentInfo(source))
                .collect(Collectors.toList());

        ComponentInfo[] array = new ComponentInfo[0];
        list.toArray(array);

        return array;

    }

    @Override
    public String[] poolNames() {
        return new String[0];
    }

    @Override
    public String[] blueprintNames() {
        return new String[0];
    }

    public static ComponentInfo createComponentInfo(JavaClassSource component) {

        String name = component.getName();
        String fullName = component.getCanonicalName();
        List<FieldSource<JavaClassSource>> fields = component.getFields();


        return new ComponentInfo(
                fullName,
                name,
                fields,
                null,
                false,
                null,
                false,
                false,
                false,
                false
        );
    }

    public static List<File> readFileComponents(String pathname) {
        List<File> recursiveList = new ArrayList<File>();
        File d = new File(pathname);
        if (d.isDirectory()) {
            for (String f : d.list()) {
                readFileComponents(f);
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
