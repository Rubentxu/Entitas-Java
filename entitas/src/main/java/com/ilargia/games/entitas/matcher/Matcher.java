package com.ilargia.games.entitas.matcher;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.events.GroupEventType;
import com.ilargia.games.entitas.exceptions.MatcherException;
import com.ilargia.games.entitas.interfaces.IAllOfMatcher;
import com.ilargia.games.entitas.interfaces.IAnyOfMatcher;
import com.ilargia.games.entitas.interfaces.IMatcher;
import com.ilargia.games.entitas.interfaces.INoneOfMatcher;

import java.util.Arrays;


public class Matcher implements IAllOfMatcher, IAnyOfMatcher, INoneOfMatcher {

    public String[] componentNames;
    private Integer[] _indices;
    private Integer[] _allOfIndices;
    private Integer[] _anyOfIndices;
    private Integer[] _noneOfIndices;
    private int _hash;
    private boolean _isHashCached;
    private String _toStringCache;

    private Matcher() {
    }

    private static String[] getComponentNames(IMatcher[] matchers) {
        for (int i = 0; i < matchers.length; i++) {
            Matcher matcher = (Matcher) ((matchers[i] instanceof Matcher) ? matchers[i] : null);
            if (matcher != null && matcher.componentNames != null) {
                return matcher.componentNames;
            }
        }

        return null;
    }

    private static void setComponentNames(Matcher matcher, IMatcher[] matchers) {
        String[] componentNames = getComponentNames(matchers);
        if (componentNames != null) {
            matcher.componentNames = componentNames;
        }
    }

    private static Integer[] distinctIndices(Integer... indices) {
        ObjectSet<Integer> indicesSet = EntitasCache.getIntHashSet();

        indicesSet.addAll(indices);
        Array<Integer> uniqueIndices = new Array(true, indicesSet.size, Integer.class);
        indicesSet.iterator().toArray(uniqueIndices);
        Arrays.sort(uniqueIndices.items);

        EntitasCache.pushIntHashSet(indicesSet);
        return uniqueIndices.items;

    }

    private static boolean equalIndices(Integer[] i1, Integer[] i2) {
        if ((i1 == null) != (i2 == null)) {
            return false;
        }
        if (i1 == null) {
            return true;
        }
        if (i1.length != i2.length) {
            return false;
        }

        for (int i = 0, indicesLength = i1.length; i < indicesLength; i++) {
            if (i1[i] != i2[i]) {
                return false;
            }
        }
        return true;

    }

    private static int applyHash(int hash, Integer[] indices, int i1, int i2) {
        if (indices != null) {
            for (int i = 0, indicesLength = indices.length; i < indicesLength; i++) {
                hash ^= indices[i] * i1;
            }
            hash ^= indices.length * i2;
        }
        return hash;
    }

    public static IAllOfMatcher AllOf(Integer... indices) {
        Matcher matcher = new Matcher();
        matcher._allOfIndices = distinctIndices(indices);
        return matcher;
    }

    public static IAllOfMatcher AllOf(IMatcher... matchers) {
        Matcher allOfMatcher = (Matcher) AllOf(MergeIndices(matchers));
        setComponentNames(allOfMatcher, matchers);
        return allOfMatcher;
    }

    public static IAnyOfMatcher AnyOf(Integer... indices) {
        Matcher matcher = new Matcher();
        matcher._anyOfIndices = distinctIndices(indices);
        return matcher;
    }

    public static IAnyOfMatcher AnyOf(IMatcher... matchers) {
        Matcher anyOfMatcher = (Matcher) Matcher.AnyOf(MergeIndices(matchers));
        setComponentNames(anyOfMatcher, matchers);
        return anyOfMatcher;

    }

    private static void appendIndices(StringBuilder sb, String prefix, Integer[] indexArray, String[] componentNames) {
        final String SEPARATOR = ", ";
        sb.append(prefix);
        sb.append("(");
        int lastSeparator = indexArray.length - 1;
        for (int i = 0, indicesLength = indexArray.length; i < indicesLength; i++) {
            int index = indexArray[i];
            if (componentNames == null) {
                sb.append(index);
            } else {
                sb.append(componentNames[index]);
            }

            if (i < lastSeparator) {
                sb.append(SEPARATOR);
            }
        }
        sb.append(")");
    }

    static Integer[] mergeIndices(IMatcher... matchers) {
        Integer[] indices = new Integer[matchers.length];
        for (int i = 0; i < matchers.length; i++) {
            IMatcher matcher = matchers[i];
            if (matcher.getIndices().length != 1) {
                throw new MatcherException(matcher);
            }
            indices[i] = matcher.getIndices()[0];
        }

        return indices;
    }

