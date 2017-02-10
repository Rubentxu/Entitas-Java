/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.caching {
    import IComponent = com.ilargia.games.entitas.api.IComponent;

    import GroupChanged = com.ilargia.games.entitas.api.events.GroupChanged;

    import Collections = com.ilargia.games.entitas.factories.Collections;

    import List = java.util.List;

    import Set = java.util.Set;

    export class EntitasCache {
        static componentArray : ObjectPool<List<IComponent>>; public static componentArray_$LI$() : ObjectPool<List<IComponent>> { if(EntitasCache.componentArray == null) EntitasCache.componentArray = new ObjectPool<List<IComponent>>(() => {
            return Collections.createList("com.ilargia.games.entitas.api.IComponent");
        }, null); return EntitasCache.componentArray; };

        static integerArray : ObjectPool<List<any>>; public static integerArray_$LI$() : ObjectPool<List<any>> { if(EntitasCache.integerArray == null) EntitasCache.integerArray = new ObjectPool<List<any>>(() => {
            return Collections.createList(Number);
        }, null); return EntitasCache.integerArray; };

        static integerSet : ObjectPool<Set<any>>; public static integerSet_$LI$() : ObjectPool<Set<any>> { if(EntitasCache.integerSet == null) EntitasCache.integerSet = new ObjectPool<Set<any>>(() => {
            return Collections.createSet<any>(Number);
        }, null); return EntitasCache.integerSet; };

        static groupChangedArray : ObjectPool<List<Set<GroupChanged<any>>>>; public static groupChangedArray_$LI$() : ObjectPool<List<Set<GroupChanged<any>>>> { if(EntitasCache.groupChangedArray == null) EntitasCache.groupChangedArray = new ObjectPool<List<Set<GroupChanged<any>>>>(() => {
            return Collections.createList("com.ilargia.games.entitas.api.events.GroupChanged");
        }, null); return EntitasCache.groupChangedArray; };

        public static getIComponentList() : List<IComponent> {
            return EntitasCache.componentArray_$LI$().get();
        }

        public static pushIComponentList(list : List<IComponent>) {
            list.clear();
            EntitasCache.componentArray_$LI$().push(list);
        }

        public static getIntArray() : List<number> {
            return EntitasCache.integerArray_$LI$().get();
        }

        public static pushIntArray(list : List<any>) {
            list.clear();
            EntitasCache.integerArray_$LI$().push(list);
        }

        public static getIntHashSet() : Set<number> {
            return EntitasCache.integerSet_$LI$().get();
        }

        public static pushIntHashSet(hashSet : Set<any>) {
            hashSet.clear();
            EntitasCache.integerSet_$LI$().push(hashSet);
        }

        public static getGroupChangedList() : List<Set<GroupChanged<any>>> {
            return EntitasCache.groupChangedArray_$LI$().get();
        }

        public static pushGroupChangedList(list : List<Set<GroupChanged<any>>>) {
            list.clear();
            EntitasCache.groupChangedArray_$LI$().push(list);
        }

        public static reset() {
            EntitasCache.componentArray_$LI$().reset();
            EntitasCache.integerArray_$LI$().reset();
            EntitasCache.integerSet_$LI$().reset();
            EntitasCache.groupChangedArray_$LI$().reset();
        }
    }
    EntitasCache["__class"] = "com.ilargia.games.entitas.caching.EntitasCache";

}


com.ilargia.games.entitas.caching.EntitasCache.groupChangedArray_$LI$();

com.ilargia.games.entitas.caching.EntitasCache.integerSet_$LI$();

com.ilargia.games.entitas.caching.EntitasCache.integerArray_$LI$();

com.ilargia.games.entitas.caching.EntitasCache.componentArray_$LI$();
