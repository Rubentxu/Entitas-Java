package ilargia.entitas.api;

import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.events.GroupChanged;
import ilargia.entitas.api.matcher.IMatcher;

import java.util.Set;

public interface IGroup<TEntity extends IEntity> {

    int getCount();

    void removeAllEventHandlers();

    IMatcher<TEntity> getMatcher();

    void handleEntitySilently(TEntity entity);

    void handleEntity(TEntity entity, int index, IComponent component);

    Set<GroupChanged> handleEntity(TEntity entity);

    void updateEntity(TEntity entity, int index, IComponent previousComponent, IComponent newComponent);

    boolean containsEntity(TEntity entity);

    TEntity[] getEntities();

    TEntity getSingleEntity();


}
