package ilargia.entitas.fixtures;

import ilargia.entitas.Context;
import ilargia.entitas.api.ContextInfo;
import ilargia.entitas.api.entitas.EntityBaseFactory;

public class TestContext extends Context<TestEntity> {

    public TestContext(int totalComponents, int startCreationIndex,
                       ContextInfo contextInfo, EntityBaseFactory<TestEntity> factoryMethod) {
        super(totalComponents, startCreationIndex, contextInfo, factoryMethod, null);
    }

    public TestEntity getPlayerEntity() {
        return getGroup(TestMatcher.Player()).getSingleEntity();
    }

    public boolean isPlayer() {
        return getPlayerEntity() != null;
    }

    public TestContext setPlayer(boolean value) {
        TestEntity entity = getPlayerEntity();
        if (value != (entity != null)) {
            if (value) {
                entity.setPlayer(true);
            } else {
                destroyEntity(entity);
            }
        }
        return this;
    }
}