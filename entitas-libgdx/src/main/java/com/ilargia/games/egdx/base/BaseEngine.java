package com.ilargia.games.egdx.base;


import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.managers.Manager;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.IContexts;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

public class BaseEngine implements Engine, IContexts {

    public Object2ObjectMap<Class<? extends Manager>, Manager> _managers;

    public BaseEngine() {
        _managers = new Object2ObjectArrayMap();

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
