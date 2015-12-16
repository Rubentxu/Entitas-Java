package com.ilargia.games.entitas;

import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.interfaces.*;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;

import java.util.ArrayList;

public class ReactiveSystem implements IExecuteSystem {
    private IReactiveExecuteSystem _subsystem;
    private GroupObserver _observer;
    private IMatcher _ensureComponents;
    private IMatcher _excludeComponents;
    private boolean _clearAfterExecute;
    private ArrayList<Entity> _buffer;

    public ReactiveSystem(Pool pool, IReactiveSystem subSystem) {
        this(pool, subSystem, new TriggerOnEvent[]{subSystem.getTrigger()});
    }

    public ReactiveSystem(Pool pool, IMultiReactiveSystem subSystem) {
        this(pool, subSystem, subSystem.getTriggers());
    }

    private ReactiveSystem(Pool pool, IReactiveExecuteSystem subSystem, TriggerOnEvent[] triggers) {
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

        int triggersLength = triggers.length;
        Group[] groups = new Group[triggersLength];
        GroupEventType[] eventTypes = new GroupEventType[triggersLength];
        for (int i = 0; i < triggersLength; i++) {
            TriggerOnEvent trigger = triggers[i];
            groups[i] = pool.getGroup(trigger.trigger);
            eventTypes[i] = trigger.eventType;
        }
        _observer = new GroupObserver(groups, eventTypes);
        _buffer = new ArrayList<Entity>();
    }

    public IReactiveExecuteSystem getsubsystem() {
        return _subsystem;
    }

    public void activate() {
        _observer.activate();
    }

    public void deactivate() {
        _observer.deactivate();
    }

    public void clear() {
        _observer.clearCollectedEntities();
    }

    public void execute() {
        if (_observer.getcollectedEntities().size() != 0) {
            if (_ensureComponents != null) {
                if (_excludeComponents != null) {
                    for (Entity e : _observer.getcollectedEntities()) {
                        if (_ensureComponents.matches(e) && !_excludeComponents.matches(e)) {
                            _buffer.add(e.retain(this));
                        }
                    }
                } else {
                    for (Entity e : _observer.getcollectedEntities()) {
                        if (_ensureComponents.matches(e)) {
                            _buffer.add(e.retain(this));
                        }
                    }
                }
            } else if (_excludeComponents != null) {
                for (Entity e : _observer.getcollectedEntities()) {
                    if (!_excludeComponents.matches(e)) {
                        _buffer.add(e.retain(this));
                    }
                }
            } else {
                for (Entity e : _observer.getcollectedEntities()) {
                    _buffer.add(e.retain(this));
                }
            }

            _observer.clearCollectedEntities();
            if (_buffer.size() != 0) {
                _subsystem.execute(_buffer);
                for (int i = 0, bufferCount = _buffer.size(); i < bufferCount; i++) {
                    _buffer.get(i).release(this);
                }
                _buffer.clear();
                if (_clearAfterExecute) {
                    _observer.clearCollectedEntities();
                }
            }
        }
    }
}