    private static Integer[] MergeIndices(IMatcher... matchers) {
        Integer[] indices = new Integer[matchers.length];
        for (int i = 0; i < matchers.length; i++) {
            IMatcher matcher = matchers[i];
            if (matcher.getIndices().length != 1) {
                throw new MatcherException(matcher);
            }
            indices[i] = matcher.getIndices()[0];
        }
        return indices;
    }

    public Integer[] getAllOfIndices() {
        return _allOfIndices;
    }

    public Integer[] getAnyOfIndices() {
        return _anyOfIndices;
    }

    public Integer[] getIndices() {
        if (_indices == null) {
            _indices = mergeIndices();
        }
        return _indices;
    }

    public Integer[] getNoneOfIndices() {
        return _noneOfIndices;
    }

    public IAnyOfMatcher anyOf(Integer... indices) {
        _anyOfIndices = distinctIndices(indices);
        _indices = null;
        return this;
    }

    public IAnyOfMatcher anyOf(IMatcher... matchers) {
        return ((IAllOfMatcher) this).anyOf(MergeIndices(matchers));
    }

    public INoneOfMatcher noneOf(Integer... indices) {
        _noneOfIndices = distinctIndices(indices);
        _indices = null;
        return this;
    }

    public INoneOfMatcher noneOf(IMatcher... matchers) {
        return noneOf(MergeIndices(matchers));
    }

    public boolean matches(Entity entity) {
        boolean matchesAllOf = _allOfIndices == null || entity.hasComponents(_allOfIndices);
        boolean matchesAnyOf = _anyOfIndices == null || entity.hasAnyComponent(_anyOfIndices);
        boolean matchesNoneOf = _noneOfIndices == null || !entity.hasAnyComponent(_noneOfIndices);
        return matchesAllOf && matchesAnyOf && matchesNoneOf;
    }

    private Integer[] mergeIndices() {
        Array<Integer> indicesList = EntitasCache.getIntArray();

        if (_allOfIndices != null) {
            for (int it : _allOfIndices) {
                indicesList.add(it);
            }
        }
        if (_anyOfIndices != null) {
            for (int it : _anyOfIndices) {
                indicesList.add(it);
            }
        }
        if (_noneOfIndices != null) {
            for (int it : _noneOfIndices) {
                indicesList.add(it);
            }

        }

        Integer[] mergeIndices = distinctIndices(indicesList.toArray());

        EntitasCache.pushIntArray(indicesList);
        return mergeIndices;

    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj.getClass() != this.getClass() || obj.hashCode() != hashCode()) {
            return false;
        }

        Matcher matcher = (Matcher) obj;
        if (!equalIndices(matcher.getAllOfIndices(), _allOfIndices)) {
            return false;
        }
        if (!equalIndices(matcher.getAnyOfIndices(), _anyOfIndices)) {
            return false;
        }
        if (!equalIndices(matcher.getNoneOfIndices(), _noneOfIndices)) {
            return false;
        }
        return true;

    }

    public TriggerOnEvent OnEntityAdded() {
        return new TriggerOnEvent(this, GroupEventType.OnEntityAdded);
    }

    /// Convenience method to create a new TriggerOnEvent.
    /// Commonly used in IReactiveSystem and IMultiReactiveSystem.
    public TriggerOnEvent OnEntityRemoved() {
        return new TriggerOnEvent(this, GroupEventType.OnEntityRemoved);
    }

    /// Convenience method to create a new TriggerOnEvent.
    /// Commonly used in IReactiveSystem and IMultiReactiveSystem.
    public TriggerOnEvent OnEntityAddedOrRemoved() {
        return new TriggerOnEvent(this, GroupEventType.OnEntityAddedOrRemoved);
    }

    @Override
    public int hashCode() {
        if (!_isHashCached) {
            int hash = this.getClass().hashCode();
            hash = applyHash(hash, _allOfIndices, 3, 53);
            hash = applyHash(hash, _anyOfIndices, 307, 367);
            hash = applyHash(hash, _noneOfIndices, 647, 683);
            _hash = hash;
            _isHashCached = true;
        }
        return _hash;
    }

    @Override
    public String toString() {
        if (_toStringCache == null) {
            StringBuilder sb = new StringBuilder();
            if (_allOfIndices != null) {
                appendIndices(sb, "AllOf", _allOfIndices, componentNames);
            }
            if (_anyOfIndices != null) {
                if (_allOfIndices != null) {
                    sb.append(".");
                }
                appendIndices(sb, "AnyOf", _anyOfIndices, componentNames);
            }
            if (_noneOfIndices != null) {
                appendIndices(sb, ".NoneOf", _noneOfIndices, componentNames);
            }
            _toStringCache = sb.toString();
        }

        return _toStringCache;
    }

}