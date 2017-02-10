/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.collector {
    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IEntity = com.ilargia.games.entitas.api.IEntity;

    import IGroup = com.ilargia.games.entitas.api.IGroup;

    import GroupChanged = com.ilargia.games.entitas.api.events.GroupChanged;

    import GroupEvent = com.ilargia.games.entitas.events.GroupEvent;

    import EntityCollectorException = com.ilargia.games.entitas.exceptions.EntityCollectorException;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import Group = com.ilargia.games.entitas.group.Group;

    import Arrays = java.util.Arrays;

    import Set = java.util.Set;

    export class Collector<TEntity extends IEntity> {
        public _collectedEntities : Set<TEntity>;

        _addEntityCache : GroupChanged<TEntity>;

        _toStringCache : string;

        _toStringBuilder : java.lang.StringBuilder;

        private _groups : IGroup<TEntity>[];

        private _groupEvents : GroupEvent[];

        public constructor(group? : any, eventType? : any) {
            if(((group != null && (group["__interfaces"] != null && group["__interfaces"].indexOf("com.ilargia.games.entitas.api.IGroup") >= 0 || group.constructor != null && group.constructor["__interfaces"] != null && group.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.IGroup") >= 0)) || group === null) && ((typeof eventType === 'number') || eventType === null)) {
                let __args = Array.prototype.slice.call(arguments);
                {
                    let __args = Array.prototype.slice.call(arguments);
                    let groups : any = [group];
                    let groupEvents : any = [eventType];
                    (() => {
                        this._groups = groups;
                        this._collectedEntities = Collections.createSet<any>("com.ilargia.games.entitas.api.IEntity");
                        this._groupEvents = groupEvents;
                        if(groups.length !== groupEvents.length) {
                            throw new EntityCollectorException("Unbalanced count with groups (" + groups.length + ") and event types (" + groupEvents.length + ").", "Group and event type count must be equal.");
                        }
                        this._addEntityCache = (group, entity, index, component) => {
                            this.addEntity(group, entity, index, component);
                        };
                        this.activate();
                    })();
                }
            } else if(((group != null && group instanceof Array) || group === null) && ((eventType != null && eventType instanceof Array) || eventType === null)) {
                let __args = Array.prototype.slice.call(arguments);
                let groups : any = __args[0];
                let groupEvents : any = __args[1];
                (() => {
                    this._groups = groups;
                    this._collectedEntities = Collections.createSet<any>("com.ilargia.games.entitas.api.IEntity");
                    this._groupEvents = groupEvents;
                    if(groups.length !== groupEvents.length) {
                        throw new EntityCollectorException("Unbalanced count with groups (" + groups.length + ") and event types (" + groupEvents.length + ").", "Group and event type count must be equal.");
                    }
                    this._addEntityCache = (group, entity, index, component) => {
                        this.addEntity(group, entity, index, component);
                    };
                    this.activate();
                })();
            } else throw new Error('invalid overload');
        }

        public activate() {
            for(let i : number = 0; i < this._groups.length; i++) {
                let group : Group<any> = <Group<any>>this._groups[i];
                let groupEvent : GroupEvent = this._groupEvents[i];
                switch((groupEvent)) {
                case com.ilargia.games.entitas.events.GroupEvent.Added:
                    group.OnEntityAdded(this._addEntityCache);
                    break;
                case com.ilargia.games.entitas.events.GroupEvent.Removed:
                    group.OnEntityRemoved(this._addEntityCache);
                    break;
                case com.ilargia.games.entitas.events.GroupEvent.AddedOrRemoved:
                    group.OnEntityAdded(this._addEntityCache);
                    group.OnEntityRemoved(this._addEntityCache);
                    break;
                }
            }
        }

        public deactivate() {
            for(let i : number = 0; i < this._groups.length; i++) {
                let group : Group<any> = <Group<any>>this._groups[i];
                group.__OnEntityAdded.clear();
                group.__OnEntityRemoved.clear();
            }
            this.clearCollectedEntities();
        }

        public clearCollectedEntities() {
            for(let index121=this._collectedEntities.iterator();index121.hasNext();) {
                let entity = index121.next();
                {
                    entity.release(this);
                }
            }
            this._collectedEntities.clear();
        }

        addEntity(group : IGroup<TEntity>, entity : TEntity, index : number, component : IComponent) {
            let added : boolean = this._collectedEntities.add(entity);
            if(added) {
                entity.retain(this);
            }
        }

        public toString() : string {
            return "Collector{_collectedEntities=" + this._collectedEntities + ", _groups=" + Arrays.toString(this._groups) + ", _groupEvents=" + Arrays.toString(this._groupEvents) + ", _addEntityCache=" + this._addEntityCache + ", _toStringCache=\'" + this._toStringCache + '\'' + ", _toStringBuilder=" + this._toStringBuilder + '}';
        }
    }
    Collector["__class"] = "com.ilargia.games.entitas.collector.Collector";

}

