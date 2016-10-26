package com.ilargia.games.entitas.caching;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.interfaces.GroupChanged;
import com.ilargia.games.entitas.interfaces.IComponent;

public class EntitasCache {

    static ObjectPool<Array<IComponent>> componentArray;
    static ObjectPool<Array<Integer>> integerArray;
    static ObjectPool<ObjectSet<Integer>> integerSet;
    static ObjectPool<Array<GroupChanged>> groupChangedArray;


    public EntitasCache() {
        componentArray = new ObjectPool<>();
        integerArray = new ObjectPool<>();
        integerSet = new ObjectPool<>();
        groupChangedArray = new ObjectPool<>();
    }


    public static Array<IComponent> getIComponentList() {
        return componentArray.get();
    }

    public static void pushIComponentList(Array<IComponent> list) {
        list.clear();
        componentArray.push(list);
    }

    public static Array<Integer> getIntArray() {
        return integerArray.get();
    }

    public static void pushIntArray(Array<Integer> list) {
        list.clear();
        integerArray.push(list);
    }

    public static ObjectSet<Integer> getIntHashSet() {
        return integerSet.get();
    }

    public static void pushIntHashSet(ObjectSet<Integer> hashSet) {
        hashSet.clear();
        integerSet.push(hashSet);
    }

    public static Array<GroupChanged> getGroupChangedList() {
        return groupChangedArray.get();

    }

    public static void pushGroupChangedList(Array<GroupChanged> list) {
        list.clear();
        groupChangedArray.push(list);
    }

    public static void reset() {
        componentArray.reset();
        integerArray.reset();
        integerSet.reset();
        groupChangedArray.reset();
    }
}
