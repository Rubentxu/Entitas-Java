package ilargia.egdx.logicbricks.component.scene;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class CPointLight implements IComponent {
    public PointLight light;

    // Config
    public int raysNum;
    public Color color;
    public float distance;
    public Vector2 position;

    public CPointLight(int raysNum, Color color, float distance, Vector2 position) {
        this.raysNum = raysNum;
        this.color = color;
        this.distance = distance;
        this.position = position;
        this.light = null;

    }

}