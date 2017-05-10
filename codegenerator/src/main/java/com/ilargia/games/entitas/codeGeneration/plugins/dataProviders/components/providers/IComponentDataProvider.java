package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;


import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

public interface IComponentDataProvider {
    void provide(JavaClassSource type, ComponentData data);
}
