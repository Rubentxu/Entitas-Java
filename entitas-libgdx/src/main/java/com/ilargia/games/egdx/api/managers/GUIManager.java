package com.ilargia.games.egdx.api.managers;


import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ilargia.games.egdx.api.factories.GUIFactory;
import com.ilargia.games.entitas.api.IEntity;

public interface GUIManager<A> extends Manager {

    Skin getSkin();

    void addGUIFactory(String name, GUIFactory factory);

    <TEntity extends IEntity> TEntity createGUIElement(String name, float posX, float posY);

}
