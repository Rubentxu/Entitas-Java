package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;
import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGeneration.plugins.data.MemberData;
import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers.IComponentDataProvider;
import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentDataProvider implements ICodeGeneratorDataProvider, IConfigurable {

    List<Class> _types;
    List<IComponentDataProvider> _dataProviders;
    private CodeGeneratorConfig _codeGeneratorConfig = new CodeGeneratorConfig();
//    private AssembliesConfig _assembliesConfig = new AssembliesConfig();
//    private ContextsComponentDataProvider _contextsComponentDataProvider = new ContextsComponentDataProvider();


    public ComponentDataProvider() {
        this(null);
    }

    public ComponentDataProvider(List<Class> types) {
        this(types, getComponentDataProviders());
    }

    protected ComponentDataProvider(List<Class> types, List<IComponentDataProvider> dataProviders) {
        _types = types;
        _dataProviders = dataProviders;
    }

    @Override
    public String getName() {
        return "Component";
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

    public static Map<String, String> propertiesToMap(Properties props) {
        HashMap<String, String> hm = new HashMap<String, String>();
        Enumeration<Object> e = props.keys();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            hm.put(s, props.getProperty(s));
        }
        return hm;
    }

    @Override
    public Properties getDefaultProperties() {
        return _dataProviders.stream()
                .filter(p -> p.getClass().isAssignableFrom(IConfigurable.class))
                .map(p -> (IConfigurable) p)
                .map(p -> p.getDefaultProperties())
                .reduce(new Properties(), (a, b) -> {
                    a.putAll(propertiesToMap(b));
                    return a;
                });
    }

    @Override
    public void configure(Properties properties) {
        _codeGeneratorConfig.configure(properties);
        _dataProviders.stream()
                .filter(p -> p.getClass().isAssignableFrom(IConfigurable.class))
                .map(p -> (IConfigurable) p)
                .forEach(p -> p.configure(properties));


//        _contextsComponentDataProvider.configure(properties);
    }

    @Override
    public List<CodeGeneratorData> getData() {
//        if (_types == null) {
//            _types = PluginUtil
//                    .GetAssembliesResolver(_assembliesConfig.assemblies, _codeGeneratorConfig.searchPaths)
//                    .GetTypes();
//        }

        List<ComponentData> dataFromComponents = _types.stream()
                .filter(type -> type.isAssignableFrom(IComponent.class))
                .filter(type -> !Modifier.isAbstract(type.getModifiers()))
                .map(type -> createDataForComponent(type))
                .collect(Collectors.toList());

        List<ComponentData> dataFromNonComponents = _types.stream()
                .filter(type -> type.isAssignableFrom(IComponent.class))
                .filter(type -> hasContexts(type))
                .map(type -> createDataForNonComponent(type))
                .reduce(new ArrayList<ComponentData>(), (a, b)-> {
                            a.addAll(b);
                            return a;
                        });

        List<String> generatedComponentsLookup = dataFromNonComponents.stream()
                .map(data -> data.getFullTypeName())
                .collect(Collectors.toList());

        return Stream.concat(dataFromComponents.stream()
                .filter(data -> !generatedComponentsLookup.contains(data.getFullTypeName())),
                dataFromNonComponents.stream()).collect(Collectors.toList());


    }

    private boolean hasContexts(Class type) {
        return _contextsComponentDataProvider.getContextNames(type).ength != 0;
    }

    private ComponentData createDataForComponent(Class type) {
        ComponentData data = new ComponentData();
        for (IComponentDataProvider dataProvider : _dataProviders) {
            dataProvider.provide(type, data);
        }
        return data;
    }

    List<ComponentData> createDataForNonComponent(Class type) {
        return getComponentNames(type).stream()
                .map(componentName -> {
                    ComponentData data = createDataForComponent(type);
                    data.setFullTypeName(componentName);
                    data.setMemberData(new ArrayList<MemberData>(){{
                            add(new MemberData(type.getSimpleName(), "value"));
                    }});
                    return data;
                }).collect(Collectors.toList());

    }

    List<String> getComponentNames(Class type) {
//        var attr = Attribute
//                .GetCustomAttributes(type)
//                .OfType<CustomComponentNameAttribute>()
//                .SingleOrDefault();
//
//        if (attr == null) {
//            return new [] { type.ToCompilableString().ShortTypeName().AddComponentSuffix() };
//        }
//
//        return attr.componentNames;
        return new ArrayList<>();
    }

    static List<IComponentDataProvider> getComponentDataProviders() {
        return new ArrayList<IComponentDataProvider>() {
//                new ComponentTypeComponentDataProvider(),
//                new MemberDataComponentDataProvider(),
//                new ContextsComponentDataProvider(),
//                new IsUniqueComponentDataProvider(),
//                new CustomPrefixComponentDataProvider(),
//                new ShouldGenerateComponentComponentDataProvider(),
//                new ShouldGenerateMethodsComponentDataProvider(),
//                new ShouldGenerateComponentIndexComponentDataProvider()
        };
    }
}
