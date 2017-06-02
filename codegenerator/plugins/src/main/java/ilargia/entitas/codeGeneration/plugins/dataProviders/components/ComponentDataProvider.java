package ilargia.entitas.codeGeneration.plugins.dataProviders.components;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGeneration.config.AbstractConfigurableConfig;
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.plugins.data.MemberData;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.IComponentDataProvider;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.*;
import ilargia.entitas.codeGeneration.utils.ClassFinder;
import ilargia.entitas.codeGenerator.annotations.CustomComponentName;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentDataProvider extends AbstractConfigurableConfig implements ICodeGeneratorDataProvider {

    List<Class> _types;
    List<IComponentDataProvider> _dataProviders;
    private CodeGeneratorConfig _codeGeneratorConfig;
    private ContextsDataProvider _contextsComponentDataProvider = new ContextsDataProvider();

    public ComponentDataProvider() {
        this(getComponentDataProviders());
    }

    protected ComponentDataProvider(List<IComponentDataProvider> dataProviders) {
        _dataProviders = dataProviders;
        _codeGeneratorConfig = new CodeGeneratorConfig();
    }

    public static Map<String, String> propertiesToMap(Properties props) {
        HashMap<String, String> hm = new HashMap<>();
        Enumeration<Object> e = props.keys();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            hm.put(s, props.getProperty(s));
        }
        return hm;
    }

    static List<IComponentDataProvider> getComponentDataProviders() {
        return new ArrayList<IComponentDataProvider>() {{
            add(new ComponentTypeDataProvider());
            add(new ConstructorDataProvider());
            add(new ContextsDataProvider());
            add(new CustomPrefixDataProvider());
            add(new EnumsDataProvider());
            add(new GenericsDataProvider());
            add(new IsUniqueDataProvider());
            add(new MemberDataProvider());
            add(new ShouldGenerateComponentDataProvider());
            add(new ShouldGenerateMethodsDataProvider());
        }};
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

    @Override
    public Properties getDefaultProperties() {
        _codeGeneratorConfig.configure(properties);
        return _dataProviders.stream()
                .filter(p -> p instanceof IConfigurable)
                .map(p -> (IConfigurable) p)
                .map(p -> p.getDefaultProperties())
                .reduce(properties, (a, b) -> {
                    a.putAll(propertiesToMap(b));
                    return a;
                });

    }

    @Override
    public void configure(Properties properties) {
        super.configure(properties);
        _codeGeneratorConfig.configure(properties);
        _dataProviders.stream()
                .filter(p -> p instanceof IConfigurable)
                .map(p -> (IConfigurable) p)
                .forEach(p -> p.configure(properties));

        _contextsComponentDataProvider.configure(properties);
    }

    @Override
    public List<CodeGeneratorData> getData() {
        if (_types == null) {
            _types = _codeGeneratorConfig.getPackages().stream()
                    .flatMap(pkg-> ClassFinder.findRecursive(pkg).stream())
                    .collect(Collectors.toList());                  
        }

        List<ComponentData> dataFromComponents = _types.stream()
                .filter(s -> s.isAssignableFrom(IComponent.class))
                .filter(s -> !Modifier.isAbstract(s.getModifiers()))
                .map(s -> createDataForComponent(s))
                .collect(Collectors.toList());

        List<ComponentData> dataFromNonComponents = _types.stream()
                .filter(s -> !s.isAssignableFrom(IComponent.class))
                .filter(s -> !Modifier.isInterface(s.getModifiers()))
                .filter(s -> hasContexts(s))
                .flatMap(s -> createDataForNonComponent(s).stream())
                .collect(Collectors.toList());


        return Stream.concat(dataFromComponents.stream(),
                dataFromNonComponents.stream())
                .collect(Collectors.toList());


    }

    private boolean hasContexts(Class type) {
        return _contextsComponentDataProvider.extractContextNames(type).size() != 0;
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
                .map( componentName -> {
                    ComponentData data = createDataForComponent(type);
                    ComponentTypeDataProvider.setFullTypeName(data, componentName);
                    MemberDataProvider.setMemberData(data, new ArrayList<MemberData>() {{
                        add(new MemberData(type, "value", null));
                    }});
                    return data;
                }).collect(Collectors.toList());

    }

    List<String> getComponentNames(Class type) {
        CustomComponentName annotation = (CustomComponentName) type.getAnnotation(CustomComponentName.class);
        if (annotation != null) {
            return Arrays.asList(annotation.componentNames());

        } else {
            return new ArrayList<>();
        }

    }
}
