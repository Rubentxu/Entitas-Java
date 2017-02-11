package com.ilargia.games.egdx.api.managers;

import com.ilargia.games.egdx.api.EntityFactory;

public interface SceneManager extends Manager {

    public void addEntityFactory(String name, EntityFactory factory);

    public <Camera> Camera createEntity(String name, float posX, float posY);


}
