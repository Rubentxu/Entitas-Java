package ilargia.entitas.api;


import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.api.entitas.IEntityIndex;
import ilargia.entitas.api.matcher.IMatcher;
import ilargia.entitas.collector.Collector;
import ilargia.entitas.group.GroupEvent;

import java.util.Stack;

public interface IContext<TEntity extends IEntity> {

    TEntity createEntity();

    @Deprecated
    void destroyEntity(TEntity entity);

    boolean hasEntity(TEntity entity);

    TEntity[] getEntities();

    IGroup<TEntity> getGroup(IMatcher<TEntity> matcher);

    int getTotalComponents();

    Stack<IComponent>[] getComponentPools();

    ContextInfo getContextInfo();

    int getCount();

    int getReusableEntitiesCount();

    int getRetainedEntitiesCount();

    void destroyAllEntities();

    void addEntityIndex(String name, IEntityIndex entityIndex);

    IEntityIndex getEntityIndex(String name);

    void resetCreationIndex();

    void clearComponentPool(int index);

    void clearComponentPools();

    void reset();

    Collector createCollector(IMatcher matcher);

    Collector createCollector(IMatcher matcher, GroupEvent groupEvent);

//    Collector createEntityCollector(Context[] contexts, IMatcher matcher);
//
//    Collector createEntityCollector(Context[] contexts, IMatcher matcher, GroupEvent groupEvent);


}
