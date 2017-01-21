package com.ilargia.games.entitas;

import com.ilargia.games.entitas.events.EventBus;
import com.ilargia.games.entitas.events.GroupEvent;
import com.ilargia.games.entitas.factories.Collections;
import com.ilargia.games.entitas.interfaces.*;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;

import java.util.List;

public abstract class ReactiveSystem implements ISystem.IExecuteSystem {

    private Collector<Entity> _collector;
    private List<Entity> _buffer;//ObjectArrayList
    private String _toStringCache;

    protected ReactiveSystem(BaseContext context) {
        _collector = GetTrigger(context);
        _buffer = Collections.createList(Entity.class);
    }

    protected ReactiveSystem(Collector collector) {
        _collector = collector;
        _buffer = Collections.createList(Entity.class);
    }

    protected abstract Collector GetTrigger(BaseContext context);

    protected abstract boolean Filter(Entity entity);

    protected abstract void Execute(List<Entity> entities);


    static Collector createEntityCollector(BaseContext contexts, TriggerOnEvent[] triggers, EventBus eventBus) {
        int triggersLength = triggers.length;
        Group[] groups = new Group[triggersLength];
        GroupEvent[] eventTypes = new GroupEvent[triggersLength];
        for (int i = 0; i < triggersLength; i++) {
            TriggerOnEvent trigger = triggers[i];
            groups[i] = contexts.getGroup(trigger.trigger);
            eventTypes[i] = trigger.eventType;
        }

        return new Collector(groups, eventTypes, eventBus);
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

    public void execute(float deltatime) {

        if (_collector._collectedEntities.size() != 0) {
            for(Entity e : _collector._collectedEntities) {
                if(Filter(e)) {
                    e.retain(this);
                    _buffer.add(e);
                }
            }
            _collector.clearCollectedEntities();

            if(_buffer.size() != 0) {
                Execute(_buffer);
                for (int i = 0; i < _buffer.size(); i++) {
                    _buffer.get(i).release(this);
                }
                _buffer.clear();
            }
        }
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            _toStringCache = "ReactiveSystem(" + getClass().getName() + ")";
        }

        return _toStringCache;
    }
}