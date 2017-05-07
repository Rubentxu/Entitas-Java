package com.ilargia.games.egdx.api.managers;


import com.ilargia.games.egdx.api.factories.GUIFactory;

public interface GUIManager<E,S,A> extends Manager {

    S createSkin(String skin, A atlas);

    S getSkin();

    void addGUIFactory(String name, GUIFactory factory);

    E createGUIElement(String name);

}
