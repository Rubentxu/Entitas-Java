package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;


import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;

public interface IComponentDataProvider {
    void provide(Class type, ComponentData data);
}
