package com.ilargia.games.egdx.api.managers.listener;

@FunctionalInterface
public interface Collision {
    default void processCollision(Integer indexEntityA, Integer indexEntityB, boolean collisionSignal){ }

    void processSensorCollision(Integer indexEntityA, Integer indexEntityB, String tagSensorA, boolean collisionSignal);

}
