/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api {
    import Set = java.util.Set;

    import Stack = java.util.Stack;

    export interface IEntity {
        getTotalComponents() : number;

        getCreationIndex() : number;

        isEnabled() : boolean;

        componentPools() : Stack<IComponent>[];

        contextInfo() : ContextInfo;

        initialize(creationIndex : number, totalComponents : number, componentPools : Stack<IComponent>[], contextInfo : ContextInfo);

        reactivate(creationIndex : number);

        addComponent(index : number, component : IComponent);

        removeComponent(index : number);

        replaceComponent(index : number, component : IComponent);

        getComponent(index : number) : IComponent;

        getComponents() : IComponent[];

        getComponentIndices() : number[];

        hasComponent(index : number) : boolean;

        hasComponents(indices : number[]) : boolean;

        hasAnyComponent(indices : number[]) : boolean;

        removeAllComponents();

        getComponentPool(index : number) : Stack<IComponent>;

        createComponent(index? : any, type? : any) : any;

        owners() : Set<any>;

        retainCount() : number;

        retain(owner : any);

        release(owner : any);

        destroy();
    }
}

