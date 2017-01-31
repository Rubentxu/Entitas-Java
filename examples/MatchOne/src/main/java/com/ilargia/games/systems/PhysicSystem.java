package com.ilargia.games.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.entitas.api.system.ICleanupSystem;


public class PhysicSystem implements ICleanupSystem {

    private final int BOX2D_VELOCITY_ITERATIONS = 6;
    private final int BOX2D_POSITION_ITERATIONS = 10;
    private World physics;


    public PhysicSystem(World world) {
        this.physics = world;

    }

    @Override
    public void cleanup() {
        physics.step(Gdx.graphics.getDeltaTime(), BOX2D_VELOCITY_ITERATIONS, BOX2D_POSITION_ITERATIONS);
    }
}
