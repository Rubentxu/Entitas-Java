package com.ilargia.games.entitas.codeGeneration.dataProviders.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGeneration.config.AbstractConfigurableConfig;
import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.config.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeDataProvider;
import com.ilargia.games.entitas.codeGeneration.dataProviders.components.providers.*;
import com.ilargia.games.entitas.codeGeneration.interfaces.IConfigurable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentDataProvider extends AbstractConfigurableConfig implements ICodeDataProvider {

    List<SourceDataFile> _sources;
    List<IComponentDataProvider> _dataProviders;
    private CodeGeneratorConfig _codeGeneratorConfig;
    //    private AssembliesConfig _assembliesConfig = new AssembliesConfig();
    private ContextsDataProvider _contextsComponentDataProvider = new ContextsDataProvider();


    public ComponentDataProvider(List<SourceDataFile> sources) {
        this(sources, getComponentDataProviders());
    }

    protected ComponentDataProvider(List<SourceDataFile> sources, List<IComponentDataProvider> dataProviders) {
        _sources = sources;
        _dataProviders = dataProviders;
        _codeGeneratorConfig = new CodeGeneratorConfig();
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

       // _contextsComponentDataProvider.configure(properties);
    }

    @Override
    public List<SourceDataFile> getData() {

        List<SourceDataFile> dataFromComponents = _sources.stream()
                .filter(s -> s.source.hasInterface(IComponent.class))
                .filter(s -> !s.source.isAbstract())
                .map(s -> createDataForComponent(s))
                .collect(Collectors.toList());

        List<SourceDataFile> dataFromNonComponents = _sources.stream()
                .filter(s -> !s.source.hasInterface(IComponent.class))
                .filter(s -> hasContexts(s))
                .map(s -> createDataForComponent(s))
                .collect(Collectors.toList());

        List<String> generatedComponentsLookup = dataFromNonComponents.stream()
                .map(data -> data.source.getCanonicalName())
                .collect(Collectors.toList());

        return Stream.concat(dataFromComponents.stream()
                        .filter(data -> !generatedComponentsLookup.contains(data.source.getCanonicalName())),
                dataFromNonComponents.stream()).collect(Collectors.toList());


    }

    private boolean hasContexts(SourceDataFile sourceData) {
        return _contextsComponentDataProvider.extractContextNames(sourceData.source).size() != 0;
    }

    private SourceDataFile createDataForComponent(SourceDataFile data) {
        for (IComponentDataProvider dataProvider : _dataProviders) {
            dataProvider.provide(data);
        }
        return data;
    }


    List<String> getComponentNames(SourceDataFile data) {
        if (data.source.hasAnnotation("CustomComponentName")) {
            return Arrays.asList(data.source.getAnnotation("CustomComponentName").getStringArrayValue("componentNames"));

        } else {
            return new ArrayList<>();
        }

    }

    static List<IComponentDataProvider> getComponentDataProviders() {
        return new ArrayList<IComponentDataProvider>() {{
            add(new ComponentTypeDataProvider());
            add(new MemberDataProvider());
            add(new ConstructorDataProvider());
            add(new EnumsDataProvider());
            add(new ContextsDataProvider());
            add(new GenericsDataProvider());
            add(new IsUniqueDataProvider());
            add(new ShouldGenerateComponentDataProvider());
            add(new ShouldGenerateMethodsDataProvider());

        }};
    }
}
