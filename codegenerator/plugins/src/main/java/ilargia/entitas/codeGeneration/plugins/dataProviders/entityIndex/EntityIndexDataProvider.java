package ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.data.MemberData;
import ilargia.entitas.codeGeneration.data.MethodData;
import ilargia.entitas.codeGeneration.data.SourceDataFile;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsDataProvider;
import ilargia.entitas.codeGenerator.annotations.CustomEntityIndex;
import ilargia.entitas.codeGenerator.annotations.EntityIndex;
import ilargia.entitas.codeGenerator.annotations.EntityIndexGetMethod;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class EntityIndexDataProvider implements ICodeGeneratorDataProvider, IConfigurable {

    public static String ENTITY_INDEX_TYPE = "entityIndex_type";
    public static String ENTITY_INDEX_IS_CUSTOM = "entityIndex_isCustom";
    public static String ENTITY_INDEX_CUSTOM_METHODS = "entityIndex_customMethods";
    public static String ENTITY_INDEX_NAME = "entityIndex_name";
    public static String ENTITY_INDEX_CONTEXT_NAMES = "entityIndex_contextNames";
    public static String ENTITY_INDEX_KEY_TYPE = "entityIndex_keyType";
    public static String ENTITY_INDEX_COMPONENT_TYPE = "entityIndex_componentType";
    public static String ENTITY_INDEX_MEMBER_NAME = "entityIndex_memberName";
    List<SourceDataFile> sourceDataFiles;
    private CodeGeneratorConfig _codeGeneratorConfig = new CodeGeneratorConfig();
    private ContextsDataProvider _contextsComponentDataProvider = new ContextsDataProvider();

    public EntityIndexDataProvider(List<SourceDataFile> sourceDataFiles) {
        this.sourceDataFiles = sourceDataFiles;
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
        if (data.containsKey(ENTITY_INDEX_TYPE)) return (String) data.get(ENTITY_INDEX_TYPE);
        return "";
    }

    public static void setEntityIndexType(SourceDataFile data, String type) {
        data.put(ENTITY_INDEX_TYPE, type);
    }

    public static boolean isCustom(SourceDataFile data) {
        if (data.containsKey(ENTITY_INDEX_IS_CUSTOM)) return (boolean) data.get(ENTITY_INDEX_IS_CUSTOM);
        return false;
    }

    public static void isCustom(SourceDataFile data, boolean isCustom) {
        data.put(ENTITY_INDEX_IS_CUSTOM, isCustom);
    }

    public static List<MethodData> getCustomMethods(SourceDataFile data) {
        if (data.containsKey(ENTITY_INDEX_CUSTOM_METHODS))
            return (List<MethodData>) data.get(ENTITY_INDEX_CUSTOM_METHODS);
        return new ArrayList<>();
    }

    public static void setCustomMethods(SourceDataFile data, List<MethodData> methods) {
        data.put(ENTITY_INDEX_CUSTOM_METHODS, methods);
    }

    public static String getEntityIndexName(SourceDataFile data) {
        if (data.containsKey(ENTITY_INDEX_NAME)) return (String) data.get(ENTITY_INDEX_NAME);
        return "";
    }

    public static void setEntityIndexName(SourceDataFile data, String name) {
        data.put(ENTITY_INDEX_NAME, name);
    }

    public static List<String> getContextNames(SourceDataFile data) {
        if (data.containsKey(ENTITY_INDEX_CONTEXT_NAMES)) return (List<String>) data.get(ENTITY_INDEX_CONTEXT_NAMES);
        return new ArrayList<>();
    }

    public static void setContextNames(SourceDataFile data, List<String> contextNames) {
        data.put(ENTITY_INDEX_CONTEXT_NAMES, contextNames);
    }

    public static String getKeyType(SourceDataFile data) {
        if (data.containsKey(ENTITY_INDEX_KEY_TYPE)) return (String) data.get(ENTITY_INDEX_KEY_TYPE);
        return "";
    }

    public static void setKeyType(SourceDataFile data, String type) {
        data.put(ENTITY_INDEX_KEY_TYPE, type);
    }

    public static String getComponentType(SourceDataFile data) {
        if (data.containsKey(ENTITY_INDEX_COMPONENT_TYPE)) return (String) data.get(ENTITY_INDEX_COMPONENT_TYPE);
        return "";
    }

    public static void setComponentType(SourceDataFile data, String type) {
        data.put(ENTITY_INDEX_COMPONENT_TYPE, type);
    }

    public static String getMemberName(SourceDataFile data) {
        if (data.containsKey(ENTITY_INDEX_MEMBER_NAME)) return (String) data.get(ENTITY_INDEX_MEMBER_NAME);
        return "";
    }

    public static void setMemberName(SourceDataFile data, String memberName) {
        data.put(ENTITY_INDEX_MEMBER_NAME, memberName);
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
        return _contextsComponentDataProvider.getDefaultProperties();
    }

    @Override
    public void configure(Properties properties) {
        _codeGeneratorConfig.configure(properties);
        _contextsComponentDataProvider.configure(properties);
    }

    @Override
    public List<SourceDataFile> getData() {
        List<SourceDataFile> entityIndexData = sourceDataFiles.stream()
                .filter(s -> !s.getFileContent().isAbstract())
                .filter(s -> s.getFileContent().hasInterface(IComponent.class))
                .filter(s -> s.getFileContent().getFields().stream().anyMatch(f -> f.hasAnnotation(EntityIndex.class)))
                .map(s -> createEntityIndexData(s, s.getFileContent().getFields()))
                .collect(Collectors.toList());

        List<SourceDataFile> customEntityIndexData = sourceDataFiles.stream()
                .filter(s -> !s.getFileContent().isAbstract())
                .filter(s -> s.getFileContent().hasAnnotation(CustomEntityIndex.class))
                .map(s -> createCustomEntityIndexData(s))
                .collect(Collectors.toList());

        entityIndexData.addAll(customEntityIndexData);
        return entityIndexData;
    }

    private SourceDataFile createEntityIndexData(SourceDataFile data, List<FieldSource<JavaClassSource>> infos) {

        FieldSource<JavaClassSource> info = infos.stream()
                .filter(i -> i.hasAnnotation(EntityIndex.class))
                .collect(singletonCollector());

        setEntityIndexType(data, info.getAnnotation(EntityIndex.class).getName());
        isCustom(data, false);
        setEntityIndexName(data, data.getFileContent().getCanonicalName());
        setKeyType(data, info.getType().getName());
        setComponentType(data, data.getFileContent().getCanonicalName());
        setMemberName(data, info.getName());
        setContextNames(data, _contextsComponentDataProvider.getContextNamesOrDefault(data.getFileContent()));

        return data;
    }

    SourceDataFile createCustomEntityIndexData(SourceDataFile data) {

        AnnotationSource annotation = data.getFileContent().getAnnotation(CustomEntityIndex.class);

        setEntityIndexType(data, data.getFileContent().getName());
        isCustom(data, true);
        setEntityIndexName(data, data.getFileContent().getCanonicalName());

        setContextNames(data, Arrays.asList(annotation.getStringValue()));

        List<MethodData> getMethods = data.getFileContent().getMethods().stream()
                .filter(m -> m.isPublic())
                .filter(m -> m.hasAnnotation(EntityIndexGetMethod.class))
                .map(m -> new MethodData(m.getReturnType(), m.getName(),
                        m.getParameters().stream().map(p -> new MemberData(p.getType(), p.getName(),
                                p.getAnnotations().size() > 0 ? p.getAnnotations().get(0) : null)).collect(Collectors.toList()),
                        m.getAnnotation(EntityIndexGetMethod.class)
                ))
                .collect(Collectors.toList());

        setCustomMethods(data, getMethods);
        return data;

    }

}
