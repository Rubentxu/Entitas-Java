package com.ilargia.games.egdx.base;


import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.managers.Manager;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IContexts;
import com.ilargia.games.entitas.factories.CollectionsFactories;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class BaseEngine implements Engine, IContexts {

    private final EntitasCollections collectionsImpl;
    public Map<Class<? extends Manager>, Manager> _managers;

    public BaseEngine(CollectionsFactories factories) {
        collectionsImpl = new EntitasCollections(factories);
        _managers = EntitasCollections.createMap(Class.class, Manager.class);
    }

    @Override
    public void dispose() {
        for (Manager manager : _managers.values()) {
            manager.dispose();
        }
        _managers.clear();
        _managers = null;

    }

    @Override
    public <M extends Manager> M getManager(Class<M> clazz) {
        return (M) _managers.get(clazz);

    }

    @Override
    public <M extends Manager> Engine addManager(M manager) {
        _managers.put(manager.getClass(), manager);
        return this;
    }


    @Override
    public IContext[] allContexts() {
        return new IContext[0];
    }
}
