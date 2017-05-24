package ilargia.egdx.logicbricks.component.scene;

import box2dLight.DirectionalLight;
import com.badlogic.gdx.graphics.Color;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class CDirectionalLight implements IComponent {
    public DirectionalLight light;

    // Config
    public int raysNum;
    public Color color;
    public float direcction;

    public CDirectionalLight(int raysNum, Color color, float direcction) {
        this.raysNum = raysNum;
        this.color = color;
        this.direcction = direcction;
        this.light = null;

    }

}