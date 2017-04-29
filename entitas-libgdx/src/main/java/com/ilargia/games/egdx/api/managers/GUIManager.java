package com.ilargia.games.egdx.api.managers;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.factories.GUIFactory;
import com.ilargia.games.entitas.api.IEntity;

public interface GUIManager<A,T> extends Manager {

    void addGUIFactory(String name, GUIFactory factory);

    T createGUIElement(String name);

}
