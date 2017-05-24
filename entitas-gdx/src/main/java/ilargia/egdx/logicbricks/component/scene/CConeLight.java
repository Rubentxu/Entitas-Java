package ilargia.egdx.logicbricks.component.scene;

import box2dLight.ConeLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Scene"})
public class CConeLight implements IComponent {
    public ConeLight light;

    // Config
    public int raysNum;
    public Color color;
    public float distance;
    public Vector2 position;
    public float directionDegree;
    public float coneDegree;

    public CConeLight(int raysNum, Color color, float distance, Vector2 position, float directionDegree, float coneDegree) {
        this.raysNum = raysNum;
        this.color = color;
        this.distance = distance;
        this.position = position;
        this.directionDegree = directionDegree;
        this.coneDegree = coneDegree;
        this.light = null;

    }

}