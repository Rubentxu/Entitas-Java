package com.ilargia.games.core.component.scene;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class Light implements IComponent {
    public box2dLight.Light light;

}