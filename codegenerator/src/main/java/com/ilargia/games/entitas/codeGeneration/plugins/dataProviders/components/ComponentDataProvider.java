package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;
import com.ilargia.games.entitas.codeGeneration.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers.*;
import com.ilargia.games.entitas.codeGenerator.interfaces.configuration.IConfigurable;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentDataProvider implements ICodeGeneratorDataProvider, IConfigurable {

    List<SourceDataFile> _sources;
    List<IComponentDataProvider> _dataProviders;
    private CodeGeneratorConfig _codeGeneratorConfig;
    //    private AssembliesConfig _assembliesConfig = new AssembliesConfig();
    private ContextsComponentDataProvider _contextsComponentDataProvider = new ContextsComponentDataProvider();


    public ComponentDataProvider(List<SourceDataFile> sources) {
        this(sources, getComponentDataProviders());
    }

    protected ComponentDataProvider(List<SourceDataFile> types, List<IComponentDataProvider> dataProviders) {
        _sources = types;
        _dataProviders = dataProviders;
        _codeGeneratorConfig = new CodeGeneratorConfig(getDefaultProperties());
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

        _contextsComponentDataProvider.configure(properties);
    }

    @Override
    public List<CodeGeneratorData> getData() {

        List<SourceDataFile> dataFromComponents = _sources.stream()
                .filter(s -> s.fileContent.hasInterface(IComponent.class))
                .filter(s -> !s.fileContent.isAbstract())
                .map(s -> createDataForComponent(s))
                .collect(Collectors.toList());

        List<SourceDataFile> dataFromNonComponents = _sources.stream()
                .filter(s -> s.fileContent.hasInterface(IComponent.class))
                .filter(s -> hasContexts(s))

                .reduce(new ArrayList<SourceDataFile>(), (a, b) -> {
                    a.addAll(b);
                    return a;
                });

        List<String> generatedComponentsLookup = dataFromNonComponents.stream()
                .map(data -> data.fileContent.getCanonicalName())
                .collect(Collectors.toList());

        return Stream.concat(dataFromComponents.stream()
                        .filter(data -> !generatedComponentsLookup.contains(data.fileContent.getCanonicalName())),
                dataFromNonComponents.stream()).collect(Collectors.toList());


    }

    private boolean hasContexts(SourceDataFile sourceData) {
        return _contextsComponentDataProvider.extractContextNames(sourceData.fileContent).size() != 0;
    }

    private SourceDataFile createDataForComponent(SourceDataFile data) {
        for (IComponentDataProvider dataProvider : _dataProviders) {
            dataProvider.provide(data);
        }
        return data;
    }



    List<String> getComponentNames(SourceDataFile data) {
        if (data.fileContent.hasAnnotation("CustomComponentName")) {
            return Arrays.asList(data.fileContent.getAnnotation("CustomComponentName").getStringArrayValue("componentNames"));

        } else {
            return new ArrayList<>();
        }

    }

    static List<IComponentDataProvider> getComponentDataProviders() {
        return new ArrayList<IComponentDataProvider>() {{
            add(new ComponentTypeComponentDataProvider());
            add(new MemberDataComponentDataProvider());
            add(new ConstructorDataComponentDataProvider());
            add(new EnumsDataComponentDataProvider());
            add(new ContextsComponentDataProvider());
            add(new GenericsDataComponentDataProvider());
            add(new IsUniqueComponentDataProvider());
            add(new ShouldGenerateComponentComponentDataProvider());

        }};
    }
}
