package ilargia.entitas.api.system;

import ilargia.entitas.Entity;

import java.util.List;

public interface IReactiveExecuteSystem<E extends Entity> extends ISystem {
    void execute(List<Entity> entities);
}