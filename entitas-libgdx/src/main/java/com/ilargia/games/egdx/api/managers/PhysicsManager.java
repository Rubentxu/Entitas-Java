package com.ilargia.games.egdx.api.managers;


public interface PhysicsManager<P> extends Manager {

    P getPhysics();

    <B> B getBodyBuilder();
}
