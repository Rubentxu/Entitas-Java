package ilargia.entitas.codeGeneration.plugins.dataProviders.components;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGeneration.config.AbstractConfigurableConfig;
import ilargia.entitas.codeGeneration.config.CodeGeneratorConfig;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import ilargia.entitas.codeGeneration.interfaces.IConfigurable;
import ilargia.entitas.codeGeneration.plugins.dataProviders.ProviderUtils;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers.*;
import ilargia.entitas.codeGenerator.annotations.CustomComponentName;
import org.jboss.forge.roaster.model.impl.FieldImpl;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentDataProvider extends AbstractConfigurableConfig implements ICodeGeneratorDataProvider {

    List<IComponentDataProvider> _dataProviders;
    private CodeGeneratorConfig _codeGeneratorConfig;
    private ContextsDataProvider _contextsComponentDataProvider = new ContextsDataProvider();
    private IAppDomain appDomain;

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
    public void setAppDomain(IAppDomain appDomain) {
        this.appDomain = appDomain;
    }

    @Override
    public List<CodeGeneratorData> getData() {
        List<ComponentData> datas = ProviderUtils.getComponentDatas(appDomain, _codeGeneratorConfig.getPackages());

        List<ComponentData> dataFromComponents = datas.stream()
                .filter(s -> s.getSource().hasInterface(IComponent.class))
                .filter(s -> !s.getSource().isAbstract())
                .map(s -> providedDataForComponent(s))
                .collect(Collectors.toList());

        List<ComponentData> dataFromNonComponents = datas.stream()
                .filter(s -> !s.getSource().hasInterface(IComponent.class))
                .filter(s -> !s.getSource().isAbstract())
                .filter(s -> _contextsComponentDataProvider.extractContextNames(s.getSource()).size() != 0)
                .flatMap(s -> provideDataForNonComponent(s).stream())
                .collect(Collectors.toList());

        return Stream.concat(dataFromComponents.stream(),
                dataFromNonComponents.stream())
                .sorted((a, b)-> a.getSource().getName().compareTo(b.getSource().getName()))
                .collect(Collectors.toList());

    }

    private ComponentData providedDataForComponent(ComponentData data) {
        for (IComponentDataProvider dataProvider : _dataProviders) {
            dataProvider.provide(data);
        }
        return data;
    }

    List<ComponentData> provideDataForNonComponent(ComponentData data) {
        return getComponentNames(data.getSource()).stream()
                .map(componentName -> {
                    ComponentTypeDataProvider.setFullTypeName(data, componentName);
                    MemberDataProvider.setMemberData(data, new ArrayList<FieldSource<JavaClassSource>>() {{
                        add(new FieldImpl<JavaClassSource>(data.getSource()));
                    }});
                    return data;
                }).collect(Collectors.toList());

    }

    List<String> getComponentNames(JavaClassSource type) {
        AnnotationSource<JavaClassSource> annotation = type.getAnnotation(CustomComponentName.class);
        if (annotation != null) {
            return Arrays.asList(annotation.getStringArrayValue("componentNames"));

        } else {
            return new ArrayList<>();
        }

    }

}
