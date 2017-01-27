package com.ilargia.games.core;

import com.ilargia.games.entitas.api.ContextInfo;
import com.ilargia.games.entitas.api.FactoryEntity;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IContext;

import java.util.Stack;


public class Contexts  {

    public CoreContext core;

    public Contexts() {
        core = createCoreContext();
    }

    public CoreContext createCoreContext() {
        return new CoreContext(CoreComponentIds.totalComponents, 0,
                new ContextInfo("Core", CoreComponentIds.componentNames(),
                        CoreComponentIds.componentTypes()), factoryEntity()   );
    }

    public IContext[] allContexts() {
        return new IContext[]{core};
    }

    public FactoryEntity<CoreEntity> factoryEntity() {
        return (int totalComponents, Stack<IComponent>[] componentContexts,
                ContextInfo contextInfo) -> {
            return new CoreEntity(totalComponents, componentContexts, contextInfo);
        };
    }
}