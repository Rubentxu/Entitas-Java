package ilargia.egdx.logicbricks.system.render;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import ilargia.egdx.impl.EngineGDX;
import ilargia.egdx.logicbricks.component.actuator.ParticleEffectActuator;
import ilargia.egdx.logicbricks.component.game.TextureView;
import ilargia.egdx.logicbricks.gen.Entitas;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorEntity;
import ilargia.egdx.logicbricks.gen.actuator.ActuatorMatcher;
import ilargia.egdx.logicbricks.gen.game.GameEntity;
import ilargia.egdx.logicbricks.gen.game.GameMatcher;
import ilargia.entitas.api.system.IInitializeSystem;
import ilargia.entitas.api.system.IRenderSystem;
import ilargia.entitas.group.Group;


public class TextureRendererSystem implements IInitializeSystem, IRenderSystem {

    private final EngineGDX engine;
    private Entitas entitas;
    private OrthographicCamera cam;
    private Batch batch;
    private Group<GameEntity> groupTextureView;
    private Group<ActuatorEntity> groupEffect;

    public TextureRendererSystem(Entitas entitas, EngineGDX engine) {
        this.batch = engine.getBatch();
        this.entitas = entitas;
        this.engine = engine;

    }

    @Override
    public void initialize() {
        this.cam = (OrthographicCamera) entitas.scene.getCamera().camera;
        this.groupTextureView = entitas.game.getGroup(GameMatcher.TextureView());
        this.groupEffect = entitas.actuator.getGroup(ActuatorMatcher.ParticleEffectActuator());
    }

    @Override
    public void render() {

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        for (GameEntity e : groupTextureView.getEntities()) {
            TextureView view = e.getTextureView();
            Body body = e.getRigidBody().body;
            processTextureFlip(view);

            batch.setColor(1f,1f,1f,1f);
            if(view.opacity< 1 ) batch.setColor(1f, 1f, 1f, view.opacity);
            batch.draw(view.texture, body.getPosition().x - view.bounds.extentsX, body.getPosition().y - view.bounds.extentsY,
                    view.bounds.extentsX, view.bounds.extentsY, view.bounds.extentsX * 2, view.bounds.extentsY * 2, 1, 1,
                    MathUtils.radiansToDegrees * body.getTransform().getRotation());


        }
        for (ActuatorEntity e : groupEffect.getEntities()) {
            if(e.getLink().isOpen) {
                ParticleEffectActuator effectActuator = e.getParticleEffectActuator();
                effectActuator.particleEffect.draw(batch);
            }
        }
        batch.end();

    }


    private void processTextureFlip(TextureView view) {
        if ((view.flipX && !view.texture.isFlipX()) || (!view.flipX && view.texture.isFlipX())) {
            float temp = view.texture.getU();
            view.texture.setU(view.texture.getU2());
            view.texture.setU2(temp);
        }


        if ((view.flipY && !view.texture.isFlipY()) || (!view.flipY && view.texture.isFlipY())) {
            float temp = view.texture.getV();
            view.texture.setV(view.texture.getV2());
            view.texture.setV2(temp);

        }
    }

}
