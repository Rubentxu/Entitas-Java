package com.ilargia.games.egdx.logicbricks.system.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ilargia.games.egdx.impl.EngineGDX;
import com.ilargia.games.egdx.impl.managers.GUIManagerGDX;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneMatcher;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneContext;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneEntity;

import java.util.List;


public class GuiRendererSystem implements IInitializeSystem, IRenderSystem {

    private Stage stage;
    private EngineGDX engine;

    public GuiRendererSystem(EngineGDX engine) {
        this.engine = engine;

    }

    @Override
    public void initialize() {
         this.stage = engine.getManager(GUIManagerGDX.class).getStage();

    }

    @Override
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

}
