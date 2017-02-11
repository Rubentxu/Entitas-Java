package com.ilargia.games.egdx.api;

import com.ilargia.games.egdx.api.managers.Manager;

public interface Engine {

    public void dispose();

    public <M extends Manager> M getManager(Class<M> clazz);

    public <M extends Manager> Engine addManager(M manager);


}
