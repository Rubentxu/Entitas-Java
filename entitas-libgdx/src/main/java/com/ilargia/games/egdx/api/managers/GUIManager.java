package com.ilargia.games.egdx.api.managers;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.factories.GUIFactory;
import com.ilargia.games.entitas.api.IEntity;

public interface GUIManager<E,S,A> extends Manager {

    S createSkin(String skin, A atlas);

    S getSkin();

    void addGUIFactory(String name, GUIFactory factory);

    E createGUIElement(String name);

}
