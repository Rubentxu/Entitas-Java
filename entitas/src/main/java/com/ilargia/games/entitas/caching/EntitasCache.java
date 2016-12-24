package com.ilargia.games.entitas.caching;


import com.ilargia.games.entitas.interfaces.IComponent;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class EntitasCache {

    private static ObjectPool<ObjectArrayList<IComponent>> componentArray = new ObjectPool<ObjectArrayList<IComponent>>(() -> {
        return new ObjectArrayList<IComponent>();
    }, null);
    private static ObjectPool<IntArrayList> integerArray = new ObjectPool<IntArrayList>(() -> {
        return new IntArrayList(20);
    }, null);
    private static ObjectPool<IntArraySet> integerSet = new ObjectPool<IntArraySet>(() -> {
        return new IntArraySet();
    }, null);
    private static ObjectPool<ObjectArrayList<GroupChanged>> groupChangedArray = new ObjectPool<ObjectArrayList<GroupChanged>>(() -> {
        return new ObjectArrayList<GroupChanged>(16);
    }, null);


    public static ObjectArrayList<IComponent> getIComponentList() {
        return componentArray.get();
    }

    public static void pushIComponentList(ObjectArrayList<IComponent> list) {
        list.clear();
        componentArray.push(list);
    }

    public static IntArrayList getIntArray() {
        return integerArray.get();
    }

    public static void pushIntArray(IntArrayList list) {
        list.clear();
        integerArray.push(list);
    }

    public static IntArraySet getIntHashSet() {
        return integerSet.get();
    }

    public static void pushIntHashSet(IntArraySet hashSet) {
        hashSet.clear();
        integerSet.push(hashSet);
    }

    public static ObjectArrayList<GroupChanged> getGroupChangedList() {
        return groupChangedArray.get();

    }

    public static void pushGroupChangedList(ObjectArrayList<GroupChanged> list) {
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
