package games.states.menu;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.ilargia.games.entitas.egdx.api.ChangeStateCommand;
import com.ilargia.games.entitas.egdx.base.managers.BaseAssetsManager;
import com.ilargia.games.entitas.egdx.api.GameState;

import com.indignado.games.SMEngine;
import com.indignado.games.SMGame;
import games.manager.SMGUIManager;

public class MenuState implements GameState {
    private final Skin skin;
    private SMGUIManager skinManager;
    private Stage stage;
    private Table mainTable;
    private SMEngine engine;
    private BaseAssetsManager assetsManager;

    public MenuState(SMEngine engine) {
        this.engine = engine;
        this.skinManager = engine.getManager(SMGUIManager.class);
        this.skin = skinManager.skin;


    }

    @Override
    public void loadResources() {
        assetsManager = engine.getManager(BaseAssetsManager.class);
        this.stage = new Stage();
        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.clear();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
        Gdx.app.log("Menu", "LoadResources");
    }

    @Override
    public void init() {
        Gdx.app.log("Menu", "Init");
        int pad = (int) (20 * SMGUIManager.ScaleUtil.getSizeRatio());
        int pad2 = (int) (60 * SMGUIManager.ScaleUtil.getSizeRatio());
        final TextButton btnStart = new TextButton("Comenzar", skin);
        btnStart.pad(pad, pad2, pad, pad2);
        final TextButton btnOptions = new TextButton("Opciones", skin);
        btnOptions.pad(pad, pad2, pad, pad2);
        final TextButton btnScores = new TextButton("Puntuaciones", skin);
        btnScores.pad(pad, pad2, pad, pad2);
        final TextButton button3 = new TextButton("Creditos", skin);
        button3.pad(pad, pad2, pad, pad2);
        button3.setChecked(false);

        btnStart.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click Comenzar...");

            }
        });

        btnOptions.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click optionScreen...");
                SMGame.ebus.post((ChangeStateCommand<SMGame>) (nameState, game) ->
                        game.changeState(game.getOptionState(), game.getFadeTransition())
                );
            }
        });

        btnScores.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Click highScoreScreen...");
                SMGame.ebus.post((ChangeStateCommand<SMGame>) (nameState, game) ->
                        game.changeState(game.getScoresState(), game.getSlideTransition())
                );
            }
        });

        Label label = new Label("SUPER MARIANO", skin, "header", Color.CYAN);
        label.setAlignment(Align.center, Align.center);
        mainTable.defaults().padBottom(pad);
        if (Gdx.graphics.getHeight() < 480) mainTable.defaults().height(Gdx.graphics.getHeight() / 5f - pad);
        mainTable.add(label);
        mainTable.row();
        mainTable.add(btnStart);
        mainTable.row();
        mainTable.add(btnOptions);
        mainTable.row();
        mainTable.add(btnScores);
        mainTable.row();
        mainTable.add(button3);
        mainTable.row();
        mainTable.setBackground(new SpriteDrawable(new Sprite((Texture) assetsManager.getTexture(SMGUIManager.MENU_BACKGROUND))));
        mainTable.row();

    }

    @Override
    public void update(float deltaTime) {
        Gdx.gl.glClearColor(0.1f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(deltaTime);

    }

    @Override
    public void render() {
        stage.draw();
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
    }

    @Override
    public void unloadResources() {
        stage.clear();
    }
}
