package com.ilargia.games.entitas.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.logicbrick.component.input.Input;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.logicbrick.gen.game.GameMatcher;
import com.ilargia.games.logicbrick.gen.input.InputEntity;
import com.ilargia.games.logicbrick.gen.input.InputMatcher;
import com.ilargia.games.entitas.core.Entitas;
import com.ilargia.games.logicbrick.component.game.TextureView;
import com.ilargia.games.entitas.api.system.ICleanupSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;
import com.ilargia.games.entitas.group.Group;


public class RendererSystem implements IRenderSystem, ICleanupSystem {

    // Debug   
    public static boolean TESTING = true;
    public static String DEBUG_ENTITY;
    public static boolean DRAW_BOX2D_BODIES = true;
    public static boolean DRAW_BOX2D_JOINTS = true;
    public static boolean DRAW_BOX2D_ABBs = true;
    public static boolean DRAW_BOX2D_INACTIVE_BODIES = true;
    public static boolean DRAW_BOX2D_VELOCITIES = true;
    public static boolean DRAW_BOX2D_CONTACTS = true;
    public static boolean DRAW_STAGE = false;
    public static boolean DRAW_GRID = false;
    public static boolean DRAW_FPS = true;
    public static float drawFPSPosX = 0;
    public static float drawFPSPosY = 0;
    private final World physics;
    private final Entitas entitas;
    private Group<InputEntity> inputs;

    private ShapeRenderer debugShapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private Camera cam;
    private Batch batch;
    private Group<GameEntity> _groupTextureView;

    public RendererSystem(Entitas entitas, Camera cam, Batch batch, World world) {
        this.physics = world;
        this.cam = cam;
        this.batch = batch;
        this.entitas = entitas;
        _groupTextureView = entitas.game.getGroup(GameMatcher.TextureView());
        this.debugShapeRenderer = new ShapeRenderer();
        this.debugRenderer = new Box2DDebugRenderer(DRAW_BOX2D_BODIES, DRAW_BOX2D_JOINTS, DRAW_BOX2D_ABBs,
                DRAW_BOX2D_INACTIVE_BODIES, DRAW_BOX2D_VELOCITIES, DRAW_BOX2D_CONTACTS);
        debugRenderer.setDrawAABBs(DRAW_BOX2D_ABBs);
        debugRenderer.setDrawBodies(DRAW_BOX2D_BODIES);
        debugRenderer.setDrawContacts(DRAW_BOX2D_CONTACTS);
        debugRenderer.setDrawInactiveBodies(DRAW_BOX2D_INACTIVE_BODIES);
        debugRenderer.setDrawJoints(DRAW_BOX2D_JOINTS);
        debugRenderer.setDrawVelocities(DRAW_BOX2D_VELOCITIES);
        this.inputs = entitas.input.getGroup(InputMatcher.Input());
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.update();
        batch.setProjectionMatrix(cam.combined);


        batch.begin();
        for (GameEntity e : _groupTextureView.getEntities()) {
            TextureView view = e.getTextureView();

            float originX = 1 * 0.5f;
            float originY = 1 * 0.5f;

            batch.draw(view.texture, view.body.getPosition().x - originX, view.body.getPosition().y - originY,
                    originX, originY, 1f, 1f, 1, 1, 0);

        }

        batch.end();

        debugShapeRenderer.setProjectionMatrix(cam.combined);
        debugShapeRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        for (InputEntity e : inputs.getEntities()) {
            Input input = e.getInput();

            debugShapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            debugShapeRenderer.circle(input.x, input.y, 0.3f);
        }
        debugShapeRenderer.end();

        debugRenderer.render(physics, cam.combined);
    }

    @Override
    public void cleanup() {
        for (InputEntity e : inputs.getEntities()) {
            entitas.input.destroyEntity(e);
        }
    }

}
