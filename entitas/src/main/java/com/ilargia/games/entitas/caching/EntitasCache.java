package com.ilargia.games.entitas.caching;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.events.GroupChanged;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.List;
import java.util.Set;

public class EntitasCache {

    private static ObjectPool<List<IComponent>> componentArray = new ObjectPool<List<IComponent>>(() -> {
        return EntitasCollections.createList(IComponent.class);
    }, null);
    private static ObjectPool<List<Integer>> integerArray = new ObjectPool<>(() -> {
        return EntitasCollections.createList(Integer.class);
    }, null);
    private static ObjectPool<Set<Integer>> integerSet = new ObjectPool<>(() -> {
        return EntitasCollections.createSet(Integer.class);
    }, null);

    private static ObjectPool<List<Set<GroupChanged>>> groupChangedArray = new ObjectPool<List<Set<GroupChanged>>>(() -> {
        return EntitasCollections.<Set<GroupChanged>>createList(null);
    }, null);




    public static List<IComponent> getIComponentList() {
        return componentArray.get();
    }

    public static void pushIComponentList(List<IComponent> list) {
        list.clear();
        componentArray.push(list);
    }

    public static List<Integer> getIntArray() {
        return integerArray.get();
    }

    public static void pushIntArray(List list) {
        list.clear();
        integerArray.push(list);
    }

    public static Set<Integer> getIntHashSet() {
        return integerSet.get();
    }

    public static void pushIntHashSet(Set hashSet) {
        hashSet.clear();
        integerSet.push(hashSet);
    }

    public static List<Set<GroupChanged>> getGroupChangedList() {
        return groupChangedArray.get();

    }

    public static void pushGroupChangedList(List<Set<GroupChanged>> list) {
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
