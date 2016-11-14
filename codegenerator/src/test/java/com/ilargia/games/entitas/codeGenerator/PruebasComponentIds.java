package com.ilargia.games.entitas.codeGenerator;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.Pool;
import com.ilargia.games.entitas.components.Position;
import com.ilargia.games.entitas.components.Movable;
import com.ilargia.games.entitas.exceptions.EntitasException;

public class PruebasComponentIds {

    public static final int Position = 0;
    public static final int Movable = 1;
    public static final int totalComponents = 2;

    public static String[] componentNames() {
        return new String[]{"Position", "Movable"};
    }

    public static Class[] componentTypes() {
        return new Class[]{Position.class, Movable.class};
    }
}


public class Entitas {

    public static Position getPosition(Entity entity) {
        return (Position) entity.getComponent(PruebasComponentIds.Position);
    }

    public static boolean hasPosition(Entity entity) {
        return entity.hasComponent(PruebasComponentIds.Position);
    }

    public static void addPosition(Entity entity, float _x, float _y) {
        Position component = entity.createComponent(
                PruebasComponentIds.Position, Position.class);
        component.x = _x;
        component.y = _y;
        entity.addComponent(PruebasComponentIds.Position, component);
    }

    public static void replacePosition(Entity entity, float _x, float _y) {
        Position component = entity.createComponent(
                PruebasComponentIds.Position, Position.class);
        component.x = _x;
        component.y = _y;
        entity.replaceComponent(PruebasComponentIds.Position, component);
    }

    public static void removePosition(Entity entity) {
        entity.removeComponent(PruebasComponentIds.Position);
    }

    public static Movable getMovable(Entity entity) {
        return (Movable) entity.getComponent(PruebasComponentIds.Movable);
    }

    public static boolean hasMovable(Entity entity) {
        return entity.hasComponent(PruebasComponentIds.Movable);
    }

    public static void addMovable(Entity entity, boolean _isMovable) {
        Movable component = entity.createComponent(PruebasComponentIds.Movable,
                Movable.class);
        component.isMovable = _isMovable;
        entity.addComponent(PruebasComponentIds.Movable, component);
    }

    public static void replaceMovable(Entity entity, boolean _isMovable) {
        Movable component = entity.createComponent(PruebasComponentIds.Movable,
                Movable.class);
        component.isMovable = _isMovable;
        entity.replaceComponent(PruebasComponentIds.Movable, component);
    }

    public static void removeMovable(Entity entity) {
        entity.removeComponent(PruebasComponentIds.Movable);
    }

    public static Entity getMovableEntity(Pool pool) {
        return pool.getGroup(PruebasMatcher.Movable).getSingleEntity();
    }

    public static Movable getMovable(Pool pool) {
        return Entitas.getMovable(Entitas.getMovableEntity(pool));
    }

    public static boolean hasMovable(Pool pool) {
        return Entitas.getMovableEntity(pool) != null;
    }

    public static Entity setMovable(Pool pool, boolean _isMovable) {
        if (Entitas.hasMovable(pool)) {
            throw new EntitasException(
                    "Could not set Movable!" + pool
                            + " already has an entity with Movable!",
                    "You should check if the pool already has a MovableEntity before setting it or use pool.ReplaceMovable().");
        }
        Entity entity = pool.createEntity();
        Entitas.addMovable(entity, _isMovable);
        return entity;
    }

    public static Entity replaceMovable(Pool pool, boolean _isMovable) {
        Entity entity = Entitas.getMovableEntity(pool);
        if (entity == null) {
            entity = Entitas.setMovable(pool, _isMovable);
        } else {
            Entitas.replaceMovable(entity, _isMovable);
        }
        return entity;
    }

    public static void removeMovable(Pool pool) {
        pool.destroyEntity(Entitas.getMovableEntity(pool));
    }
}