package com.ilargia.games.entitas.codeGeneration.dataProviders.entityIndex;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGeneration.config.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeDataProvider;
import com.ilargia.games.entitas.codeGeneration.data.MemberData;
import com.ilargia.games.entitas.codeGeneration.data.MethodData;
import com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.ContextsDataProvider;
import com.ilargia.games.entitas.codeGeneration.interfaces.IConfigurable;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class EntityIndexDataProvider implements ICodeDataProvider, IConfigurable {

    public static String ENTITY_INDEX_TYPE = "entityIndex_type";
    public static String ENTITY_INDEX_IS_CUSTOM = "entityIndex_isCustom";
    public static String ENTITY_INDEX_CUSTOM_METHODS = "entityIndex_customMethods";
    public static String ENTITY_INDEX_NAME = "entityIndex_name";
    public static String ENTITY_INDEX_CONTEXT_NAMES = "entityIndex_contextNames";
    public static String ENTITY_INDEX_KEY_TYPE = "entityIndex_keyType";
    public static String ENTITY_INDEX_COMPONENT_TYPE = "entityIndex_componentType";
    public static String ENTITY_INDEX_MEMBER_NAME = "entityIndex_memberName";

    private CodeGeneratorConfig _codeGeneratorConfig = new CodeGeneratorConfig();
    private ContextsDataProvider _contextsComponentDataProvider = new ContextsDataProvider();

    List<SourceDataFile> sourceDataFiles;

    public EntityIndexDataProvider(List<SourceDataFile> types) {
        sourceDataFiles = types;
    }

    @Override
    public String getName() {
        return "Entity Index";
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
        _codeGeneratorConfig.configure(properties);
        _contextsComponentDataProvider.configure(properties);
    }

    @Override
    public List<SourceDataFile> getData() {
        List<SourceDataFile> entityIndexData = sourceDataFiles.stream()
                .filter(s -> !s.source.isAbstract())
                .filter(s -> s.source.hasAnnotation("CustomEntityIndex"))
                .map(s -> createCustomEntityIndexData(s))
                .collect(Collectors.toList());


        List<SourceDataFile> customEntityIndexData = sourceDataFiles.stream()
                .filter(s -> !s.source.isAbstract())
                .filter(s -> s.source.hasInterface(IComponent.class))
                .map(s -> createEntityIndexData(s, s.source.getFields()))
                .collect(Collectors.toList());


        entityIndexData.addAll(customEntityIndexData);
        return entityIndexData;
    }

    private SourceDataFile createEntityIndexData(SourceDataFile data, List<FieldSource<JavaClassSource>> infos) {


        FieldSource<JavaClassSource> info = infos.stream()
                .filter(i -> i.hasAnnotation("EntityIndex"))
                .collect(singletonCollector());

        setEntityIndexType(data, info.getAnnotation("EntityIndex").getStringValue());
        isCustom(data, false);
        setEntityIndexName(data, data.source.getCanonicalName());
        setKeyType(data, info.getType().getName());
        setComponentType(data, data.source.getCanonicalName());
        setMemberName(data, info.getName());
        setContextNames(data, _contextsComponentDataProvider.getContextNamesOrDefault(data.source));

        return data;
    }

    SourceDataFile createCustomEntityIndexData(SourceDataFile data) {

        AnnotationSource attribute = data.source.getAnnotation("CustomEntityIndex");

        setEntityIndexType(data, data.source.getName());
        isCustom(data, true);
        setEntityIndexName(data, data.source.getCanonicalName());

        setContextNames(data, Arrays.asList(attribute.getStringArrayValue()));

        List<MethodData> getMethods = data.source.getMethods().stream()
                .filter(m -> m.isPublic())
                .filter(m -> m.hasAnnotation("EntityIndexGetMethod"))
                .map(m -> new MethodData(m.getReturnType(), m.getName(),
                        m.getParameters().stream().map(p -> new MemberData(p.getType(), p.getName(), null)).collect(Collectors.toList()),
                        m.getAnnotation("EntityIndexGetMethod")
                ))
                .collect(Collectors.toList());

        setCustomMethods(data, getMethods);
        return data;

    }

    public static <T> Collector<T, List<T>, T> singletonCollector() {
        return Collector.of(
                ArrayList::new,
                List::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                list -> {
                    if (list.size() != 1) {
                        list.clear();
                    }
                    return list.get(0);
                }
        );
    }

    public static String getEntityIndexType(SourceDataFile data) {
        return (String) data.get(ENTITY_INDEX_TYPE);
    }

    public static void setEntityIndexType(SourceDataFile data, String type) {
        data.put(ENTITY_INDEX_TYPE, type);
    }

    public static boolean isCustom(SourceDataFile data) {
        return (boolean) data.get(ENTITY_INDEX_IS_CUSTOM);
    }

    public static void isCustom(SourceDataFile data, boolean isCustom) {
        data.put(ENTITY_INDEX_IS_CUSTOM, isCustom);
    }

    public static List<MethodData> getCustomMethods(SourceDataFile data) {
        return (List<MethodData>) data.get(ENTITY_INDEX_CUSTOM_METHODS);
    }

    public static void setCustomMethods(SourceDataFile data, List<MethodData> methods) {
        data.put(ENTITY_INDEX_CUSTOM_METHODS, methods);
    }

    public static String getEntityIndexName(SourceDataFile data) {
        return (String) data.get(ENTITY_INDEX_NAME);
    }

    public static void setEntityIndexName(SourceDataFile data, String name) {
        data.put(ENTITY_INDEX_NAME, name);
    }

    public static List<String> getContextNames(SourceDataFile data) {
        return (List<String>) data.get(ENTITY_INDEX_CONTEXT_NAMES);
    }

    public static void setContextNames(SourceDataFile data, List<String> contextNames) {
        data.put(ENTITY_INDEX_CONTEXT_NAMES, contextNames);
    }

    public static String getKeyType(SourceDataFile data) {
        return (String) data.get(ENTITY_INDEX_KEY_TYPE);
    }

    public static void setKeyType(SourceDataFile data, String type) {
        data.put(ENTITY_INDEX_KEY_TYPE, type);
    }

    public static String getComponentType(SourceDataFile data) {
        return (String) data.get(ENTITY_INDEX_COMPONENT_TYPE);
    }

    public static void setComponentType(SourceDataFile data, String type) {
        data.put(ENTITY_INDEX_COMPONENT_TYPE, type);
    }

    public static String getMemberName(SourceDataFile data) {
        return (String) data.get(ENTITY_INDEX_MEMBER_NAME);
    }

    public static void setMemberName(SourceDataFile data, String memberName) {
        data.put(ENTITY_INDEX_MEMBER_NAME, memberName);
    }

}
