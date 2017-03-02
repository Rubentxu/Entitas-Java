package com.ilargia.games.core.component.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.indignado.games.states.game.component.gui.Label;


@Component(pools = {"Gui"})
public class ImageWidget implements IComponent {
    public Image image;


    public ImageWidget() {
    }

}
