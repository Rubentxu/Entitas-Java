package com.ilargia.games.egdx.logicbricks.system.render;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ilargia.games.egdx.impl.managers.AssetsManagerGDX;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.collector.Collector;
import com.ilargia.games.entitas.systems.ReactiveSystem;
import com.ilargia.games.egdx.logicbricks.component.scene.Tiled;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneContext;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneEntity;
import com.ilargia.games.egdx.logicbricks.gen.scene.SceneMatcher;

import java.util.List;


public class TiledRendererSystem extends ReactiveSystem<SceneEntity> implements IInitializeSystem, IRenderSystem {

    private AssetsManagerGDX assetsManager;
    private SceneContext context;
    private OrthogonalTiledMapRenderer tiledRenderer;
    private Camera cam;

    public TiledRendererSystem(Entitas entitas, AssetsManagerGDX assetsManager) {
        super(entitas.scene);
        this.context = entitas.scene;
        this.assetsManager = assetsManager;

    }

    @Override
    public void initialize() {
        this.cam = context.getCamera().camera;
        Tiled tiled = context.getTiled();
        TiledMap tiledMap = assetsManager.getMap(tiled.tileMapName);
        this.tiledRenderer = new OrthogonalTiledMapRenderer(tiledMap, tiled.unitScale);

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
        Tiled tiled = context.getTiled();
        TiledMap tiledMap = assetsManager.getMap(tiled.tileMapName);
        tiledRenderer.setMap(tiledMap);
        tiledRenderer.setView((OrthographicCamera) cam);
    }

    @Override
    public void render() {
        tiledRenderer.render();

    }


}
