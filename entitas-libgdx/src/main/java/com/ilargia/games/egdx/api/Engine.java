package com.ilargia.games.egdx.api;

import com.ilargia.games.egdx.api.managers.Manager;

public interface Engine {

    void initialize();

    void dispose();

    <M extends Manager> Engine addManager(M manager);

    <M extends Manager> M getManager(Class<M> clazz);

}
