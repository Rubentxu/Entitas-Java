/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas {
    import ContextInfo = com.ilargia.games.entitas.api.ContextInfo;

    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import IEntity = com.ilargia.games.entitas.api.IEntity;

    import EntityComponentChanged = com.ilargia.games.entitas.api.events.EntityComponentChanged;

    import EntityComponentReplaced = com.ilargia.games.entitas.api.events.EntityComponentReplaced;

    import EntityReleased = com.ilargia.games.entitas.api.events.EntityReleased;

    import EntitasCache = com.ilargia.games.entitas.caching.EntitasCache;

    import EntityAlreadyHasComponentException = com.ilargia.games.entitas.exceptions.EntityAlreadyHasComponentException;

    import EntityDoesNotHaveComponentException = com.ilargia.games.entitas.exceptions.EntityDoesNotHaveComponentException;

    import EntityIsAlreadyRetainedByOwnerException = com.ilargia.games.entitas.exceptions.EntityIsAlreadyRetainedByOwnerException;

    import EntityIsNotEnabledException = com.ilargia.games.entitas.exceptions.EntityIsNotEnabledException;

    import EntityIsNotRetainedByOwnerException = com.ilargia.games.entitas.exceptions.EntityIsNotRetainedByOwnerException;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import List = java.util.List;

    import Set = java.util.Set;

    import Stack = java.util.Stack;

    export class Entity implements IEntity {
        public __OnComponentAdded : Set<EntityComponentChanged<any>> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityComponentChanged");

        public __OnComponentRemoved : Set<EntityComponentChanged<any>> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityComponentChanged");

        public __OnComponentReplaced : Set<EntityComponentReplaced<any>> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityComponentReplaced");

        public __OnEntityReleased : Set<EntityReleased<any>> = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityReleased");

        private __owners : Set<any>;

        private _creationIndex : number;

        private _isEnabled : boolean;

        private _components : IComponent[];

        private _componentContexts : Stack<IComponent>[];

        private _componentsCache : IComponent[];

        private _componentIndicesCache : number[];

        private _totalComponents : number;

        private _contextInfo : ContextInfo;

        public constructor(totalComponents : number, componentContexts : Stack<IComponent>[], contextInfo : ContextInfo) {
            this._creationIndex = 0;
            this._isEnabled = false;
            this._totalComponents = 0;
            this._components = new Array(totalComponents);
            this._totalComponents = totalComponents;
            this._componentContexts = componentContexts;
            this._isEnabled = true;
            this.__owners = Collections.createSet<any>(Object);
            if(contextInfo != null) {
                this._contextInfo = contextInfo;
            } else {
                let componentNames : string[] = new Array(totalComponents);
                for(let i : number = 0; i < componentNames.length; i++) {
                    componentNames[i] = /* valueOf */new String(i).toString();
                }
                this._contextInfo = new ContextInfo("No Context", componentNames, null);
            }
        }

        public getTotalComponents() : number {
            return this._totalComponents;
        }

        public getCreationIndex() : number {
            return this._creationIndex;
        }

        public isEnabled() : boolean {
            return this._isEnabled;
        }

        public componentPools() : Stack<IComponent>[] {
            return new Array(0);
        }

        public contextInfo() : ContextInfo {
            return this._contextInfo;
        }

        public initialize(creationIndex : number, totalComponents : number, componentContexts : Stack<IComponent>[], contextInfo : ContextInfo) {
            this.reactivate(creationIndex);
            this._totalComponents = totalComponents;
            this._components = new Array(totalComponents);
            this._componentContexts = componentContexts;
            if(contextInfo != null) {
                this._contextInfo = contextInfo;
            } else {
                this._contextInfo = this.createDefaultContextInfo();
            }
        }

        public reactivate(creationIndex : number) {
            this._creationIndex = creationIndex;
            this._isEnabled = true;
        }

        public addComponent(index : number, component : IComponent) {
            if(!this._isEnabled) {
                throw new EntityIsNotEnabledException("Cannot add component \'" + this._contextInfo.componentNames[index] + "\' to " + this + "!");
            }
            if(this.hasComponent(index)) {
                throw new EntityAlreadyHasComponentException(index, "Cannot add component \'" + this._contextInfo.componentNames[index] + "\' to " + this + "!", "You should check if an entity already has the component before adding it or use entity.ReplaceComponent().");
            }
            this._components[index] = component;
            this._componentsCache = null;
            this._componentIndicesCache = null;
            this.notifyComponentAdded(index, component);
        }

        public removeComponent(index : number) {
            if(!this._isEnabled) {
                throw new EntityIsNotEnabledException("Cannot remove component!" + this._contextInfo.componentNames[index] + "\' from " + this + "!");
            }
            if(!this.hasComponent(index)) {
                let errorMsg : string = "Cannot remove component " + this._contextInfo.componentNames[index] + "\' from " + this + "!";
                throw new EntityDoesNotHaveComponentException(errorMsg, index);
            }
            this.replaceComponentInternal(index, null);
        }

        public replaceComponent(index : number, component : IComponent) {
            if(!this._isEnabled) {
                throw new EntityIsNotEnabledException("Cannot replace component!" + this._contextInfo.componentNames[index] + "\' on " + this + "!");
            }
            if(this.hasComponent(index)) {
                this.replaceComponentInternal(index, component);
            } else if(component != null) {
                this.addComponent(index, component);
            }
        }

        private replaceComponentInternal(index : number, replacement : IComponent) {
            let previousComponent : IComponent = this._components[index];
            if(replacement !== previousComponent) {
                this._components[index] = replacement;
                this._componentsCache = null;
                if(replacement != null) {
                    this.notifyComponentReplaced(index, previousComponent, replacement);
                } else {
                    this._componentIndicesCache = null;
                    this.notifyComponentRemoved(index, previousComponent);
                }
                this.getComponentPool(index).push(previousComponent);
            } else {
                this.notifyComponentReplaced(index, previousComponent, replacement);
            }
        }

        public getComponent(index : number) : IComponent {
            if(!this.hasComponent(index)) {
                let errorMsg : string = "Cannot get component at index " + this._contextInfo.componentNames[index] + "\' from " + this + "!";
                throw new EntityDoesNotHaveComponentException(errorMsg, index);
            }
            return this._components[index];
        }

        public getComponents() : IComponent[] {
            if(this._componentsCache == null) {
                let componentsCache : List<IComponent> = EntitasCache.getIComponentList();
                for(let i : number = 0; i < this._components.length; i++) {
                    let component : IComponent = this._components[i];
                    if(component != null) {
                        componentsCache.add(component);
                    }
                }
                this._componentsCache = new Array(componentsCache.size());
                componentsCache.toArray<any>(this._componentsCache);
                EntitasCache.pushIComponentList(componentsCache);
            }
            return this._componentsCache;
        }

        public getComponentIndices() : number[] {
            if(this._componentIndicesCache == null) {
                let indices : List<number> = EntitasCache.getIntArray();
                for(let i : number = 0; i < this._components.length; i++) {
                    if(this._components[i] != null) {
                        indices.add(i);
                    }
                }
                this._componentIndicesCache = new Array(indices.size());
                for(let i : number = 0; i < indices.size(); i++) {
                    this._componentIndicesCache[i] = indices.get(i);
                }
                EntitasCache.pushIntArray(indices);
            }
            return this._componentIndicesCache;
        }

        public hasComponent(index : number) : boolean {
            try {
                return this._components[index] != null;
            } catch(ex) {
                throw new EntityDoesNotHaveComponentException("ArrayIndexOutOfBoundsException", index);
            };
        }

        public hasComponents(indices : number[]) : boolean {
            for(let index139=0; index139 < indices.length; index139++) {
                let index = indices[index139];
                {
                    if(this._components[index] == null) {
                        return false;
                    }
                }
            }
            return true;
        }

        public hasAnyComponent(indices : number[]) : boolean {
            for(let i : number = 0; i < indices.length; i++) {
                if(this._components[indices[i]] != null) {
                    return true;
                }
            }
            return false;
        }

        public removeAllComponents() {
            for(let i : number = 0; i < this._components.length; i++) {
                if(this._components[i] != null) {
                    this.replaceComponent(i, null);
                }
            }
        }

        public getComponentPool(index : number) : Stack<IComponent> {
            let componentContext : Stack<IComponent> = this._componentContexts[index];
            if(componentContext == null) {
                componentContext = <any>(new Stack<any>());
                this._componentContexts[index] = componentContext;
            }
            return componentContext;
        }

        public createComponent(index? : any, clazz? : any) : any {
            if(((typeof index === 'number') || index === null) && ((clazz != null && clazz instanceof java.lang.Class) || clazz === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    let componentContext : Stack<IComponent> = this.getComponentPool(index);
                    try {
                        if(componentContext.size() > 0) {
                            return componentContext.pop();
                        } else {
                            return <IComponent>clazz.cast((this['__jswref_0'] = (this['__jswref_1'] = clazz).getConstructor.apply(this['__jswref_1'], <java.lang.Class<any>[]>null)).newInstance.apply(this['__jswref_0'], ));
                        }
                    } catch(e) {
                        return null;
                    };
                })();
            } else if(((typeof index === 'number') || index === null) && clazz === undefined) {
                return <any>this.createComponent$int(index);
            } else throw new Error('invalid overload');
        }

        public createComponent$int(index : number) : IComponent {
            let componentContext : Stack<IComponent> = this.getComponentPool(index);
            try {
                if(componentContext.size() > 0) {
                    return componentContext.pop();
                } else {
                    let clazz : any = this._contextInfo.componentTypes[index];
                    return <IComponent>clazz.cast((this['__jswref_2'] = (this['__jswref_3'] = clazz).getConstructor.apply(this['__jswref_3'], <java.lang.Class<any>[]>null)).newInstance.apply(this['__jswref_2'], ));
                }
            } catch(e) {
                return null;
            };
        }

        public owners() : Set<any> {
            return this.__owners;
        }

        public retainCount() : number {
            return this.__owners.size();
        }

        public retain(owner : any) {
            if(!this.__owners.add(owner)) {
                throw new EntityIsAlreadyRetainedByOwnerException(owner);
            }
        }

        public release(owner : any) {
            if(!this.__owners.remove(owner)) {
                throw new EntityIsNotRetainedByOwnerException(owner);
            }
            if(this.__owners.size() === 0) {
                this.notifyEntityReleased();
            }
        }

        public destroy() {
            this.removeAllComponents();
            this._isEnabled = false;
        }

        createDefaultContextInfo() : ContextInfo {
            let componentNames : string[] = new Array(this._totalComponents);
            for(let i : number = 0; i < componentNames.length; i++) {
                componentNames[i] = /* valueOf */new String(i).toString();
            }
            return new ContextInfo("No Context", componentNames, null);
        }

        public recoverComponent(index : number) : IComponent {
            let componentContext : Stack<IComponent> = this.getComponentPool(index);
            if(componentContext.size() > 0) {
                return componentContext.pop();
            }
            return null;
        }

        public clearEventsListener() {
            if(this.__OnComponentAdded != null) this.__OnComponentAdded.clear();
            if(this.__OnComponentRemoved != null) this.__OnComponentRemoved.clear();
            if(this.__OnComponentReplaced != null) this.__OnComponentReplaced.clear();
            if(this.__OnEntityReleased != null) this.__OnEntityReleased.clear();
        }

        public removeAllOnEntityReleasedHandlers() {
            this.__OnEntityReleased.clear();
        }

        public OnComponentAdded(listener : EntityComponentChanged<any>) {
            if(this.__OnComponentAdded != null) {
                this.__OnComponentAdded = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityComponentChanged");
            }
            this.__OnComponentAdded.add(listener);
        }

        public OnComponentRemoved(listener : EntityComponentChanged<any>) {
            if(this.__OnComponentRemoved != null) {
                this.__OnComponentRemoved = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityComponentChanged");
            }
            this.__OnComponentRemoved.add(listener);
        }

        public OnComponentReplaced(listener : EntityComponentReplaced<any>) {
            if(this.__OnComponentReplaced != null) {
                this.__OnComponentReplaced = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityComponentReplaced");
            }
            this.__OnComponentReplaced.add(listener);
        }

        public OnEntityReleased(listener : EntityReleased<any>) {
            if(this.__OnEntityReleased != null) {
                this.__OnEntityReleased = Collections.createSet<any>("com.ilargia.games.entitas.api.events.EntityReleased");
            }
            this.__OnEntityReleased.add(listener);
        }

        public notifyComponentAdded(index : number, component : IComponent) {
            if(this.__OnComponentAdded != null) {
                for(let index140=this.__OnComponentAdded.iterator();index140.hasNext();) {
                    let listener = index140.next();
                    {
                        listener(this, index, component);
                    }
                }
            }
        }

        public notifyComponentRemoved(index : number, component : IComponent) {
            if(this.__OnComponentRemoved != null) {
                for(let index141=this.__OnComponentRemoved.iterator();index141.hasNext();) {
                    let listener = index141.next();
                    {
                        listener(this, index, component);
                    }
                }
            }
        }

        public notifyComponentReplaced(index : number, previousComponent : IComponent, newComponent : IComponent) {
            if(this.__OnComponentReplaced != null) {
                for(let index142=this.__OnComponentReplaced.iterator();index142.hasNext();) {
                    let listener = index142.next();
                    {
                        listener(this, index, previousComponent, newComponent);
                    }
                }
            }
        }

        public notifyEntityReleased() {
            if(this.__OnEntityReleased != null) {
                for(let index143=this.__OnEntityReleased.iterator();index143.hasNext();) {
                    let listener = index143.next();
                    {
                        listener(this);
                    }
                }
            }
        }

        public equals(o : any) : boolean {
            if(this === o) return true;
            if(o == null || (<any>this.constructor) !== (<any>o.constructor)) return false;
            let entity : Entity = <Entity>o;
            if(this._creationIndex !== entity._creationIndex) return false;
            if(this._totalComponents !== entity._totalComponents) return false;
            return this._contextInfo != null?this._contextInfo.equals(entity._contextInfo):entity._contextInfo == null;
        }

        public hashCode() : number {
            return this._creationIndex;
        }

        public toString() : string {
            return "Entity{_creationIndex=" + this._creationIndex + ", _isEnabled=" + this._isEnabled + ", _contextInfo=" + this._contextInfo + '}';
        }
    }
    Entity["__class"] = "com.ilargia.games.entitas.Entity";
    Entity["__interfaces"] = ["com.ilargia.games.entitas.api.IEntity"];


}

