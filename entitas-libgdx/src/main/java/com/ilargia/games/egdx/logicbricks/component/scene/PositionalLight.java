package com.ilargia.games.egdx.logicbricks.component.scene;

import box2dLight.ChainLight;
import box2dLight.ConeLight;
import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class PositionalLight implements IComponent {
    public PointLight light;

    // Config
    public int raysNum;
    public Color color;
    public float distance;
    public Vector2 position;

    public PositionalLight(int raysNum, Color color,float distance, Vector2 position) {
        this.raysNum = raysNum;
        this.color = color;
        this.distance = distance;
        this.position = position;
        this.light = null;

    }

}