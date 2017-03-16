package com.ilargia.games.egdx.logicbricks.system.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ilargia.games.egdx.logicbricks.gen.Entitas;
import com.ilargia.games.entitas.api.system.IInitializeSystem;
import com.ilargia.games.entitas.api.system.IRenderSystem;


public class DebugRendererSystem implements IInitializeSystem, IRenderSystem {

    // Debug
    public static boolean TESTING = true;
    public static String DEBUG_ENTITY;
    public static boolean DRAW_BOX2D_BODIES = true;
    public static boolean DRAW_BOX2D_JOINTS = true;
    public static boolean DRAW_BOX2D_ABBs = true;
    public static boolean DRAW_BOX2D_INACTIVE_BODIES = true;
    public static boolean DRAW_BOX2D_VELOCITIES = true;
    public static boolean DRAW_BOX2D_CONTACTS = true;

    private World physics;
    private Entitas entitas;
    private ShapeRenderer debugShapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera cam;


    public DebugRendererSystem(Entitas entitas, World world) {
        this.physics = world;
        this.entitas = entitas;

        this.debugShapeRenderer = new ShapeRenderer();
        this.debugRenderer = new Box2DDebugRenderer(DRAW_BOX2D_BODIES, DRAW_BOX2D_JOINTS, DRAW_BOX2D_ABBs,
                DRAW_BOX2D_INACTIVE_BODIES, DRAW_BOX2D_VELOCITIES, DRAW_BOX2D_CONTACTS);
        debugRenderer.setDrawAABBs(DRAW_BOX2D_ABBs);
        debugRenderer.setDrawBodies(DRAW_BOX2D_BODIES);
        debugRenderer.setDrawContacts(DRAW_BOX2D_CONTACTS);
        debugRenderer.setDrawInactiveBodies(DRAW_BOX2D_INACTIVE_BODIES);
        debugRenderer.setDrawJoints(DRAW_BOX2D_JOINTS);
        debugRenderer.setDrawVelocities(DRAW_BOX2D_VELOCITIES);

    }


    @Override
    public void initialize() {
        this.cam = entitas.scene.getCamera().camera;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cam.update();


//        debugShapeRenderer.setProjectionMatrix(cam.combined);
//        debugShapeRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
//
//        debugShapeRenderer.end();

        debugRenderer.render(physics, cam.combined);
    }


}
