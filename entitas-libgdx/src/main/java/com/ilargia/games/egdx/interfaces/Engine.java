package com.ilargia.games.egdx.interfaces;

import com.ilargia.games.egdx.interfaces.managers.Manager;

public interface Engine {

    public void configure(String [] args);

    public void init();

    public void processInput();

    public void update(float deltaTime);

    public void render();

    public void shutDownEngine(int errorCode);

    public int getErrorState();

    public boolean hasShutdown();

    public <M extends Manager> M getManager(Class<M> clazz);



}
