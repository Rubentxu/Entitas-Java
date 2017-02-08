package com.ilargia.games.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Core"}, isSingleEntity = true)
public class Ball implements IComponent {
    public boolean resetBall;


}