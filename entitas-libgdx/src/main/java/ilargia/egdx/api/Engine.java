package ilargia.egdx.api;

import ilargia.egdx.api.managers.Manager;

public interface Engine {

    void initialize();

    void update(float deltaTime);

    void dispose();

    <M extends Manager> Engine addManager(M manager);

    <M extends Manager> M getManager(Class<M> clazz);

}
