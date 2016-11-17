package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.interfaces.*;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;

public class ReactiveSystem implements IExecuteSystem {

    private IReactiveExecuteSystem _subsystem;
    private EntityCollector _collector;
    private IMatcher _ensureComponents;
    private IMatcher _excludeComponents;
    private boolean _clearAfterExecute;
    private Array<Entity> _buffer;
    private String _toStringCache;

    public ReactiveSystem(Pool pool, IReactiveSystem subSystem) {
        this(subSystem, createEntityCollector(pool, new TriggerOnEvent[]{subSystem.getTrigger()}));
    }

    public ReactiveSystem(Pool pool, IMultiReactiveSystem subSystem) {
        this(subSystem, createEntityCollector(pool, subSystem.getTriggers()));
    }

    public ReactiveSystem(IEntityCollectorSystem subSystem) {
        this(subSystem, subSystem.getEntityCollector());
    }

    private ReactiveSystem(IReactiveExecuteSystem subSystem, EntityCollector collector) {
        _subsystem = subSystem;
        IEnsureComponents ensureComponents = (IEnsureComponents) ((subSystem instanceof IEnsureComponents) ? subSystem : null);
        if (ensureComponents != null) {
            _ensureComponents = ensureComponents.getEnsureComponents();
        }
        IExcludeComponents excludeComponents = (IExcludeComponents) ((subSystem instanceof IExcludeComponents) ? subSystem : null);
        if (excludeComponents != null) {
            _excludeComponents = excludeComponents.getExcludeComponents();
        }

        _clearAfterExecute = ((IClearReactiveSystem) ((subSystem instanceof IClearReactiveSystem) ? subSystem : null)) != null;

        _collector = collector;
        _buffer = new Array();
    }

    static EntityCollector createEntityCollector(Pool pool, TriggerOnEvent[] triggers) {
        int triggersLength = triggers.length;
        Group[] groups = new Group[triggersLength];
        GroupEventType[] eventTypes = new GroupEventType[triggersLength];
        for (int i = 0; i < triggersLength; i++) {
            TriggerOnEvent trigger = triggers[i];
            groups[i] = pool.getGroup(trigger.trigger);
            eventTypes[i] = trigger.eventType;
        }

        return new EntityCollector(groups, eventTypes);
    }

    public IReactiveExecuteSystem getSubsystem() {
        return _subsystem;
    }

    public void activate() {
        _collector.activate();
    }

    public void deactivate() {
        _collector.deactivate();
    }

    public void clear() {
        _collector.clearCollectedEntities();
    }

    public void execute() {

        if (_collector._collectedEntities.size != 0) {
            if (_ensureComponents != null) {

                if (_excludeComponents != null) {
                    ObjectSet.ObjectSetIterator iterator = _collector._collectedEntities.iterator();
                    while (iterator.hasNext()) {
                        Entity e = (Entity) iterator.next();
                        if (_ensureComponents.matches(e) && !_excludeComponents.matches(e)) {
                            _buffer.add(e.retain(this));
                        }
                    }
                } else {
                    ObjectSet.ObjectSetIterator iterator = _collector._collectedEntities.iterator();
                    while (iterator.hasNext()) {
                        Entity e = (Entity) iterator.next();
                        if (_ensureComponents.matches(e)) {
                            _buffer.add(e.retain(this));
                        }
                    }
                }
            } else if (_excludeComponents != null) {
                ObjectSet.ObjectSetIterator iterator = _collector._collectedEntities.iterator();
                while (iterator.hasNext()) {
                    Entity e = (Entity) iterator.next();
                    if (!_excludeComponents.matches(e)) {
                        _buffer.add(e.retain(this));
                    }
                }
            } else {
                ObjectSet.ObjectSetIterator iterator = _collector._collectedEntities.iterator();
                while (iterator.hasNext()) {
                    Entity e = (Entity) iterator.next();
                    _buffer.add(e.retain(this));
                }
            }

            _collector.clearCollectedEntities();
            if (_buffer.size != 0) {
                _subsystem.execute(_buffer);
                for (int i = 0, bufferCount = _buffer.size; i < bufferCount; i++) {
                    _buffer.get(i).release(this);
                }
                _buffer.clear();
                if (_clearAfterExecute) {
                    _collector.clearCollectedEntities();
                }
            }
        }
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            _toStringCache = "ReactiveSystem(" + _subsystem + ")";
        }

        return _toStringCache;
    }
}