package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.entityIndex;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;
import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGeneration.plugins.data.MethodData;
import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers.ContextsComponentDataProvider;
import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import com.ilargia.games.entitas.index.AbstractEntityIndex;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.awt.SystemColor.info;

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
    private ContextsComponentDataProvider _contextsComponentDataProvider = new ContextsComponentDataProvider();

    List<JavaClassSource> _types;

    public EntityIndexDataProvider() {
        this(null);
    }

    public EntityIndexDataProvider(List<JavaClassSource> types) {
        _types = types;
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
    public List<CodeGeneratorData> getData() {
        _types.stream()
                .filter(type -> !type.isAbstract())
                .filter(type -> type.hasInterface(IComponent.class))
                .collect(Collectors.toMap(Function.identity(), JavaClassSource::getFields))
                .forEach((k ,v) ->  createEntityIndexData(k, v));
        return null;
    }

    private EntityIndexData createEntityIndexData(JavaClassSource type, List<FieldSource<JavaClassSource>> infos) {
        EntityIndexData data = new EntityIndexData();

        info = infos.stream()
                .filter(i -> i.hasAnnotation("AbstractEntityIndex");
        var attribute = (AbstractEntityIndexAttribute)info.attributes.Single(attr => attr.attribute is AbstractEntityIndexAttribute).attribute;

        setEntityIndexType(data, getEntityIndexType(attribute));
        isCustom(data, false);
        setEntityIndexName(data, type.getCanonicalName());
        setKeyType(data, info.type.ToCompilableString());
        setComponentType(data, type.getCanonicalName());
        setMemberName(data, info.);
        setContextNames(data, _contextsComponentDataProvider.getContextNamesOrDefault(type));

        return data;
    }


    public static String getEntityIndexType(EntityIndexData data) {
        return (String)data.get(ENTITY_INDEX_TYPE);
    }

    public static void setEntityIndexType(EntityIndexData data, String type) {
        data.put(ENTITY_INDEX_TYPE, type);
    }

    public static boolean isCustom(EntityIndexData data) {
        return (boolean)data.get(ENTITY_INDEX_IS_CUSTOM);
    }

    public static void isCustom(EntityIndexData data, boolean isCustom) {
        data.put(ENTITY_INDEX_IS_CUSTOM, isCustom);
    }

    public static MethodData[] getCustomMethods(EntityIndexData data) {
        return (MethodData[])data.get(ENTITY_INDEX_CUSTOM_METHODS);
    }

    public static void setCustomMethods(EntityIndexData data, MethodData[] methods) {
        data.put(ENTITY_INDEX_CUSTOM_METHODS, methods);
    }

    public static String getEntityIndexName(EntityIndexData data) {
        return (String)data.get(ENTITY_INDEX_NAME);
    }

    public static void setEntityIndexName(EntityIndexData data, String name) {
        data.put(ENTITY_INDEX_NAME, name);
    }

    public static List<String> getContextNames(EntityIndexData data) {
        return (List<String>)data.get(ENTITY_INDEX_CONTEXT_NAMES);
    }

    public static void setContextNames(EntityIndexData data, List<String> contextNames) {
        data.put(ENTITY_INDEX_CONTEXT_NAMES, contextNames);
    }

    public static String getKeyType(EntityIndexData data) {
        return (String)data.get(ENTITY_INDEX_KEY_TYPE);
    }

    public static void setKeyType(EntityIndexData data, String type) {
        data.put(ENTITY_INDEX_KEY_TYPE, type);
    }

    public static String getComponentType(EntityIndexData data) {
        return (String)data.get(ENTITY_INDEX_COMPONENT_TYPE);
    }

    public static void setComponentType(EntityIndexData data, String type) {
        data.put(ENTITY_INDEX_COMPONENT_TYPE, type);
    }

    public static String getMemberName(EntityIndexData data) {
        return (String)data.get(ENTITY_INDEX_MEMBER_NAME);
    }

    public static void setMemberName(EntityIndexData data, String memberName) {
        data.put(ENTITY_INDEX_MEMBER_NAME, memberName);
    }

}
