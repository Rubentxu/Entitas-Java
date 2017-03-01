package com.ilargia.games.core.system.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.states.game.gen.Entitas;
import com.ilargia.games.core.gen.scene.SceneContext;
import com.ilargia.games.core.gen.scene.SceneEntity;
import com.ilargia.games.core.gen.scene.SceneMatcher;

import java.util.List;


public class GuiRendererSystem extends ReactiveSystem<SceneEntity> implements IInitializeSystem, IRenderSystem {

    private Stage stage;
    private SceneContext context;
    private OrthographicCamera cam;

    public GuiRendererSystem(Entitas entitas, Stage stage) {
        super(entitas.scene);
        this.context = entitas.scene;
        this.stage = stage;

    }

    @Override
    public void initialize() {
        this.cam = context.getCamera().camera;


    }

    @Override
    protected Collector<SceneEntity> getTrigger(IContext<SceneEntity> context) {
        return context.createCollector(SceneMatcher.Tiled());
    }

    @Override
    protected boolean filter(SceneEntity entity) {
        return entity.hasTiled();
    }

    @Override
    protected void execute(List<SceneEntity> gameEntities) {

    }

    @Override
    public void render() {


    }


}
