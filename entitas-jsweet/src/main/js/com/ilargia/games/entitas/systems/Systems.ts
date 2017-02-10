/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.systems {
    import ICleanupSystem = com.ilargia.games.entitas.api.system.ICleanupSystem;

    import IExecuteSystem = com.ilargia.games.entitas.api.system.IExecuteSystem;

    import IInitializeSystem = com.ilargia.games.entitas.api.system.IInitializeSystem;

    import IRenderSystem = com.ilargia.games.entitas.api.system.IRenderSystem;

    import ISystem = com.ilargia.games.entitas.api.system.ISystem;

    import ITearDownSystem = com.ilargia.games.entitas.api.system.ITearDownSystem;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import List = java.util.List;

    export class Systems implements IInitializeSystem, IExecuteSystem, IRenderSystem, ICleanupSystem, ITearDownSystem {
        _initializeSystems : List<IInitializeSystem>;

        _executeSystems : List<IExecuteSystem>;

        _renderSystems : List<IRenderSystem>;

        _cleanupSystems : List<ICleanupSystem>;

        _tearDownSystems : List<ITearDownSystem>;

        public constructor() {
            this._initializeSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
            this._executeSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
            this._renderSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
            this._cleanupSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
            this._tearDownSystems = Collections.createList("com.ilargia.games.entitas.api.system.ISystem");
        }

        public add(system : ISystem) : Systems {
            if(system != null) {
                if(system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IInitializeSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IInitializeSystem") >= 0)) this._initializeSystems.add(<IInitializeSystem>system);
                if(system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IExecuteSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IExecuteSystem") >= 0)) this._executeSystems.add(<IExecuteSystem>system);
                if(system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IRenderSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.IRenderSystem") >= 0)) this._renderSystems.add(<IRenderSystem>system);
                if(system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ICleanupSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ICleanupSystem") >= 0)) this._cleanupSystems.add(<ICleanupSystem>system);
                if(system != null && (system["__interfaces"] != null && system["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ITearDownSystem") >= 0 || system.constructor != null && system.constructor["__interfaces"] != null && system.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.system.ITearDownSystem") >= 0)) this._tearDownSystems.add(<ITearDownSystem>system);
            }
            return this;
        }

        public initialize() {
            for(let index158=this._initializeSystems.iterator();index158.hasNext();) {
                let iSystem = index158.next();
                {
                    iSystem.initialize();
                }
            }
        }

        public execute(entities? : any) : any {
            if(((typeof entities === 'number') || entities === null)) {
                return <any>this.execute$float(entities);
            } else throw new Error('invalid overload');
        }

        public execute$float(deltaTime : number) {
            for(let index159=this._executeSystems.iterator();index159.hasNext();) {
                let eSystem = index159.next();
                {
                    eSystem.execute(deltaTime);
                }
            }
        }

        public render() {
            for(let index160=this._renderSystems.iterator();index160.hasNext();) {
                let eSystem = index160.next();
                {
                    eSystem.render();
                }
            }
        }

        public cleanup() {
            for(let index161=this._cleanupSystems.iterator();index161.hasNext();) {
                let clSystem = index161.next();
                {
                    clSystem.cleanup();
                }
            }
        }

        public tearDown() {
            for(let index162=this._tearDownSystems.iterator();index162.hasNext();) {
                let tSystem = index162.next();
                {
                    tSystem.tearDown();
                }
            }
        }

        public activateReactiveSystems() {
            for(let i : number = 0; i < this._executeSystems.size(); i++) {
                let reactiveSystem : ReactiveSystem<any> = <ReactiveSystem<any>>((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.ReactiveSystem)?this._executeSystems.get(i):null);
                if(reactiveSystem != null) {
                    reactiveSystem.activate();
                }
                let nestedSystems : Systems = <Systems>((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.Systems)?this._executeSystems.get(i):null);
                if(nestedSystems != null) {
                    nestedSystems.activateReactiveSystems();
                }
            }
        }

        public deactivateReactiveSystems() {
            for(let i : number = 0; i < this._executeSystems.size(); i++) {
                let reactiveSystem : ReactiveSystem<any> = <ReactiveSystem<any>>((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.ReactiveSystem)?this._executeSystems.get(i):null);
                if(reactiveSystem != null) {
                    reactiveSystem.deactivate();
                }
                let nestedSystems : Systems = <Systems>((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.Systems)?this._executeSystems.get(i):null);
                if(nestedSystems != null) {
                    nestedSystems.deactivateReactiveSystems();
                }
            }
        }

        public clearReactiveSystems() {
            for(let i : number = 0; i < this._executeSystems.size(); i++) {
                let reactiveSystem : ReactiveSystem<any> = <ReactiveSystem<any>>((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.ReactiveSystem)?this._executeSystems.get(i):null);
                if(reactiveSystem != null) {
                    reactiveSystem.clear();
                }
                let nestedSystems : Systems = <Systems>((this._executeSystems.get(i) != null && this._executeSystems.get(i) instanceof com.ilargia.games.entitas.systems.Systems)?this._executeSystems.get(i):null);
                if(nestedSystems != null) {
                    nestedSystems.clearReactiveSystems();
                }
            }
        }

        public clearSystems() {
            this._initializeSystems.clear();
            this._executeSystems.clear();
            this._renderSystems.clear();
            this._cleanupSystems.clear();
            this._tearDownSystems.clear();
        }

        public toString() : string {
            return "Systems{_initializeSystems=" + this._initializeSystems + ", _executeSystems=" + this._executeSystems + ", _renderSystems=" + this._renderSystems + ", _cleanupSystems=" + this._cleanupSystems + ", _tearDownSystems=" + this._tearDownSystems + '}';
        }
    }
    Systems["__class"] = "com.ilargia.games.entitas.systems.Systems";
    Systems["__interfaces"] = ["com.ilargia.games.entitas.api.system.ICleanupSystem","com.ilargia.games.entitas.api.system.ISystem","com.ilargia.games.entitas.api.system.IRenderSystem","com.ilargia.games.entitas.api.system.IInitializeSystem","com.ilargia.games.entitas.api.system.IExecuteSystem","com.ilargia.games.entitas.api.system.ITearDownSystem"];


}

