package com.ilargia.games.logicbrick.component;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.Component;

@Component(pools = {"Core"}, isSingleEntity = true)
public class Ball implements IComponent {
    public boolean resetBall;


}