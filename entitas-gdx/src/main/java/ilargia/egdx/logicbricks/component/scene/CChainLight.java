package ilargia.egdx.logicbricks.component.scene;

import box2dLight.ChainLight;
import com.badlogic.gdx.graphics.Color;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


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