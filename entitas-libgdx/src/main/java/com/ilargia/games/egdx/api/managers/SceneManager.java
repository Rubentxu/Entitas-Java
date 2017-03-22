package com.ilargia.games.egdx.api.managers;

import com.ilargia.games.egdx.api.factories.EntityFactory;
import com.ilargia.games.egdx.api.factories.LightFactory;
import com.ilargia.games.egdx.api.factories.SceneFactory;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.api.IComponent;

public interface SceneManager extends Manager {

    void addEntityFactory(String name, EntityFactory factory);

    void addSceneFactory(String name, SceneFactory factory);

    <L> void  addLightFactory(Class<L> type, LightFactory factory);

    <TEntity extends Entity> TEntity createEntity(String name);

    void createScene(String scene);

    <C extends IComponent, L> L createLight(Class<L> type, C lightComponent);

    <C> C createCamera(String type);

    <C> C getDefaultCamera();

    <B> B getBatch();


}
