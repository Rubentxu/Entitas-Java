package games.states.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.egdx.base.BaseGameState;
import com.ilargia.games.entitas.egdx.base.managers.BaseAssetsManager;
import ilargia.egdx.util.BodyBuilder;
import ilargia.egdx.util.FixtureDefBuilder;
import com.ilargia.games.entitas.factories.Collections;
import com.indignado.games.SMEngine;
import games.manager.SMGUIManager;
import com.indignado.games.states.game.data.Bounds;
import com.indignado.games.states.game.gen.Entitas;
import com.indignado.games.states.game.system.AnimationSystem;
import com.indignado.games.states.game.system.InputsControllerSystem;
import com.indignado.games.states.game.system.render.TextureRendererSystem;
import games.states.game.utils.GuiFactory;

import java.util.Map;

public class GameSceneState extends BaseGameState {
    public static final String SPRITE_ATLAS = "imagenes/animaciones/sprites.pack";
    public static final float RUNNING_FRAME_DURATION = 0.02f;
    private final Skin skin;
    private final Entitas entitas;
    private SMGUIManager skinManager;
    private Stage stage;
    private SMEngine engine;
    private BaseAssetsManager assetsManager;
    private TextureAtlas atlas;
    private GuiFactory guiFactory;
    private BodyBuilder bodyBuilder;

    public GameSceneState(SMEngine engine) {
        this.engine = engine;
        this.skin = skinManager.skin;
        entitas = new Entitas();

    }

    @Override
    public void loadResources() {
        this.skinManager = engine.getManager(SMGUIManager.class);
        this.assetsManager = engine.getManager(BaseAssetsManager.class);
        guiFactory = new GuiFactory(assetsManager, skin);
        bodyBuilder =  new BodyBuilder();
        this.stage = new Stage();
        stage.clear();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(stage);
        Gdx.app.log("Menu", "LoadResources");

        assetsManager.loadTextureAtlas(SPRITE_ATLAS);
        assetsManager.finishLoading();

    }


    @Override
    public void initialize() {
        atlas = assetsManager.getTextureAtlas(SPRITE_ATLAS);
        Array<TextureAtlas.AtlasRegion> heroWalking = atlas.findRegions("Andando");
        Array<TextureAtlas.AtlasRegion> heroJump = atlas.findRegions("Saltando");
        Array<TextureAtlas.AtlasRegion> heroFall = atlas.findRegions("Cayendo");
        Array<TextureAtlas.AtlasRegion> heroIdle = atlas.findRegions("Parado");


        Animation walking = new Animation(RUNNING_FRAME_DURATION, heroWalking, Animation.PlayMode.LOOP);
        Animation jump = new Animation(RUNNING_FRAME_DURATION * 7, heroJump, Animation.PlayMode.NORMAL);
        Animation fall = new Animation(RUNNING_FRAME_DURATION * 5, heroFall, Animation.PlayMode.NORMAL);
        Animation idle = new Animation(RUNNING_FRAME_DURATION * 4, heroIdle, Animation.PlayMode.LOOP);


        Map animationHero = Collections.createMap(String.class, Animation.class);
        animationHero.put("WALKING", walking);
        animationHero.put("JUMPING", jump);
        animationHero.put("FALL", fall);
        animationHero.put("HURT", fall);
        animationHero.put("IDLE", idle);
        animationHero.put("HIT", fall);


        Body body = bodyBuilder.fixture(new FixtureDefBuilder()
                .boxShape(0.5f, 0.5f))
                .type(BodyDef.BodyType.KinematicBody)
                .build();


        systems.add(new InputsControllerSystem(entitas.input, guiFactory))
                .add(new AnimationSystem(entitas))
                .add(new TextureRendererSystem(entitas, engine.batch));


        entitas.game.getPlayerEntity()
                .addTextureView(null,new Bounds(0.8f,1.2f),false,false,1,1, Color.WHITE)
                .addAnimations(animationHero, walking, 0.02f)
                .addRigidBody(body);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);

    }

    @Override
    public void render() {
        stage.draw();
        super.render();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void dispose() {
        stage = null;
        super.dispose();
    }

    @Override
    public void unloadResources() {
        stage.clear();
        assetsManager.unloadTextureAtlas(SPRITE_ATLAS);
        entitas.game.destroyAllEntities();
        entitas.scene.destroyAllEntities();
        entitas.actuator.destroyAllEntities();
        entitas.gui.destroyAllEntities();
        systems.clearSystems();
    }
}
