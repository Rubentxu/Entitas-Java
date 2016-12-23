package com.ilargia.games.entitas.caching;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.interfaces.events.GroupChanged;
import com.ilargia.games.entitas.interfaces.IComponent;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;

import java.util.ArrayList;

public class EntitasCache {

    private static ObjectPool<ArrayList<IComponent>> componentArray = new ObjectPool<ArrayList<IComponent>>(() -> {
        return new ArrayList<IComponent>();
    }, null);
    private static ObjectPool<IntArrayList> integerArray = new ObjectPool<IntArrayList>(() -> {
        return new IntArrayList(20);
    }, null);
    private static ObjectPool<IntArraySet> integerSet = new ObjectPool<IntArraySet>(() -> {
        return new IntArraySet();
    }, null);
    private static ObjectPool<Array<GroupChanged>> groupChangedArray = new ObjectPool<Array<GroupChanged>>(() -> {
        return new Array<GroupChanged>(true, 16, GroupChanged.class);
    }, null);


    public static ArrayList<IComponent> getIComponentList() {
        return componentArray.get();
    }

    public static void pushIComponentList(ArrayList<IComponent> list) {
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
