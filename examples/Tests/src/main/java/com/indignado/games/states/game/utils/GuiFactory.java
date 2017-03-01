package com.indignado.games.states.game.utils;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ilargia.games.egdx.base.managers.BaseAssetsManager;
import com.indignado.games.states.game.component.input.PlayerInputController;
import com.indignado.games.states.game.gen.input.InputEntity;

import static com.indignado.games.manager.SMGUIManager.GUI_ATLAS;
import static com.indignado.games.manager.SMGUIManager.ScaleUtil;

public class GuiFactory {
    private BaseAssetsManager assetsManager;
    private Skin skin;

    public GuiFactory(BaseAssetsManager assetsManager, Skin skin) {
        this.assetsManager = assetsManager;
        this.skin = skin;
    }


    public Touchpad createTouchPad(float width, float height, InputEntity player) {
        Touchpad touchpad = new Touchpad(10 * ScaleUtil.getSizeRatio(), skin);
        touchpad.setPosition(25 * ScaleUtil.getSizeRatio(), 15);
        touchpad.setWidth(width);
        touchpad.setHeight(height);
        PlayerInputController stateController = player.getPlayerInputController();

        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("PercentX " + ((Touchpad) actor).getKnobPercentX() + "PercentY " + ((Touchpad) actor).getKnobPercentY());
                if (((Touchpad) actor).getKnobPercentX() == 0 || ((Touchpad) actor).getKnobPercentX() < 0.5
                        && ((Touchpad) actor).getKnobPercentX() > -0.5) {
//                    controller.rightReleased();
//                    controller.leftReleased();
                    player.replacePlayerInputController(false, false, stateController.jumpPressed);
                }
                if (((Touchpad) actor).getKnobPercentX() > 0.5) {
//                    controller.rightPressed();
//                    controller.leftReleased();
                    player.replacePlayerInputController(false, true, stateController.jumpPressed);
                }
                if (((Touchpad) actor).getKnobPercentX() < -0.5) {
//                    controller.leftPressed();
//                    controller.rightReleased();
                    player.replacePlayerInputController(true, false, stateController.jumpPressed);
                }
                if (((Touchpad) actor).getKnobPercentY() > 0.5) {
//                    controller.jumpPressed();
                    player.replacePlayerInputController(stateController.leftPressed,
                            stateController.rightPressed, true);
                } else {
//                    controller.jumpReleased();
                    player.replacePlayerInputController(stateController.leftPressed,
                            stateController.rightPressed, false);
                }

            }
        });

        return touchpad;
    }

    public Table createPadButtons(float width, float height, InputEntity player) {

        Table tableControlPad = new Table();
        PlayerInputController stateController = player.getPlayerInputController();


        tableControlPad.row().height(height);
        ImageButton btnLeft = new ImageButton(skin, "buttonLeft");
        tableControlPad.add(btnLeft).width(width).expandY().fill();

        btnLeft.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event " + event.getType());
                super.touchDown(event, x, y, pointer, button);
//                controller.leftPressed();
//                controller.rightReleased();
                player.replacePlayerInputController(true, false, stateController.jumpPressed);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event " + event.getType());
                super.touchUp(event, x, y, pointer, button);
//                controller.leftReleased();
                player.replacePlayerInputController(false, stateController.rightPressed, stateController.jumpPressed);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                System.out.println("Event- " + event.getType());
                super.touchDragged(event, x, y, pointer);
                if (isOver(event.getListenerActor(), x, y)) {
//                    controller.rightReleased();
//                    controller.leftPressed();
                    player.replacePlayerInputController(true, false, stateController.jumpPressed);
                } else {
//                    controller.leftReleased();
                    player.replacePlayerInputController(false, stateController.rightPressed, stateController.jumpPressed);
                }
            }
        });


        ImageButton btnRight = new ImageButton(skin, "buttonRight");
        tableControlPad.add(btnRight).width(width).expandY().fill().padRight((width) * 2);
        btnRight.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event " + event.getType());
                super.touchDown(event, x, y, pointer, button);
//                controller.leftReleased();
//                controller.rightPressed();
                player.replacePlayerInputController(false, true, stateController.jumpPressed);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event " + event.getType());
                super.touchUp(event, x, y, pointer, button);
//                controller.rightReleased();
                player.replacePlayerInputController(stateController.leftPressed, false, stateController.jumpPressed);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                System.out.println("Event- " + event.getType());
                super.touchDragged(event, x, y, pointer);
                if (isOver(event.getListenerActor(), x, y)) {
//                    controller.rightPressed();
//                    controller.leftReleased();
                    player.replacePlayerInputController(false, true, stateController.jumpPressed);
                } else {
//                    controller.rightReleased();
                    player.replacePlayerInputController(stateController.leftPressed, false, stateController.jumpPressed);
                }
            }
        });

        ImageButton btnUP = new ImageButton(skin, "buttonUp");
        tableControlPad.add(btnUP).width(width).expandY().fill();
        btnUP.addListener(new ClickListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event " + event.getType());
                super.touchDown(event, x, y, pointer, button);
//                controller.jumpPressed();
                player.replacePlayerInputController(stateController.leftPressed, stateController.rightPressed, true);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Event " + event.getType());
                super.touchUp(event, x, y, pointer, button);
//                controller.jumpReleased();
                player.replacePlayerInputController(stateController.leftPressed, stateController.rightPressed, false);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                System.out.println("Event- " + event.getType());
                super.touchDragged(event, x, y, pointer);
                if (isOver(event.getListenerActor(), x, y)) {
//                    controller.jumpPressed();
                    player.replacePlayerInputController(stateController.leftPressed, stateController.rightPressed, true);

                } else {
//                    controller.jumpReleased();
                    player.replacePlayerInputController(stateController.leftPressed, stateController.rightPressed, false);
                }
            }
        });

        tableControlPad.setBounds(0, 0, Gdx.graphics.getWidth(), height + 10);
        return tableControlPad;
    }

    public Table createScore(float width, float height) {
        Table tableProfile = new Table();
        tableProfile.setBounds(0, 0, width, height);


        Image imageLives = new Image(((TextureAtlas) assetsManager.getTextureAtlas(GUI_ATLAS)).findRegion("vidas"));
        imageLives.setName("IMAGE_LIVES");

        Label lives = new Label("0", skin, "default", Color.ORANGE);
        lives.setName("LIVES");

        Label labelScore = new Label("Tijeras: ", skin, "default", Color.ORANGE);
        labelScore.setName("LABEL_SCORE");

        Label score = new Label("0000", skin, "default", Color.ORANGE);
        score.setName("SCORE");
        tableProfile.defaults().height(height);
        tableProfile.defaults().width(width / 4.5f);


        tableProfile.add(imageLives).left().padRight(15).width(imageLives.getPrefWidth() * ScaleUtil.getSizeRatio());
        tableProfile.add(lives).expandY().fill();
        tableProfile.add();

        tableProfile.add(labelScore).right().expandY().fill();
        tableProfile.add(score).right().expandY().fill();
        tableProfile.debug();
        return tableProfile;

    }
}
