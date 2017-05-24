package ilargia.egdx.impl.managers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import ilargia.egdx.api.managers.RaycasterManager;
import ilargia.egdx.api.managers.data.PointerState;
import ilargia.egdx.impl.EngineGDX;

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
