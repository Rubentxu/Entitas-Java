package com.ilargia.games.entitas.caching;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.interfaces.GroupChanged;
import com.ilargia.games.entitas.interfaces.IComponent;

import java.util.ArrayList;

public class EntitasCache {

    private static ObjectPool<ArrayList<IComponent>> componentArray = new ObjectPool<ArrayList<IComponent>>(() -> {
        return new ArrayList<IComponent>();
    }, null);
    private static ObjectPool<Array<Integer>> integerArray = new ObjectPool<Array<Integer>>(() -> {
        return new Array<Integer>(true, 16, Integer.class);
    }, null);
    private static ObjectPool<ObjectSet<Integer>> integerSet = new ObjectPool<ObjectSet<Integer>>(() -> {
        return new ObjectSet<Integer>();
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
