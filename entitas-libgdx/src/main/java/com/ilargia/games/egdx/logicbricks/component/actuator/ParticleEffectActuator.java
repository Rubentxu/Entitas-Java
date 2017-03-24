package com.ilargia.games.egdx.logicbricks.component.actuator;


import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Actuator"})
public class ParticleEffectActuator implements IComponent {
    public ParticleEffect effect;
    public boolean autoStart = false;



}
