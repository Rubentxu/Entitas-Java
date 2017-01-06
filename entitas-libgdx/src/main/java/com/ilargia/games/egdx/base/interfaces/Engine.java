package com.ilargia.games.egdx.base.interfaces;

import com.ilargia.games.egdx.base.interfaces.managers.Manager;

public interface Engine {

    public void dispose();

    public <M extends Manager> M getManager(Class<M> clazz);

    public <M extends Manager> Engine addManager( M manager);


}
