/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.systems {
    import Entity = com.ilargia.games.entitas.Entity;

    import IContext = com.ilargia.games.entitas.api.IContext;

    import IReactiveSystem = com.ilargia.games.entitas.api.system.IReactiveSystem;

    import Collector = com.ilargia.games.entitas.collector.Collector;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import List = java.util.List;

    export abstract class ReactiveSystem<TEntity extends Entity> implements IReactiveSystem {
        _collector : Collector<TEntity>;

        _buffer : List<TEntity>;

        _toStringCache : string;

        public constructor(context? : any) {
            if(((context != null && (context["__interfaces"] != null && context["__interfaces"].indexOf("com.ilargia.games.entitas.api.IContext") >= 0 || context.constructor != null && context.constructor["__interfaces"] != null && context.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.IContext") >= 0)) || context === null)) {
                let __args = Array.prototype.slice.call(arguments);
                (() => {
                    this._collector = this.getTrigger(context);
                    this._buffer = Collections.createList(Entity);
                })();
            } else if(((context != null && context instanceof com.ilargia.games.entitas.collector.Collector) || context === null)) {
                let __args = Array.prototype.slice.call(arguments);
                let collector : any = __args[0];
                (() => {
                    this._collector = collector;
                    this._buffer = Collections.createList(Entity);
                })();
            } else throw new Error('invalid overload');
        }

        abstract getTrigger(context : IContext<TEntity>) : Collector<TEntity>;

        abstract filter(entity : TEntity) : boolean;

        public execute(entities? : any) : any {
            if(((entities != null && (entities["__interfaces"] != null && entities["__interfaces"].indexOf("java.util.List") >= 0 || entities.constructor != null && entities.constructor["__interfaces"] != null && entities.constructor["__interfaces"].indexOf("java.util.List") >= 0)) || entities === null)) {
                let __args = Array.prototype.slice.call(arguments);
            } else if(((typeof entities === 'number') || entities === null)) {
                return <any>this.execute$float(entities);
            } else throw new Error('invalid overload');
        }

        public activate() {
            this._collector.activate();
        }

        public deactivate() {
            this._collector.deactivate();
        }

        public clear() {
            this._collector.clearCollectedEntities();
        }

        public execute$float(deltatime : number) {
            if(this._collector._collectedEntities.size() !== 0) {
                for(let index157=this._collector._collectedEntities.iterator();index157.hasNext();) {
                    let e = index157.next();
                    {
                        if(this.filter(e)) {
                            e.retain(this);
                            this._buffer.add(e);
                        }
                    }
                }
                this._collector.clearCollectedEntities();
                if(this._buffer.size() !== 0) {
                    this.execute(this._buffer);
                    for(let i : number = 0; i < this._buffer.size(); i++) {
                        this._buffer.get(i).release(this);
                    }
                    this._buffer.clear();
                }
            }
        }

        public toString() : string {
            return "ReactiveSystem{_collector=" + this._collector + ", _buffer=" + this._buffer + ", _toStringCache=\'" + this._toStringCache + '\'' + '}';
        }
    }
    ReactiveSystem["__class"] = "com.ilargia.games.entitas.systems.ReactiveSystem";
    ReactiveSystem["__interfaces"] = ["com.ilargia.games.entitas.api.system.IReactiveSystem","com.ilargia.games.entitas.api.system.ISystem","com.ilargia.games.entitas.api.system.IExecuteSystem"];


}

