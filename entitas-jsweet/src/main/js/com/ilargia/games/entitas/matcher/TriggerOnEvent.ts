/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.matcher {
    import IMatcher = com.ilargia.games.entitas.api.matcher.IMatcher;

    import GroupEvent = com.ilargia.games.entitas.events.GroupEvent;

    export class TriggerOnEvent {
        public trigger : IMatcher<any>;

        public eventType : GroupEvent;

        public constructor(trigger? : any, eventType? : any) {
            if(((trigger != null && (trigger["__interfaces"] != null && trigger["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0 || trigger.constructor != null && trigger.constructor["__interfaces"] != null && trigger.constructor["__interfaces"].indexOf("com.ilargia.games.entitas.api.matcher.IMatcher") >= 0)) || trigger === null) && ((typeof eventType === 'number') || eventType === null)) {
                let __args = Array.prototype.slice.call(arguments);
                (() => {
                    this.trigger = trigger;
                    this.eventType = eventType;
                })();
            } else if(trigger === undefined && eventType === undefined) {
                let __args = Array.prototype.slice.call(arguments);
            } else throw new Error('invalid overload');
        }

        public clone() : TriggerOnEvent {
            let varCopy : TriggerOnEvent = new TriggerOnEvent();
            varCopy.trigger = this.trigger;
            varCopy.eventType = this.eventType;
            return varCopy;
        }
    }
    TriggerOnEvent["__class"] = "com.ilargia.games.entitas.matcher.TriggerOnEvent";

}

