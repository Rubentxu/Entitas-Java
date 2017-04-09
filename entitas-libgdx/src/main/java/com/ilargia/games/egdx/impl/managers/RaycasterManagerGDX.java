package com.ilargia.games.egdx.impl.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.ilargia.games.egdx.api.managers.RaycasterManager;
import com.ilargia.games.egdx.api.managers.data.PointerState;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.List;
import java.util.Map;

public class RaycasterManagerGDX implements RaycasterManager<Vector2, Vector3> {

    private final EngineGDX engine;
    private World physics;

    public RaycasterManagerGDX(EngineGDX engine) {
        this.engine = engine;

    }

    @Override
    public void initialize() {
        this.physics = engine.getPhysics();

    }

    @Override
    public Integer raycast(String raycaster, PointerState<Vector2, Vector3> state) {
        final Integer[] index = new Integer[1];
        physics.QueryAABB(fixture -> {
                index[0] = (Integer) fixture.getBody().getUserData();
                return false;
            }, state.coordinates.x, state.coordinates.y, state.coordinates.x, state.coordinates.y);

        return index[0];
    }

    @Override
    public Integer raycast(String raycaster, PointerState<Vector2, Vector3> state, PointerState<Vector2, Vector3> state2) {
        final Integer[] index = new Integer[1];
        physics.QueryAABB(fixture -> {
            index[0] = (Integer) fixture.getBody().getUserData();
            return false;
        }, state.coordinates.x, state.coordinates.y, state2.coordinates.x, state2.coordinates.y);

        return index[0];
    }

    @Override
    public void dispose() {

    }

}
