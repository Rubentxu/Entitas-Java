package games.game.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.ilargia.games.logicbrick.data.Bounds;
import com.ilargia.games.logicbrick.data.StateCharacter;
import com.ilargia.games.entitas.egdx.api.Engine;
import com.ilargia.games.entitas.egdx.api.EntityFactory;
import com.ilargia.games.entitas.egdx.base.managers.BaseAssetsManager;
import com.ilargia.games.entitas.egdx.base.managers.BasePhysicsManager;
import com.ilargia.games.egdx.util.BodyBuilder;
import com.ilargia.games.entitas.Context;
import com.ilargia.games.entitas.factories.Collections;
import com.indignado.games.states.game.gen.GameContext;
import com.indignado.games.states.game.gen.GameEntity;

import java.util.Map;

public class Mariano implements EntityFactory<GameEntity> {
    private String effect = "assets/particles/dust.pfx";
    private String atlas = "assets/animations/sprites.pack";

    private BaseAssetsManager assetsManager;

    @Override
    public void loadAssets(Engine engine) {
        assetsManager = engine.getManager(BaseAssetsManager.class);
        assetsManager.loadAsset(effect, ParticleEffect.class);
        assetsManager.loadTextureAtlas(atlas);
    }

    @Override
    public GameEntity create(Engine engine, Context<GameEntity> context) {

        BasePhysicsManager physics = engine.getManager(BasePhysicsManager.class);
        BodyBuilder bodyBuilder =  physics.getBodyBuilder();

        TextureAtlas textureAtlas = assetsManager.getTextureAtlas(atlas);

        Array<TextureAtlas.AtlasRegion> heroWalking = textureAtlas.findRegions("Andando");
        Array<TextureAtlas.AtlasRegion> heroJump = textureAtlas.findRegions("Saltando");
        Array<TextureAtlas.AtlasRegion> heroFall = textureAtlas.findRegions("Cayendo");
        Array<TextureAtlas.AtlasRegion> heroIdle = textureAtlas.findRegions("Parado");

        Map<String, Animation<TextureRegion>> animationStates = Collections.createMap(String.class, Animation.class);
        animationStates.put("walking", new Animation(0.02f, heroWalking, Animation.PlayMode.LOOP));
        animationStates.put("jump", new Animation(0.02f * 7, heroJump, Animation.PlayMode.NORMAL));
        animationStates.put("fall", new Animation(0.02f * 5, heroFall, Animation.PlayMode.NORMAL));
        animationStates.put("idle", new Animation(0.02f * 4, heroIdle, Animation.PlayMode.LOOP));

        Body bodyPlayer = bodyBuilder.fixture(bodyBuilder.fixtureDefBuilder()
                .boxShape(0.35f, 1)
                .density(1))
                .type(BodyDef.BodyType.DynamicBody)
                .fixedRotation()
                .position(0, 5)
                .mass(1)
                .build();



        GameEntity entity = ((GameContext) context).getPlayerEntity()
                .addRigidBody(bodyPlayer)
                .addAnimations(animationStates, animationStates.get("idle"), 0)
                .addCharacter("Mariano", StateCharacter.IDLE, false)
                .addMovable(7,8)
                .addTextureView(null, new Bounds(0.9f,1.15f),false, false,1,1, Color.WHITE);


        return entity;
    }
}
