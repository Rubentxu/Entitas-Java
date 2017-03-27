package com.ilargia.games.egdx.logicbricks.data;


import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Map;

public class TextureViews {
    public Map<String, TextureView> views;

    public TextureViews() {
        this.views = EntitasCollections.createMap(String.class, TextureView.class);

    }

}
