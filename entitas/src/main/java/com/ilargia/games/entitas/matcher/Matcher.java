package com.ilargia.games.entitas.matcher;

import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.api.matcher.IAllOfMatcher;
import com.ilargia.games.entitas.api.matcher.IAnyOfMatcher;
import com.ilargia.games.entitas.api.matcher.IMatcher;
import com.ilargia.games.entitas.api.matcher.INoneOfMatcher;
import com.ilargia.games.entitas.caching.EntitasCache;
import com.ilargia.games.entitas.exceptions.MatcherException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class Matcher<TEntity extends IEntity> implements IAllOfMatcher<TEntity>, IAnyOfMatcher<TEntity>, INoneOfMatcher<TEntity> {

    public String[] componentNames;
    private int[] _indices;
    private int[] _allOfIndices;
    private int[] _anyOfIndices;
    private int[] _noneOfIndices;
    private int _hash;
    private boolean _isHashCached = false;

    private Matcher() {
    }


    // Begin MatcherStatic
    public static <TEntity extends IEntity> IAllOfMatcher<TEntity> AllOf(int... indices) {
        Matcher matcher = new Matcher<TEntity>();
        matcher._allOfIndices = distinctIndices(indices);
        return matcher;
    }

    public static <TEntity extends IEntity> IAllOfMatcher<TEntity> AllOf(IMatcher... matchers) {
        Matcher allOfMatcher = (Matcher<TEntity>) AllOf(mergeIndices(matchers));
        setComponentNames(allOfMatcher, matchers);
        return allOfMatcher;
    }

    public static <TEntity extends IEntity> IAnyOfMatcher<TEntity> AnyOf(int... indices) {
        Matcher<TEntity> matcher = new Matcher<TEntity>();
        matcher._anyOfIndices = distinctIndices(indices);
        return matcher;
    }

    public static <TEntity extends IEntity> IAnyOfMatcher<TEntity> AnyOf(IMatcher<TEntity>... matchers) {
        Matcher anyOfMatcher = (Matcher<TEntity>) Matcher.AnyOf(mergeIndices(matchers));
        setComponentNames(anyOfMatcher, matchers);
        return anyOfMatcher;

    }

    private static int[] mergeIndices(int[] allOfIndices, int[] anyOfIndices, int[] noneOfIndices) {
        List<Integer> indicesList = EntitasCache.getIntArray();

        if (allOfIndices != null) {
            for (int it : allOfIndices) {
                indicesList.add(it);
            }
        }
        if (anyOfIndices != null) {
            for (int it : anyOfIndices) {
                indicesList.add(it);
            }
        }
        if (noneOfIndices != null) {
            for (int it : noneOfIndices) {
                indicesList.add(it);
            }

        }
        int temp[] = new int[indicesList.size()];
        for (int i = 0; i < indicesList.size(); i++) {
            temp[i] = indicesList.get(i);
        }

        int[] mergeIndices = distinctIndices(temp);

        EntitasCache.pushIntArray(indicesList);
        return mergeIndices;

    }

    static int[] mergeIndices(IMatcher... matchers) {
        int[] indices = new int[matchers.length];
        for (int i = 0; i < matchers.length; i++) {
            IMatcher matcher = matchers[i];
            if (matcher.getIndices().length != 1) {
                throw new MatcherException(matcher);
            }
            indices[i] = matcher.getIndices()[0];
        }

        return indices;
    }

    static <TEntity extends IEntity> String[] getComponentNames(IMatcher<TEntity>[] matchers) {
        for (int i = 0; i < matchers.length; i++) {
            Matcher matcher = (Matcher) matchers[i];
            if (matcher != null && matcher.componentNames != null) {
                return matcher.componentNames;
            }
        }

        return null;
    }

    static <TEntity extends IEntity> void setComponentNames(Matcher<TEntity> matcher, IMatcher<TEntity>[] matchers) {
        String[] componentNames = getComponentNames(matchers);
        if (componentNames != null) {
            matcher.componentNames = componentNames;
        }

    }

    static int[] distinctIndices(int... indices) {
        Set<Integer> indicesSet = EntitasCache.getIntHashSet(); // IntArraySet
        // IntArrays
        for (int indice : indices) {
            indicesSet.add(indice);
        }
        int[] uniqueIndices = new int[indicesSet.size()];
        int i = 0;
        for (Integer ind : indicesSet) {
            uniqueIndices[i] = ind;
            i++;
        }
        Arrays.sort(uniqueIndices);
        EntitasCache.pushIntHashSet(indicesSet);
        return uniqueIndices;

    }
    // End Matcher

    // Begin MatcherToString
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

    private static int applyHash(int hash, int[] indices, int i1, int i2) {
        if (indices != null) {
            for (int i = 0, indicesLength = indices.length; i < indicesLength; i++) {
                hash ^= indices[i] * i1;
            }
            hash ^= indices.length * i2;
        }
        return hash;
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

    public int[] getIndices() {
        if (_indices == null) {
            _indices = mergeIndices(_allOfIndices, _anyOfIndices, _noneOfIndices);
        }
        return _indices;
    }

    public int[] getAllOfIndices() {
        return _allOfIndices;
    }

    public int[] getAnyOfIndices() {
        return _anyOfIndices;
    }

    // End MatcherEquals
    public int[] getNoneOfIndices() {
        return _noneOfIndices;
    }

    @Override
    public IAnyOfMatcher<TEntity> anyOf(int... indices) {
        _anyOfIndices = distinctIndices(indices);
        _indices = null;
        return this;
    }

    @Override
    public IAnyOfMatcher<TEntity> anyOf(IMatcher<TEntity>... matchers) {
        return ((IAllOfMatcher<TEntity>) this).anyOf(mergeIndices(matchers));
    }

    @Override
    public INoneOfMatcher<TEntity> noneOf(int... indices) {
        _noneOfIndices = distinctIndices(indices);
        _indices = null;
        return this;
    }

    @Override
    public INoneOfMatcher<TEntity> noneOf(IMatcher... matchers) {
        return noneOf(mergeIndices(matchers));
    }

    @Override
    public boolean matches(IEntity entity) {
        boolean matchesAllOf = _allOfIndices == null || entity.hasComponents(_allOfIndices);
        boolean matchesAnyOf = _anyOfIndices == null || entity.hasAnyComponent(_anyOfIndices);
        boolean matchesNoneOf = _noneOfIndices == null || !entity.hasAnyComponent(_noneOfIndices);
        return matchesAllOf && matchesAnyOf && matchesNoneOf;
    }

    @Override
    public int hashCode() {
        if (!_isHashCached) {
            int hash = getClass().hashCode();
            hash = applyHash(hash, _allOfIndices, 3, 53);
            hash = applyHash(hash, _anyOfIndices, 307, 367);
            hash = applyHash(hash, _noneOfIndices, 647, 683);
            _hash = hash;
            _isHashCached = true;
        }

        return _hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass() ||
                obj.hashCode() != hashCode()) {
            return false;
        }

        Matcher<TEntity> matcher = (Matcher<TEntity>) obj;
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

    @Override
    public String toString() {
        return "Matcher{" +
                ", _indices=" + components(_indices) +
                ", _allOfIndices=" + components(_allOfIndices) +
                ", _anyOfIndices=" + components(_anyOfIndices) +
                ", _noneOfIndices=" + components(_noneOfIndices) +
                ", _hash=" + _hash +
                '}';
    }

    private String components(int[] indices) {
        StringBuilder builder = new StringBuilder();
        if (indices != null) {
            for (int i : indices) {
                builder.append(componentNames[i]);
            }
        }
        return builder.toString();
    }

}