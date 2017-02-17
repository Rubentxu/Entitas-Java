package com.ilargia.games.egdx.api.managers;

import com.ilargia.games.egdx.api.EntityFactory;
import com.ilargia.games.entitas.api.IEntity;

public interface SceneManager<M> extends Manager {

    void initialize();

    void addEntityFactory(String name, EntityFactory factory);

    <TEntity extends IEntity> TEntity createEntity(String name, float posX, float posY);

    void createScene(M map);

    <L> L createLight();

    <C> C createCamera();


}
