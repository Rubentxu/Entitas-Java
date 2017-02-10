/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.group {
    import Entity = com.ilargia.games.entitas.Entity;

    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IEntity = com.ilargia.games.entitas.api.IEntity;

    import IGroup = com.ilargia.games.entitas.api.IGroup;

    import GroupChanged = com.ilargia.games.entitas.api.events.GroupChanged;

    import GroupUpdated = com.ilargia.games.entitas.api.events.GroupUpdated;

    import IMatcher = com.ilargia.games.entitas.api.matcher.IMatcher;

    import Collector = com.ilargia.games.entitas.collector.Collector;

    import GroupEvent = com.ilargia.games.entitas.events.GroupEvent;

    import GroupSingleEntityException = com.ilargia.games.entitas.exceptions.GroupSingleEntityException;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import Iterator = java.util.Iterator;

    import Set = java.util.Set;

    export class Group<TEntity extends IEntity> implements IGroup<TEntity> {
        public type : any;

        public __OnEntityAdded : Set<GroupChanged<any>> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.GroupChanged");

        public __OnEntityRemoved : Set<GroupChanged<any>> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.GroupChanged");

        public __OnEntityUpdated : Set<GroupUpdated<any>> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.GroupUpdated");

        private _matcher : IMatcher<TEntity>;

        private _entities : Set<TEntity>;

        private _entitiesCache : TEntity[];

        private _singleEntityCache : TEntity;

        public constructor(matcher : IMatcher<TEntity>, clazz : any) {
            this._entities = Collections.createSet<any>(Entity);
            this._matcher = matcher;
            this.type = clazz;
        }

        public static createCollector<TE extends Entity>(group : IGroup<TE>, groupEvent : GroupEvent) : Collector<TE> {
            return <any>(new Collector<TE>(group, groupEvent));
        }

        public getCount() : number {
            return this._entities.size();
        }

        public getMatcher() : IMatcher<any> {
            return this._matcher;
        }

        public handleEntitySilently(entity : TEntity) {
            if(this._matcher.matches(entity)) {
                this.addEntitySilently(entity);
            } else {
                this.removeEntitySilently(entity);
            }
        }

        public handleEntity(entity? : any, index? : any, component? : any) : any {
            if(((entity != null) || entity === null) && ((typeof index === 'number') || index === null) && ((component != null && (component["__interfaces"] != null && component["__interfaces"].indexOf("com.ilargia.games.entitas.api.IComponent") >= 0 || component.constructor != null && component.constructor["__interfaces"] != null && component.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.IComponent") >= 0)) || component === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    if(this._matcher.matches(entity)) {
                        this.addEntitySilently(entity);
                    } else {
                        this.removeEntitySilently(entity);
                    }
                })();
            } else if(((entity != null) || entity === null) && index === undefined && component === undefined) {
                return <any>this.handleEntity$com_ilargia_games_entitas_api_IEntity(entity);
            } else throw new Error('invalid overload');
        }

        public updateEntity(entity : TEntity, index : number, previousComponent : IComponent, newComponent : IComponent) {
            if(this._entities.contains(entity)) {
                this.notifyOnEntityRemoved(entity, index, previousComponent);
                this.notifyOnEntityAdded(entity, index, previousComponent);
                this.notifyOnEntityUpdated(entity, index, previousComponent, newComponent);
            }
        }

        public removeAllEventHandlers() {
            this.__OnEntityAdded.clear();
            this.__OnEntityRemoved.clear();
            this.__OnEntityUpdated.clear();
        }

        public handleEntity$com_ilargia_games_entitas_api_IEntity(entity : TEntity) : Set<GroupChanged<any>> {
            return (this._matcher.matches(entity))?(this.addEntitySilently(entity))?this.__OnEntityAdded:null:(this.removeEntitySilently(entity))?this.__OnEntityRemoved:null;
        }

        private addEntitySilently(entity : TEntity) : boolean {
            let added : boolean = this._entities.add(entity);
            if(added) {
                this._entitiesCache = null;
                this._singleEntityCache = null;
                entity.retain(this);
            }
            return added;
        }

        addEntity(entity : TEntity, index : number, component : IComponent) {
            if(this.addEntitySilently(entity)) {
                this.notifyOnEntityAdded(entity, index, component);
            }
        }

        private removeEntitySilently(entity : TEntity) : boolean {
            let removed : boolean = this._entities.remove(entity);
            if(removed) {
                this._entitiesCache = null;
                this._singleEntityCache = null;
                entity.release(this);
            }
            return removed;
        }

        removeEntity(entity : TEntity, index : number, component : IComponent) {
            let removed : boolean = this._entities.remove(entity);
            if(removed) {
                this._entitiesCache = null;
                this._singleEntityCache = null;
                this.notifyOnEntityRemoved(entity, index, component);
                entity.release(this);
            }
        }

        public containsEntity(entity : TEntity) : boolean {
            return this._entities.contains(entity);
        }

        public getEntities() : TEntity[] {
            if(this._entitiesCache == null) {
                this._entitiesCache = <TEntity[]>java.lang.reflect.Array.newInstance(this.type, this._entities.size());
                let i : number = 0;
                for(let index144=this._entities.iterator();index144.hasNext();) {
                    let entity = index144.next();
                    {
                        this._entitiesCache[i] = entity;
                        i++;
                    }
                }
            }
            return this._entitiesCache;
        }

        public getSingleEntity() : TEntity {
            if(this._singleEntityCache == null) {
                let c : number = this._entities.size();
                if(c === 1) {
                    let enumerator : Iterator<TEntity> = this._entities.iterator();
                    this._singleEntityCache = enumerator.next();
                } else if(c === 0) {
                    return null;
                } else {
                    throw new GroupSingleEntityException(this);
                }
            }
            return this._singleEntityCache;
        }

        public equals(o : any) : boolean {
            if(this === o) return true;
            if(!(o != null && o instanceof com.ilargia.games.entitas.group.Group)) return false;
            let group : Group<any> = <Group<any>>o;
            if(this._matcher != null?!this._matcher.equals(group._matcher):group._matcher != null) return false;
            if(this._entities != null?!this._entities.equals(group._entities):group._entities != null) return false;
            return this.type != null?this.type.equals(group.type):group.type == null;
        }

        public hashCode() : number {
            let result : number = this._matcher != null?this._matcher.hashCode():0;
            result = 31 * result + (this.type != null?this.type.hashCode():0);
            return result;
        }

        public toString() : string {
            return "Group{_matcher=" + this._matcher + ", _entities=" + this._entities + ", _singleEntityCache=" + this._singleEntityCache + ", type=" + this.type + '}';
        }

        public clearEventsListener() {
            if(this.__OnEntityAdded != null) this.__OnEntityAdded.clear();
            if(this.__OnEntityRemoved != null) this.__OnEntityRemoved.clear();
            if(this.__OnEntityUpdated != null) this.__OnEntityUpdated.clear();
        }

        public OnEntityAdded(listener : GroupChanged<TEntity>) {
            if(this.__OnEntityAdded != null) {
                this.__OnEntityAdded = Collections.createSet<any>("com.ilargia.games.entitas.api.events.GroupChanged");
            }
            this.__OnEntityAdded.add(listener);
        }

        public OnEntityUpdated(listener : GroupUpdated<TEntity>) {
            if(this.__OnEntityUpdated != null) {
                this.__OnEntityUpdated = Collections.createSet<any>("com.ilargia.games.entitas.api.events.GroupUpdated");
            }
            this.__OnEntityUpdated.add(listener);
        }

        public OnEntityRemoved(listener : GroupChanged<TEntity>) {
            if(this.__OnEntityRemoved != null) {
                this.__OnEntityRemoved = Collections.createSet<any>("com.ilargia.games.entitas.api.events.GroupChanged");
            }
            this.__OnEntityRemoved.add(listener);
        }

        public notifyOnEntityAdded(entity : TEntity, index : number, component : IComponent) {
            if(this.__OnEntityAdded != null) {
                for(let index145=this.__OnEntityAdded.iterator();index145.hasNext();) {
                    let listener = index145.next();
                    {
                        listener(this, entity, index, component);
                    }
                }
            }
        }

        public notifyOnEntityUpdated(entity : TEntity, index : number, component : IComponent, newComponent : IComponent) {
            if(this.__OnEntityUpdated != null) {
                for(let index146=this.__OnEntityUpdated.iterator();index146.hasNext();) {
                    let listener = index146.next();
                    {
                        listener(this, entity, index, component, newComponent);
                    }
                }
            }
        }

        public notifyOnEntityRemoved(entity : TEntity, index : number, component : IComponent) {
            if(this.__OnEntityRemoved != null) {
                for(let index147=this.__OnEntityRemoved.iterator();index147.hasNext();) {
                    let listener = index147.next();
                    {
                        listener(this, entity, index, component);
                    }
                }
            }
        }
    }
    Group["__class"] = "com.ilargia.games.entitas.group.Group";
    Group["__interfaces"] = ["com.ilargia.games.entitas.api.IGroup"];


}

