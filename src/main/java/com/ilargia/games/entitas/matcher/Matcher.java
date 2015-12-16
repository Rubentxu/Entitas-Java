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
    private List<Integer> _indices;
    private List<Integer> _allOfIndices;
    private List<Integer> _anyOfIndices;
    private List<Integer> _noneOfIndices;
    private int _hash;
    private boolean _isHashCached;
    private String _toStringCache;


    private Matcher() {
    }

    private static Integer[] MergeIndices(IMatcher... matchers) {
        Integer[] indices = new Integer[matchers.length];
        for (int i = 0, matchersLength = matchers.length; i < matchersLength; i++) {
            IMatcher matcher = matchers[i];
            if (matcher.getindices().size() != 1) {
                throw new MatcherException(matcher);
            }
            indices[i] = matcher.getindices().get(0);
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

    private static List<Integer> DistinctIndices(List<Integer> indices) {
        Set<Integer> indicesSet = new HashSet<Integer>(indices);
        List<Integer> indicesList = Arrays.asList((Integer[]) indicesSet.toArray());
        Collections.sort(indicesList);
        return indicesList;

    }

    private static boolean equalIndices(List<Integer> i1, List<Integer> i2) {
        if ((i1 == null) != (i2 == null)) {
            return false;
        }
        if (i1 == null) {
            return true;
        }
        if (i1.size() != i2.size()) {
            return false;
        }

        for (int i = 0, indicesLength = i1.size(); i < indicesLength; i++) {
            if (i1.get(i) != i2.get(i)) {
                return false;
            }
        }
        return true;

    }

    private static int applyHash(int hash, List<Integer> indices, int i1, int i2) {
        if (indices != null) {
            for (int i = 0, indicesLength = indices.size(); i < indicesLength; i++) {
                hash ^= indices.get(i) * i1;
            }
            hash ^= indices.size() * i2;
        }

        return hash;
    }

    public static IAllOfMatcher AllOf(Integer... indices) {
        Matcher matcher = new Matcher();
        matcher._allOfIndices = DistinctIndices(Arrays.asList(indices));
        return matcher;
    }

    public static IAllOfMatcher AllOf(IMatcher... matchers) {
        Matcher allOfMatcher = (Matcher) AllOf(MergeIndices(matchers));
        setComponentNames(allOfMatcher, matchers);
        return allOfMatcher;
    }

    public static IAnyOfMatcher AnyOf(Integer... indices) {
        Matcher matcher = new Matcher();
        matcher._anyOfIndices = DistinctIndices(Arrays.asList(indices));
        return matcher;
    }

    public static IAnyOfMatcher AnyOf(IMatcher... matchers) {
        Matcher anyOfMatcher = (Matcher) AnyOf(MergeIndices(matchers));
        setComponentNames(anyOfMatcher, matchers);
        return anyOfMatcher;
    }

    private static void appendIndices(StringBuilder sb, String prefix, List<Integer> indexArray, String[] componentNames) {
        final String sEPARATOR = ", ";
        sb.append(prefix);
        sb.append("(");
        int lastSeparator = indexArray.size() - 1;
        for (int i = 0, indicesLength = indexArray.size(); i < indicesLength; i++) {
            int index = indexArray.get(i);
            if (componentNames == null) {
                sb.append(index);
            } else {
                sb.append(componentNames[index]);
            }

            if (i < lastSeparator) {
                sb.append(sEPARATOR);
            }
        }
        sb.append(")");
    }

    public List<Integer> getallOfIndices() {
        return _allOfIndices;
    }

    public List<Integer> getanyOfIndices() {
        return _anyOfIndices;
    }

    public List<Integer> getindices() {
        if (_indices == null) {
            _indices = mergeIndices();
        }
        return _indices;
    }

    public List<Integer> getnoneOfIndices() {
        return _noneOfIndices;
    }

    public IAnyOfMatcher anyOf(Integer... indices) {
        _anyOfIndices = DistinctIndices(Arrays.asList(indices));
        _indices = null;
        return this;
    }

    public IAnyOfMatcher anyOf(IMatcher... matchers) {
        return ((IAllOfMatcher) this).anyOf(MergeIndices(matchers));
    }

    public INoneOfMatcher noneOf(Integer... indices) {
        _noneOfIndices = DistinctIndices(Arrays.asList(indices));
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

    private List<Integer> mergeIndices() {
        int totalIndices = (_allOfIndices != null ? _allOfIndices.size() : 0) + (_anyOfIndices != null ? _anyOfIndices.size() : 0) + (_noneOfIndices != null ? _noneOfIndices.size() : 0);

        ArrayList<Integer> indicesList = new ArrayList<Integer>();
        if (_allOfIndices != null) {
            indicesList.addAll(_allOfIndices);
        }
        if (_anyOfIndices != null) {
            indicesList.addAll(_anyOfIndices);
        }
        if (_noneOfIndices != null) {
            indicesList.addAll(_noneOfIndices);
        }
        return DistinctIndices(indicesList);

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