package com.examples.games.entities;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.ilargia.games.egdx.api.Engine;
import com.ilargia.games.egdx.api.factories.EntityFactory;
import com.ilargia.games.egdx.impl.managers.AssetsManagerGDX;
import com.ilargia.games.egdx.impl.managers.PhysicsManagerGDX;
import com.ilargia.games.egdx.logicbricks.data.Bounds;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.egdx.logicbricks.gen.game.GameEntity;
import com.ilargia.games.egdx.logicbricks.gen.sensor.SensorEntity;
import com.ilargia.games.egdx.logicbricks.index.Indexed;
import com.ilargia.games.egdx.util.BodyBuilder;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;
import java.util.Set;


public class Mariano implements EntityFactory<Entitas, GameEntity> {
    private String effect = "assets/particles/dust.pfx";
    private String atlas = "assets/animations/sprites.pack";
    private AssetsManagerGDX assetsManager;

    @Override
    public void loadAssets(Engine engine) {
        assetsManager = engine.getManager(AssetsManagerGDX.class);
        assetsManager.loadAsset(effect, ParticleEffect.class);
        assetsManager.loadTextureAtlas(atlas);

    }

    @Override
    public GameEntity create(Engine engine, Entitas entitas) {
        PhysicsManagerGDX physics = engine.getManager(PhysicsManagerGDX.class);
        BodyBuilder bodyBuilder = physics.getBodyBuilder();

        TextureAtlas textureAtlas = assetsManager.getTextureAtlas(atlas);

        Array<TextureAtlas.AtlasRegion> heroWalking = textureAtlas.findRegions("Andando");
        Array<TextureAtlas.AtlasRegion> heroJump = textureAtlas.findRegions("Saltando");
        Array<TextureAtlas.AtlasRegion> heroFall = textureAtlas.findRegions("Cayendo");
        Array<TextureAtlas.AtlasRegion> heroIdle = textureAtlas.findRegions("Parado");

        Map<String, Animation<TextureRegion>> animationStates = EntitasCollections.createMap(String.class, Animation.class);
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

        GameEntity entity = entitas.game.createEntity();
        entity.addRigidBody(bodyPlayer)
                .addAnimations(animationStates, animationStates.get("idle"), 0)
                .addTags("Mariano")
                .setInteractive(true)
                .addTextureView(null, new Bounds(0.9f, 1.15f), false, false, 1, 1, Color.WHITE)
                .addInputController((inputManager, context) -> {
                    boolean isGround = false;
                    Set<SensorEntity> sensors = Indexed.getSensorsEntities(entity);
                    for (SensorEntity sensor : sensors) {
                        if (sensor.hasCollisionSensor() && sensor.getCollisionSensor().collisionSignal)
                            isGround = true;

                    }

                    Vector2 impulse = new Vector2();
                    if (inputManager.isKeyDown(Input.Keys.D)) {
                        impulse.x = 2;
                        if (isGround)
                            entity.getAnimations().currentAnimation = entity.getAnimations().animationStates.get("walking");
                        entity.getTextureView().flipX = false;
                    } else if (inputManager.isKeyDown(Input.Keys.A)) {
                        impulse.x = -2;
                        if (isGround)
                            entity.getAnimations().currentAnimation = entity.getAnimations().animationStates.get("walking");
                        entity.getTextureView().flipX = true;
                    }

                    if (inputManager.isKeyDown(Input.Keys.W)) {
                        if (isGround) impulse.y = 4;
                        entity.getAnimations().currentAnimation = entity.getAnimations().animationStates.get("jump");
                    }

                    Vector2 vel = bodyPlayer.getLinearVelocity();
                    if (!inputManager.isKeyDown(Input.Keys.A) && !inputManager.isKeyDown(Input.Keys.D) && isGround) {
                        bodyPlayer.setLinearVelocity(new Vector2(0, vel.y));
                        entity.getAnimations().currentAnimation = entity.getAnimations().animationStates.get("idle");
                    }

                    if (Math.abs(vel.x) > 7) {
                        vel.x = Math.signum(vel.x) * 7;
                        bodyPlayer.setLinearVelocity(new Vector2(vel.x, vel.y));
                    }
                    if (!isGround && vel.y < 0) {
                        entity.getAnimations().currentAnimation = entity.getAnimations().animationStates.get("fall");
                    }
                    bodyPlayer.applyLinearImpulse(impulse, bodyPlayer.getWorldCenter(), false);

                });

        SensorEntity collisionGroundSensor = entitas.sensor.createEntity()
                .addCollisionSensor("Ground")
                .addLink(entity.getCreationIndex());

//        entitas.actuator.createEntity()
//                .addCameraActuator((short) 0.3f, 0.08f, new Vector2(6, 1), "Mariano")
//                .addLink(entity.getCreationIndex());

        return entity;

    }
}
