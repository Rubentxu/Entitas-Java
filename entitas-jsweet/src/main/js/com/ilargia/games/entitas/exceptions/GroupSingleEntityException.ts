/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import EntitasException = com.ilargia.games.entitas.api.EntitasException;

    import Group = com.ilargia.games.entitas.group.Group;

    export class GroupSingleEntityException extends EntitasException {
        public constructor(group : Group<any>) {
            super("Cannot get the single entity from " + group + "!\nGroup contains " + group.getCount() + " entities:", "");
        }
    }
    GroupSingleEntityException["__class"] = "com.ilargia.games.entitas.exceptions.GroupSingleEntityException";
    GroupSingleEntityException["__interfaces"] = ["java.io.Serializable"];


}

