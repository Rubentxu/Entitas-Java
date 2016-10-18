package com.ilargia.games.entitas.matcher;

import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.exceptions.MatcherException;
import com.ilargia.games.entitas.interfaces.IAllOfMatcher;
import com.ilargia.games.entitas.interfaces.IAnyOfMatcher;
import com.ilargia.games.entitas.interfaces.IMatcher;
import com.ilargia.games.entitas.interfaces.INoneOfMatcher;

import java.util.*;


public class Matcher implements IAllOfMatcher, IAnyOfMatcher, INoneOfMatcher {

    public String[] componentNames;
    private int[] _indices;
    private int[] _allOfIndices;
    private int[] _anyOfIndices;
    private int[] _noneOfIndices;
    private int _hash;
    private boolean _isHashCached;
    private String _toStringCache;

    private Matcher() {}

    private static  int[] MergeIndices(IMatcher... matchers) {
        int[] indices = new int[matchers.length];
        for (int i = 0; i < matchers.length; i++) {
            IMatcher matcher = matchers[i];
            if(matcher.getindices().length != 1) {
                throw new MatcherException(matcher);
            }
            indices[i] = matcher.getindices()[0];
        }
        return indices;
    }

    private static String[] getComponentNames(IMatcher[] matchers) {
        for (int i = 0, matchersLength = matchers.length; i < matchersLength; i++) {
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

    private static int[] DistinctIndices(int... indices) {
        int[] indicesSet = new int[indices.length];
        int[] indicesList =  indicesSet;
//        indicesList..sort();
        return indicesList;

    }

    private static boolean equalIndices(int[] i1, int[] i2) {
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

    private static int applyHash(int hash, int[] indices, int i1, int i2) {
        if (indices != null) {
            for (int i = 0, indicesLength = indices.length; i < indicesLength; i++) {
                hash ^= indices[i] * i1;
            }
            hash ^= indices.length * i2;
        }

        return hash;
    }

    public static IAllOfMatcher AllOf(int... indices) {
        Matcher matcher = new Matcher();
        matcher._allOfIndices = DistinctIndices(indices);
        return matcher;
    }

    public static IAllOfMatcher AllOf(IMatcher... matchers) {
        Matcher allOfMatcher = (Matcher) AllOf(MergeIndices(matchers));
        setComponentNames(allOfMatcher, matchers);
        return allOfMatcher;
    }

    public static IAnyOfMatcher AnyOf(int... indices) {
        Matcher matcher = new Matcher();
        matcher._anyOfIndices = DistinctIndices(indices);
        return matcher;
    }

    public static IAnyOfMatcher AnyOf(IMatcher... matchers) {
        Matcher anyOfMatcher = (Matcher) AnyOf(MergeIndices(matchers));
        setComponentNames(anyOfMatcher, matchers);
        return anyOfMatcher;
    }

    private static void appendIndices(StringBuilder sb, String prefix, int[] indexArray, String[] componentNames) {
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

    public int[] getallOfIndices() {
        return _allOfIndices;
    }

    public int[] getanyOfIndices() {
        return _anyOfIndices;
    }

    public int[] getindices() {
        if (_indices == null) {
            _indices = mergeIndices();
        }
        return _indices;
    }

    public int[] getnoneOfIndices() {
        return _noneOfIndices;
    }

    public IAnyOfMatcher anyOf(int... indices) {
        _anyOfIndices = DistinctIndices(indices);
        _indices = null;
        return this;
    }

    public IAnyOfMatcher anyOf(IMatcher... matchers) {
        return ((IAllOfMatcher) this).anyOf(MergeIndices(matchers));
    }

    public INoneOfMatcher noneOf(int... indices) {
        _noneOfIndices = DistinctIndices(indices);
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

    private int[] mergeIndices() {
        List<Integer> indicesList = new ArrayList<>();

        if(_allOfIndices != null) {
            for (int it : _allOfIndices) {
                indicesList.add(it);
            }
        }
        if(_anyOfIndices != null) {
            for (int it : _anyOfIndices) {
                indicesList.add(it);
            }
        }
        if(_noneOfIndices != null) {
            for (int it : _noneOfIndices) {
                indicesList.add(it);
            }

        }
//
        int[] mergedIndices =  DistinctIndices(indicesList.stream().mapToInt(i -> i).toArray());

        EntitasCache.PushIntList(indicesList);

        return mergedIndices;

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass() || obj.hashCode() != hashCode()) {
            return false;
        }

        Matcher matcher = (Matcher) obj;
        if (!equalIndices(matcher.getallOfIndices(), _allOfIndices)) {
            return false;
        }
        if (!equalIndices(matcher.getanyOfIndices(), _anyOfIndices)) {
            return false;
        }
        if (!equalIndices(matcher.getnoneOfIndices(), _noneOfIndices)) {
            return false;
        }
        return true;

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