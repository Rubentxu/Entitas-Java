package ilargia.egdx.logicbricks.system.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.impl.managers.GUIManagerGDX;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.api.system.IRenderSystem;


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
