package ilargia.entitas.fixtures.components.dir;

import com.badlogic.gdx.math.Vector2;
import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.annotations.Contexts;

@Contexts(names = {"Test"})
public class Motion implements IComponent {
    public Vector2 velocity;

    public Motion(float x, float y) {
        this.velocity = new Vector2(x, y);
    }


}
