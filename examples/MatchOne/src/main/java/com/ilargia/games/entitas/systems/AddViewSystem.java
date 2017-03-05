package com.ilargia.games.entitas.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.ilargia.games.logicbrick.gen.game.GameContext;
import com.ilargia.games.logicbrick.gen.game.GameMatcher;
import com.ilargia.games.logicbrick.component.game.Position;
import com.ilargia.games.logicbrick.gen.game.GameEntity;
import com.ilargia.games.entitas.egdx.base.managers.BaseAssetsManager;
import com.ilargia.games.entitas.egdx.base.util.BodyBuilder;
import com.ilargia.games.entitas.egdx.base.util.FixtureDefBuilder;
import com.ilargia.games.entitas.api.IContext;
import com.ilargia.games.entitas.collector.Collector;

import java.util.List;


public class AddViewSystem extends ReactiveSystem<GameEntity> {

    private final BaseAssetsManager assetsManager;
    private final BodyBuilder bodyBuilder;
    private GameContext context;

    public AddViewSystem(GameContext context, BaseAssetsManager assetsManager, BodyBuilder bodyBuilder) {
        super(context);
        this.assetsManager = assetsManager;
        this.bodyBuilder = bodyBuilder;

    }

    @Override
    protected Collector<GameEntity> getTrigger(IContext<GameEntity> context) {
        return context.createCollector(GameMatcher.Asset());
    }

    @Override
    protected boolean filter(GameEntity entity) {
        return entity.hasAsset() && !entity.hasTextureView();
    }


    @Override
    public void execute(List<GameEntity> entities) {
        for (GameEntity e : entities) {
            Texture texture = assetsManager.getTexture(String.format("assets/textures/%1$s.png", e.getAsset().name));
            Body body = bodyBuilder.fixture(new FixtureDefBuilder()
                    .boxShape(0.5f, 0.5f))
                    .type(BodyDef.BodyType.KinematicBody)
                    .build();
            TextureRegion textureRegion = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
            e.addTextureView(e.getAsset().name, textureRegion, body);


//                gameObject.Link(e, _pool);
//
            if (e.hasPosition()) {
                Position pos = e.getPosition();
                body.setTransform(new Vector2(pos.x, pos.y + 1), 0);
            }
        }
    }
}



