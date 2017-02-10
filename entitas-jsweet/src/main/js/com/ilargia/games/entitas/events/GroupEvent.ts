/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.events {
    export enum GroupEvent {
        Added, Removed, AddedOrRemoved
    }

    export class GroupEvent_$WRAPPER {
        public static forValue(value) : GroupEvent {
            return GroupEvent_$WRAPPER.values()[value];
        }

        public getValue() : number {
            return this.ordinal();
        }

        constructor() {
        }
        public name() : string { return this._$name; }
        public ordinal() : number { return this._$ordinal; }
    }
    GroupEvent["__class"] = "com.ilargia.games.entitas.events.GroupEvent";
    GroupEvent["__interfaces"] = ["java.lang.Comparable","java.io.Serializable"];

    GroupEvent["_$wrappers"] = [new GroupEvent_$WRAPPER(0, "Added"), new GroupEvent_$WRAPPER(1, "Removed"), new GroupEvent_$WRAPPER(2, "AddedOrRemoved")];

}

