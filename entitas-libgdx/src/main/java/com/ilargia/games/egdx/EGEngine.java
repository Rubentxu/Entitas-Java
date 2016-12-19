package com.ilargia.games.egdx;


import com.ilargia.games.egdx.interfaces.Engine;
import com.ilargia.games.egdx.interfaces.managers.Manager;
import com.ilargia.games.entitas.Systems;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;

public abstract class EGEngine implements Engine {

    public Object2ObjectMap<Class<? extends Manager>, Manager> _managers;
    public Systems _systems;
    private int _errorState;
    private boolean _hasShutdown;


    public EGEngine(Systems systems, Manager... managers) {
        _systems = systems;
        for (Manager manager : managers) {
            _managers.put(manager.getClass(), manager);
        }

    }

    public void disposeSystems() {
        _systems.clearSystems();
    }



    @Override
    public void processInput() {

    }

    @Override
    public void update(float deltaTime) {
        _systems.execute(deltaTime);
    }

    @Override
    public void render() {

    }

    @Override
    public void shutDownEngine(int errorCode) {
        _errorState = errorCode;
        _hasShutdown = true;
        disposeSystems();
        for (Manager manager: _managers.values()) {
            manager.dispose();
        }
        _managers.clear();
        _managers = null;
        _systems = null;

    }

    @Override
    public int getErrorState() {
        return _errorState;
    }

    @Override
    public boolean hasShutdown() {
        return _hasShutdown;
    }

    @Override
    public <M extends Manager> M getManager(Class<M> clazz) {
        return (M) _managers.get(clazz);

    }


}
