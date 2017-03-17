package com.ilargia.games.egdx.logicbricks.system.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
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
    private ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera cam;


    public DebugRendererSystem(Entitas entitas, World world) {
        this.physics = world;
        this.entitas = entitas;

        this.shapeRenderer = new ShapeRenderer();
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
       // cam.update();

        Rectangle rect = new Rectangle(cam.position.x, cam.position.y, 3, 10);

        shapeRenderer.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.identity();
        shapeRenderer.translate(20, 12, 2);
        shapeRenderer.rotate(0, 0, 1, 90);
        shapeRenderer.rect(cam.position.x , cam.position.y, 50, 30);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.identity();
        shapeRenderer.translate(20, 12, 2);
        shapeRenderer.rotate(0, 0, 1, 90);
        shapeRenderer.rect(-rect.getX() / 2, -rect.getY() / 2, rect.width, rect.height);
        shapeRenderer.end();
       // debugRenderer.render(physics, cam.combined);
    }


}
