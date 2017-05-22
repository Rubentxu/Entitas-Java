package ilargia.egdx.api.managers;

import ilargia.egdx.api.factories.EntityFactory;
import ilargia.egdx.api.factories.LightFactory;
import ilargia.egdx.api.factories.SceneFactory;
import ilargia.entitas.Entity;
import ilargia.entitas.api.IComponent;

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
