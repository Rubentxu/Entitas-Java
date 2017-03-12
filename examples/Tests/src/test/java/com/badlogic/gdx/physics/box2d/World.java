package com.badlogic.gdx.physics.box2d;

import com.badlogic.gdx.math.Vector2;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;


public class World {
    Body body = Mockito.mock(Body.class);
    Fixture fixture = Mockito.mock(Fixture.class);

    public World() {
        body = Mockito.mock(Body.class);
        fixture = Mockito.mock(Fixture.class);
        when(fixture.getBody()).thenReturn(body);
    }

    public void setIndexEntity(Integer indexEntity) {
        when(body.getUserData()).thenReturn(indexEntity);
    }

    public void rayCast (RayCastCallback callback, Vector2 point1, Vector2 point2) {
        callback.reportRayFixture(fixture, new Vector2(), new Vector2(), 1F);
    }
}
