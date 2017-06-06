package ilargia.entitas.fixtures;

import ilargia.entitas.Entity;
import ilargia.entitas.fixtures.components.View;
import ilargia.entitas.fixtures.components.test.Motion;
import ilargia.entitas.fixtures.components.test.Size;
import ilargia.entitas.fixtures.components.test.Interactive;
import ilargia.entitas.fixtures.components.test.Player;
import ilargia.entitas.fixtures.components.test.Position;

public class TestEntity extends Entity {

    public Interactive InteractiveComponent = new Interactive();
    public Player PlayerComponent = new Player(Player.ID.PLAYER1);

    public TestEntity() {

    }

    public boolean isInteractive() {
        return hasComponent(TestComponentIds.Interactive);
    }

    public TestEntity setInteractive(boolean value) {
        if (value != hasComponent(TestComponentIds.Interactive)) {
            if (value) {
                addComponent(TestComponentIds.Interactive, InteractiveComponent);
            } else {
                removeComponent(TestComponentIds.Interactive);
            }
        }
        return this;
    }

    public Motion getMotion() {
        return (Motion) getComponent(TestComponentIds.Motion);
    }

    public boolean hasMotion() {
        return hasComponent(TestComponentIds.Motion);
    }

    public TestEntity addMotion(float x, float y) {
        Motion component = (Motion) recoverComponent(TestComponentIds.Motion);
        if (component == null) {
            component = new Motion(x, y);
        } else {
            component.xVelocity = x;
            component.yVelocity = y;
        }
        addComponent(TestComponentIds.Motion, component);
        return this;
    }

    public TestEntity replaceMotion(float x, float y) {
        Motion component = (Motion) recoverComponent(TestComponentIds.Motion);
        if (component == null) {
            component = new Motion(x, y);
        } else {
            component.xVelocity = x;
            component.yVelocity = y;
        }
        replaceComponent(TestComponentIds.Motion, component);
        return this;
    }

    public TestEntity removeMotion() {
        removeComponent(TestComponentIds.Motion);
        return this;
    }

    public boolean isPlayer() {
        return hasComponent(TestComponentIds.Player);
    }

    public TestEntity setPlayer(boolean value) {
        if (value != hasComponent(TestComponentIds.Player)) {
            if (value) {
                addComponent(TestComponentIds.Player, PlayerComponent);
            } else {
                removeComponent(TestComponentIds.Player);
            }
        }
        return this;
    }

    public Position getPosition() {
        return (Position) getComponent(TestComponentIds.Position);
    }

    public boolean hasPosition() {
        return hasComponent(TestComponentIds.Position);
    }

    public TestEntity addPosition(float x, float y) {
        Position component = (Position) recoverComponent(TestComponentIds.Position);
        if (component == null) {
            component = new Position(x, y);
        } else {
            component.x = x;
            ;
            component.y = y;
        }
        addComponent(TestComponentIds.Position, component);
        return this;
    }

    public TestEntity replacePosition(float x, float y) {
        Position component = (Position) recoverComponent(TestComponentIds.Position);
        if (component == null) {
            component = new Position(x, y);
        } else {
            component.x = x;
            ;
            component.y = y;
        }
        replaceComponent(TestComponentIds.Position, component);
        return this;
    }

    public TestEntity removePosition() {
        removeComponent(TestComponentIds.Position);
        return this;
    }

    public View getView() {
        return (View) getComponent(TestComponentIds.View);
    }

    public boolean hasView() {
        return hasComponent(TestComponentIds.View);
    }

    public TestEntity addView(int shape) {
        View component = (View) recoverComponent(TestComponentIds.View);
        if (component == null) {
            component = new View(shape);
        } else {
            component.shape = shape;
        }
        addComponent(TestComponentIds.View, component);
        return this;
    }

    public TestEntity replaceView(int shape) {
        View component = (View) recoverComponent(TestComponentIds.View);
        if (component == null) {
            component = new View(shape);
        } else {
            component.shape = shape;
        }
        replaceComponent(TestComponentIds.View, component);
        return this;
    }

    public TestEntity removeView() {
        removeComponent(TestComponentIds.View);
        return this;
    }

    public Size getSize() {
        return (Size) getComponent(TestComponentIds.Size);
    }

    public boolean hasSize() {
        return hasComponent(TestComponentIds.Size);
    }

    public TestEntity addSize(int width, int height) {
        Size component = (Size) recoverComponent(TestComponentIds.Size);
        if (component == null) {
            component = new Size(width, height);
        } else {
            component.width = width;
            component.height = height;
        }
        addComponent(TestComponentIds.Size, component);
        return this;
    }

    public TestEntity replaceSize(int width, int height) {
        Size component = (Size) recoverComponent(TestComponentIds.Size);
        if (component == null) {
            component = new Size(width, height);
        } else {
            component.width = width;
            component.height = height;
        }
        replaceComponent(TestComponentIds.Size, component);
        return this;
    }

    public TestEntity removeSize() {
        removeComponent(TestComponentIds.Size);
        return this;
    }
}