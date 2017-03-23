package com.ilargia.games.egdx.logicbricks.component.scene;

import box2dLight.ChainLight;
import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class CChainLight implements IComponent {
    public ChainLight light;

    // Config
    public int raysNum;
    public Color color;
    public float distance;
    public int rayDirecction;
    public float[] chain;

    public CChainLight(int raysNum, Color color, float distance, int rayDirecction, float[] chain ) {
        this.raysNum = raysNum;
        this.color = color;
        this.distance = distance;
        this.rayDirecction = rayDirecction;
        this.chain = chain;
        this.light = null;

    }

}