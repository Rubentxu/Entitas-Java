/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var ContextInfo = com.ilargia.games.entitas.api.ContextInfo;
                var EntitasCache = com.ilargia.games.entitas.caching.EntitasCache;
                var EntityAlreadyHasComponentException = com.ilargia.games.entitas.exceptions.EntityAlreadyHasComponentException;
                var EntityDoesNotHaveComponentException = com.ilargia.games.entitas.exceptions.EntityDoesNotHaveComponentException;
                var EntityIsAlreadyRetainedByOwnerException = com.ilargia.games.entitas.exceptions.EntityIsAlreadyRetainedByOwnerException;
                var EntityIsNotEnabledException = com.ilargia.games.entitas.exceptions.EntityIsNotEnabledException;
                var EntityIsNotRetainedByOwnerException = com.ilargia.games.entitas.exceptions.EntityIsNotRetainedByOwnerException;
                var Collections = com.ilargia.games.entitas.factories.Collections;
                var Stack = java.util.Stack;
                var Entity = (function () {
                    function Entity(totalComponents, componentContexts, contextInfo) {
                        this.__OnComponentAdded = Collections.createSet("com.ilargia.games.entitas.api.events.EntityComponentChanged");
                        this.__OnComponentRemoved = Collections.createSet("com.ilargia.games.entitas.api.events.EntityComponentChanged");
                        this.__OnComponentReplaced = Collections.createSet("com.ilargia.games.entitas.api.events.EntityComponentReplaced");
                        this.__OnEntityReleased = Collections.createSet("com.ilargia.games.entitas.api.events.EntityReleased");
                        this._creationIndex = 0;
                        this._isEnabled = false;
                        this._totalComponents = 0;
                        this._components = new Array(totalComponents);
                        this._totalComponents = totalComponents;
                        this._componentContexts = componentContexts;
                        this._isEnabled = true;
                        this.__owners = Collections.createSet(Object);
                        if (contextInfo != null) {
                            this._contextInfo = contextInfo;
                        }
                        else {
                            var componentNames = new Array(totalComponents);
                            for (var i = 0; i < componentNames.length; i++) {
                                componentNames[i] = new String(i).toString();
                            }
                            this._contextInfo = new ContextInfo("No Context", componentNames, null);
                        }
                    }
                    Entity.prototype.getTotalComponents = function () {
                        return this._totalComponents;
                    };
                    Entity.prototype.getCreationIndex = function () {
                        return this._creationIndex;
                    };
                    Entity.prototype.isEnabled = function () {
                        return this._isEnabled;
                    };
                    Entity.prototype.componentPools = function () {
                        return new Array(0);
                    };
                    Entity.prototype.contextInfo = function () {
                        return this._contextInfo;
                    };
                    Entity.prototype.initialize = function (creationIndex, totalComponents, componentContexts, contextInfo) {
                        this.reactivate(creationIndex);
                        this._totalComponents = totalComponents;
                        this._components = new Array(totalComponents);
                        this._componentContexts = componentContexts;
                        if (contextInfo != null) {
                            this._contextInfo = contextInfo;
                        }
                        else {
                            this._contextInfo = this.createDefaultContextInfo();
                        }
                    };
                    Entity.prototype.reactivate = function (creationIndex) {
                        this._creationIndex = creationIndex;
                        this._isEnabled = true;
                    };
                    Entity.prototype.addComponent = function (index, component) {
                        if (!this._isEnabled) {
                            throw new EntityIsNotEnabledException("Cannot add component \'" + this._contextInfo.componentNames[index] + "\' to " + this + "!");
                        }
                        if (this.hasComponent(index)) {
                            throw new EntityAlreadyHasComponentException(index, "Cannot add component \'" + this._contextInfo.componentNames[index] + "\' to " + this + "!", "You should check if an entity already has the component before adding it or use entity.ReplaceComponent().");
                        }
                        this._components[index] = component;
                        this._componentsCache = null;
                        this._componentIndicesCache = null;
                        this.notifyComponentAdded(index, component);
                    };
                    Entity.prototype.removeComponent = function (index) {
                        if (!this._isEnabled) {
                            throw new EntityIsNotEnabledException("Cannot remove component!" + this._contextInfo.componentNames[index] + "\' from " + this + "!");
                        }
                        if (!this.hasComponent(index)) {
                            var errorMsg = "Cannot remove component " + this._contextInfo.componentNames[index] + "\' from " + this + "!";
                            throw new EntityDoesNotHaveComponentException(errorMsg, index);
                        }
                        this.replaceComponentInternal(index, null);
                    };
                    Entity.prototype.replaceComponent = function (index, component) {
                        if (!this._isEnabled) {
                            throw new EntityIsNotEnabledException("Cannot replace component!" + this._contextInfo.componentNames[index] + "\' on " + this + "!");
                        }
                        if (this.hasComponent(index)) {
                            this.replaceComponentInternal(index, component);
                        }
                        else if (component != null) {
                            this.addComponent(index, component);
                        }
                    };
                    Entity.prototype.replaceComponentInternal = function (index, replacement) {
                        var previousComponent = this._components[index];
                        if (replacement !== previousComponent) {
                            this._components[index] = replacement;
                            this._componentsCache = null;
                            if (replacement != null) {
                                this.notifyComponentReplaced(index, previousComponent, replacement);
                            }
                            else {
                                this._componentIndicesCache = null;
                                this.notifyComponentRemoved(index, previousComponent);
                            }
                            this.getComponentPool(index).push(previousComponent);
                        }
                        else {
                            this.notifyComponentReplaced(index, previousComponent, replacement);
                        }
                    };
                    Entity.prototype.getComponent = function (index) {
                        if (!this.hasComponent(index)) {
                            var errorMsg = "Cannot get component at index " + this._contextInfo.componentNames[index] + "\' from " + this + "!";
                            throw new EntityDoesNotHaveComponentException(errorMsg, index);
                        }
                        return this._components[index];
                    };
                    Entity.prototype.getComponents = function () {
                        if (this._componentsCache == null) {
                            var componentsCache = EntitasCache.getIComponentList();
                            for (var i = 0; i < this._components.length; i++) {
                                var component = this._components[i];
                                if (component != null) {
                                    componentsCache.add(component);
                                }
                            }
                            this._componentsCache = new Array(componentsCache.size());
                            componentsCache.toArray(this._componentsCache);
                            EntitasCache.pushIComponentList(componentsCache);
                        }
                        return this._componentsCache;
                    };
                    Entity.prototype.getComponentIndices = function () {
                        if (this._componentIndicesCache == null) {
                            var indices = EntitasCache.getIntArray();
                            for (var i = 0; i < this._components.length; i++) {
                                if (this._components[i] != null) {
                                    indices.add(i);
                                }
                            }
                            this._componentIndicesCache = new Array(indices.size());
                            for (var i = 0; i < indices.size(); i++) {
                                this._componentIndicesCache[i] = indices.get(i);
                            }
                            EntitasCache.pushIntArray(indices);
                        }
                        return this._componentIndicesCache;
                    };
                    Entity.prototype.hasComponent = function (index) {
                        try {
                            return this._components[index] != null;
                        }
                        catch (ex) {
                            throw new EntityDoesNotHaveComponentException("ArrayIndexOutOfBoundsException", index);
                        }
                        ;
                    };
                    Entity.prototype.hasComponents = function (indices) {
                        for (var index139 = 0; index139 < indices.length; index139++) {
                            var index = indices[index139];
                            {
                                if (this._components[index] == null) {
                                    return false;
                                }
                            }
                        }
                        return true;
                    };
                    Entity.prototype.hasAnyComponent = function (indices) {
                        for (var i = 0; i < indices.length; i++) {
                            if (this._components[indices[i]] != null) {
                                return true;
                            }
                        }
                        return false;
                    };
                    Entity.prototype.removeAllComponents = function () {
                        for (var i = 0; i < this._components.length; i++) {
                            if (this._components[i] != null) {
                                this.replaceComponent(i, null);
                            }
                        }
                    };
                    Entity.prototype.getComponentPool = function (index) {
                        var componentContext = this._componentContexts[index];
                        if (componentContext == null) {
                            componentContext = (new Stack());
                            this._componentContexts[index] = componentContext;
                        }
                        return componentContext;
                    };
                    Entity.prototype.createComponent = function (index, clazz) {
                        var _this = this;
                        if (((typeof index === 'number') || index === null) && ((clazz != null && clazz instanceof java.lang.Class) || clazz === null)) {
                            var __args = Array.prototype.slice.call(arguments);
                            return (function () {
                                var componentContext = _this.getComponentPool(index);
                                try {
                                    if (componentContext.size() > 0) {
                                        return componentContext.pop();
                                    }
                                    else {
                                        return clazz.cast((_this['__jswref_0'] = (_this['__jswref_1'] = clazz).getConstructor.apply(_this['__jswref_1'], null)).newInstance.apply(_this['__jswref_0']));
                                    }
                                }
                                catch (e) {
                                    return null;
                                }
                                ;
                            })();
                        }
                        else if (((typeof index === 'number') || index === null) && clazz === undefined) {
                            return this.createComponent$int(index);
                        }
                        else
                            throw new Error('invalid overload');
                    };
                    Entity.prototype.createComponent$int = function (index) {
                        var componentContext = this.getComponentPool(index);
                        try {
                            if (componentContext.size() > 0) {
                                return componentContext.pop();
                            }
                            else {
                                var clazz = this._contextInfo.componentTypes[index];
                                return clazz.cast((this['__jswref_2'] = (this['__jswref_3'] = clazz).getConstructor.apply(this['__jswref_3'], null)).newInstance.apply(this['__jswref_2']));
                            }
                        }
                        catch (e) {
                            return null;
                        }
                        ;
                    };
                    Entity.prototype.owners = function () {
                        return this.__owners;
                    };
                    Entity.prototype.retainCount = function () {
                        return this.__owners.size();
                    };
                    Entity.prototype.retain = function (owner) {
                        if (!this.__owners.add(owner)) {
                            throw new EntityIsAlreadyRetainedByOwnerException(owner);
                        }
                    };
                    Entity.prototype.release = function (owner) {
                        if (!this.__owners.remove(owner)) {
                            throw new EntityIsNotRetainedByOwnerException(owner);
                        }
                        if (this.__owners.size() === 0) {
                            this.notifyEntityReleased();
                        }
                    };
                    Entity.prototype.destroy = function () {
                        this.removeAllComponents();
                        this._isEnabled = false;
                    };
                    Entity.prototype.createDefaultContextInfo = function () {
                        var componentNames = new Array(this._totalComponents);
                        for (var i = 0; i < componentNames.length; i++) {
                            componentNames[i] = new String(i).toString();
                        }
                        return new ContextInfo("No Context", componentNames, null);
                    };
                    Entity.prototype.recoverComponent = function (index) {
                        var componentContext = this.getComponentPool(index);
                        if (componentContext.size() > 0) {
                            return componentContext.pop();
                        }
                        return null;
                    };
                    Entity.prototype.clearEventsListener = function () {
                        if (this.__OnComponentAdded != null)
                            this.__OnComponentAdded.clear();
                        if (this.__OnComponentRemoved != null)
                            this.__OnComponentRemoved.clear();
                        if (this.__OnComponentReplaced != null)
                            this.__OnComponentReplaced.clear();
                        if (this.__OnEntityReleased != null)
                            this.__OnEntityReleased.clear();
                    };
                    Entity.prototype.removeAllOnEntityReleasedHandlers = function () {
                        this.__OnEntityReleased.clear();
                    };
                    Entity.prototype.OnComponentAdded = function (listener) {
                        if (this.__OnComponentAdded != null) {
                            this.__OnComponentAdded = Collections.createSet("com.ilargia.games.entitas.api.events.EntityComponentChanged");
                        }
                        this.__OnComponentAdded.add(listener);
                    };
                    Entity.prototype.OnComponentRemoved = function (listener) {
                        if (this.__OnComponentRemoved != null) {
                            this.__OnComponentRemoved = Collections.createSet("com.ilargia.games.entitas.api.events.EntityComponentChanged");
                        }
                        this.__OnComponentRemoved.add(listener);
                    };
                    Entity.prototype.OnComponentReplaced = function (listener) {
                        if (this.__OnComponentReplaced != null) {
                            this.__OnComponentReplaced = Collections.createSet("com.ilargia.games.entitas.api.events.EntityComponentReplaced");
                        }
                        this.__OnComponentReplaced.add(listener);
                    };
                    Entity.prototype.OnEntityReleased = function (listener) {
                        if (this.__OnEntityReleased != null) {
                            this.__OnEntityReleased = Collections.createSet("com.ilargia.games.entitas.api.events.EntityReleased");
                        }
                        this.__OnEntityReleased.add(listener);
                    };
                    Entity.prototype.notifyComponentAdded = function (index, component) {
                        if (this.__OnComponentAdded != null) {
                            for (var index140 = this.__OnComponentAdded.iterator(); index140.hasNext();) {
                                var listener = index140.next();
                                {
                                    listener(this, index, component);
                                }
                            }
                        }
                    };
                    Entity.prototype.notifyComponentRemoved = function (index, component) {
                        if (this.__OnComponentRemoved != null) {
                            for (var index141 = this.__OnComponentRemoved.iterator(); index141.hasNext();) {
                                var listener = index141.next();
                                {
                                    listener(this, index, component);
                                }
                            }
                        }
                    };
                    Entity.prototype.notifyComponentReplaced = function (index, previousComponent, newComponent) {
                        if (this.__OnComponentReplaced != null) {
                            for (var index142 = this.__OnComponentReplaced.iterator(); index142.hasNext();) {
                                var listener = index142.next();
                                {
                                    listener(this, index, previousComponent, newComponent);
                                }
                            }
                        }
                    };
                    Entity.prototype.notifyEntityReleased = function () {
                        if (this.__OnEntityReleased != null) {
                            for (var index143 = this.__OnEntityReleased.iterator(); index143.hasNext();) {
                                var listener = index143.next();
                                {
                                    listener(this);
                                }
                            }
                        }
                    };
                    Entity.prototype.equals = function (o) {
                        if (this === o)
                            return true;
                        if (o == null || this.constructor !== o.constructor)
                            return false;
                        var entity = o;
                        if (this._creationIndex !== entity._creationIndex)
                            return false;
                        if (this._totalComponents !== entity._totalComponents)
                            return false;
                        return this._contextInfo != null ? this._contextInfo.equals(entity._contextInfo) : entity._contextInfo == null;
                    };
                    Entity.prototype.hashCode = function () {
                        return this._creationIndex;
                    };
                    Entity.prototype.toString = function () {
                        return "Entity{_creationIndex=" + this._creationIndex + ", _isEnabled=" + this._isEnabled + ", _contextInfo=" + this._contextInfo + '}';
                    };
                    return Entity;
                }());
                entitas.Entity = Entity;
                Entity["__class"] = "com.ilargia.games.entitas.Entity";
                Entity["__interfaces"] = ["com.ilargia.games.entitas.api.IEntity"];
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
