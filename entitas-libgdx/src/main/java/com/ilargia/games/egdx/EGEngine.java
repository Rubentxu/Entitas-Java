package com.ilargia.games.egdx;


import com.badlogic.gdx.utils.ObjectMap;
import com.ilargia.games.egdx.interfaces.Engine;
import com.ilargia.games.egdx.interfaces.Manager;
import com.ilargia.games.entitas.Systems;

public class EGEngine implements Engine{

    private ObjectMap<Class<? extends Manager>, Manager> _managers;
    private int _errorState;
    private boolean _hasShutdown;
    private Systems _systems;

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
    public void configure(String[] args) {

    }

    @Override
    public void initSystems() {

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
        for (ObjectMap.Entry<Class<? extends Manager>, Manager> manager : _managers) {
            manager.value.dispose();
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
