package ilargia.entitas.codeGeneration.plugins.dataProviders.entityIndex;


import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.dataProviders.ProviderUtils;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsDataProvider;
import ilargia.entitas.codeGenerator.annotations.CustomEntityIndex;
import ilargia.entitas.codeGenerator.annotations.EntityIndex;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EntityIndexDataProvider implements ICodeGeneratorDataProvider, IConfigurable {

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
    private IAppDomain appDomain;

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

    @Override
    public void setAppDomain(IAppDomain appDomain) {
        this.appDomain = appDomain;
    }

    public static String getEntityIndexType(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_TYPE)) return (String) data.get(ENTITY_INDEX_TYPE);
        return "";
    }

    public static void setEntityIndexType(ComponentData data, String type) {
        data.put(ENTITY_INDEX_TYPE, type);
    }

    public static boolean isCustom(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_IS_CUSTOM)) return (boolean) data.get(ENTITY_INDEX_IS_CUSTOM);
        return false;
    }

    public static void isCustom(ComponentData data, boolean isCustom) {
        data.put(ENTITY_INDEX_IS_CUSTOM, isCustom);
    }

    public static List<MethodSource<JavaClassSource>> getCustomMethods(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_CUSTOM_METHODS))
            return (List<MethodSource<JavaClassSource>>) data.get(ENTITY_INDEX_CUSTOM_METHODS);
        return new ArrayList<>();
    }

    public static void setCustomMethods(ComponentData data, List<MethodSource<JavaClassSource>> methods) {
        data.put(ENTITY_INDEX_CUSTOM_METHODS, methods);
    }

    public static String getEntityIndexName(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_NAME)) return (String) data.get(ENTITY_INDEX_NAME);
        return "";
    }

    public static void setEntityIndexName(ComponentData data, String name) {
        data.put(ENTITY_INDEX_NAME, name);
    }

    public static List<String> getContextNames(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_CONTEXT_NAMES)) return (List<String>) data.get(ENTITY_INDEX_CONTEXT_NAMES);
        return new ArrayList<>();
    }

    public static void setContextNames(ComponentData data, List<String> contextNames) {
        data.put(ENTITY_INDEX_CONTEXT_NAMES, contextNames);
    }

    public static String getKeyType(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_KEY_TYPE)) return (String) data.get(ENTITY_INDEX_KEY_TYPE);
        return "";
    }

    public static void setKeyType(ComponentData data, String type) {
        data.put(ENTITY_INDEX_KEY_TYPE, type);
    }

    public static String getComponentType(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_COMPONENT_TYPE)) return (String) data.get(ENTITY_INDEX_COMPONENT_TYPE);
        return "";
    }

    public static void setComponentType(ComponentData data, String type) {
        data.put(ENTITY_INDEX_COMPONENT_TYPE, type);
    }

    public static String getMemberName(ComponentData data) {
        if (data.containsKey(ENTITY_INDEX_MEMBER_NAME)) return (String) data.get(ENTITY_INDEX_MEMBER_NAME);
        return "";
    }

    public static void setMemberName(ComponentData data, String memberName) {
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
    public List<CodeGeneratorData> getData() {
        List<ComponentData> datas = ProviderUtils.getComponentDatas(appDomain, _codeGeneratorConfig.getPackages());

        List<ComponentData> entityIndexData = datas.stream()
                .filter(s -> !s.getSource().isAbstract())
                .filter(s -> s.getSource().hasInterface(IComponent.class))
                .filter(s -> s.getSource().getFields().stream().anyMatch(f -> f.hasAnnotation(EntityIndex.class)))
                .map(s -> createEntityIndexData(s, s.getSource().getFields()))
                .collect(Collectors.toList());

        List<ComponentData> customEntityIndexData = datas.stream()
                .filter(s -> !s.getSource().isAbstract())
                .filter(s -> s.getSource().hasAnnotation(CustomEntityIndex.class))
                .map(s -> createCustomEntityIndexData(s))
                .collect(Collectors.toList());

        return Stream.concat(entityIndexData.stream(),
                customEntityIndexData.stream())
                .collect(Collectors.toList());
    }

    private ComponentData createEntityIndexData(ComponentData data, List<FieldSource<JavaClassSource>> infos) {

        FieldSource<JavaClassSource> info = infos.stream()
                .filter(i -> i.hasAnnotation(EntityIndex.class))
                .collect(singletonCollector());

        setEntityIndexType(data, info.getAnnotation(EntityIndex.class).getName());
        isCustom(data, false);
        setEntityIndexName(data, data.getSource().getCanonicalName());
        setKeyType(data, info.getType().getName());
        setComponentType(data, data.getSource().getCanonicalName());
        setMemberName(data, info.getName());
        setContextNames(data, _contextsComponentDataProvider.getContextNamesOrDefault(data.getSource()));

        return data;
    }

    ComponentData createCustomEntityIndexData(ComponentData data) {
        AnnotationSource annotation = data.getSource().getAnnotation(CustomEntityIndex.class);

        setEntityIndexType(data, data.getSource().getName());
        isCustom(data, true);
        setEntityIndexName(data, data.getSource().getCanonicalName());

        setContextNames(data, Arrays.asList(annotation.getStringValue()));
        setCustomMethods(data, data.getSource().getMethods());
        return data;
    }

}
