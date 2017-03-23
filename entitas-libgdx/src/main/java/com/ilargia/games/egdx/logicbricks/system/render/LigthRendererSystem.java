package com.ilargia.games.egdx.logicbricks.system.render;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.SceneManagerGDX;
import com.ilargia.games.egdx.logicbricks.component.game.TextureView;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.game.GameMatcher;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.group.Group;


public class LigthRendererSystem implements IInitializeSystem, IRenderSystem {

    private final EngineGDX engine;
    private Entitas entitas;
    private OrthographicCamera cam;
    private RayHandler rayHandler;

    public LigthRendererSystem(Entitas entitas, EngineGDX engine) {
        this.entitas = entitas;
        this.engine = engine;

    }

    @Override
    public void initialize() {
        this.cam = (OrthographicCamera) entitas.scene.getCamera().camera;
        this.rayHandler =  engine.getManager(SceneManagerGDX.class).rayHandler;

    }

    @Override
    public void render() {
        cam.update();
        rayHandler.setCombinedMatrix(cam);
        rayHandler.updateAndRender();

    }

}
