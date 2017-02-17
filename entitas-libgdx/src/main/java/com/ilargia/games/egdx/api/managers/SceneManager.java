package com.ilargia.games.egdx.api.managers;

import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.entitas.api.IEntity;

public interface SceneManager<M> extends Manager {

    public void addEntityFactory(String name, EntityFactory factory);

    public <TEntity extends IEntity> TEntity createEntity(String name, float posX, float posY);

    public  void generateScene(M map);

    public <L> L createLight();

    public <C> C createCamera();


}
