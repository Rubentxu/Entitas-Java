package ilargia.egdx.logicbricks.system.render;

import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.OrthographicCamera;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.impl.managers.SceneManagerGDX;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.api.system.IRenderSystem;


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
