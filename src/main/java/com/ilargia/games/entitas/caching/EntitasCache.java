package com.ilargia.games.entitas.caching;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.GroupChanged;
import com.ilargia.games.entitas.interfaces.IComponent;

public class EntitasCache {

    static ObjectCache _cache = new ObjectCache();

    public static Array<IComponent> getIComponentList() { return _cache.<Array<IComponent>>get(); }
    public static void pushIComponentList(Array<IComponent> list) { list.clear(); _cache.push(list); }

    public static Array<Integer> getIntList() { return _cache.<Array<Integer>>get(); }
    public static void pushIntList(Array<Integer> list) { list.clear(); _cache.push(list); }

    public static ObjectSet<Integer> getIntHashSet() { return _cache.<ObjectSet<Integer>>get(); }
    public static void pushIntHashSet(ObjectSet<Integer> hashSet) { hashSet.clear(); _cache.push(hashSet); }

    public static Array<GroupChanged> getGroupChangedList() { return _cache.<Array<GroupChanged>>get(); }
    public static void pushGroupChangedList(Array<GroupChanged> list) { list.clear(); _cache.push(list); }

    public static void reset() {
        _cache.reset();
    }
}
