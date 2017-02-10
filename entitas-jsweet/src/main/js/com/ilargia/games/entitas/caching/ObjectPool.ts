/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.caching {
    import Stack = java.util.Stack;

    export class ObjectPool<T> {
        private _factoryMethod : ObjectPool.Factory<T>;

        private _resetMethod : (p1: T) => void;

        private _pool : Stack<T>;

        public constructor(factoryMethod? : any, resetMethod? : any) {
            if(((factoryMethod != null && (factoryMethod["__interfaces"] != null && factoryMethod["__interfaces"].indexOf("com.ilargia.games.entitas.caching.ObjectPool.Factory") >= 0 || factoryMethod.constructor != null && factoryMethod.constructor["__interfaces"] != null && factoryMethod.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.caching.ObjectPool.Factory") >= 0)) || factoryMethod === null) && ((typeof resetMethod === 'function' && (<any>resetMethod).length == 1) || resetMethod === null)) {
                let __args = Array.prototype.slice.call(arguments);
                (() => {
                    this._factoryMethod = factoryMethod;
                    this._resetMethod = resetMethod;
                    this._pool = <any>(new Stack<T>());
                })();
            } else if(factoryMethod === undefined && resetMethod === undefined) {
                let __args = Array.prototype.slice.call(arguments);
            } else throw new Error('invalid overload');
        }

        public get() : T {
            return this._pool.size() === 0?this._factoryMethod.create():this._pool.pop();
        }

        public push(obj : T) {
            if(this._resetMethod != null) {
                this._resetMethod(obj);
            }
            this._pool.push(obj);
        }

        public reset() {
            this._pool.clear();
        }
    }
    ObjectPool["__class"] = "com.ilargia.games.entitas.caching.ObjectPool";


    export namespace ObjectPool {

        export interface Factory<T> {
            create() : T;
        }
    }

}

