/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
var com;
(function (com) {
    var ilargia;
    (function (ilargia) {
        var games;
        (function (games) {
            var entitas;
            (function (entitas) {
                var matcher;
                (function (matcher_1) {
                    var EntitasCache = com.ilargia.games.entitas.caching.EntitasCache;
                    var MatcherException = com.ilargia.games.entitas.exceptions.MatcherException;
                    var Arrays = java.util.Arrays;
                    var Matcher = (function () {
                        function Matcher() {
                            this._isHashCached = false;
                            this._hash = 0;
                        }
                        Matcher.AllOf = function () {
                            var _this = this;
                            var indices = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                indices[_i - 0] = arguments[_i];
                            }
                            if (((indices != null && indices instanceof Array) || indices === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                return (function () {
                                    var matcher = (new Matcher());
                                    matcher._allOfIndices = Matcher.distinctIndices.apply(_this, indices);
                                    return matcher;
                                })();
                            }
                            else if (((indices != null && indices instanceof Array) || indices === null)) {
                                return com.ilargia.games.entitas.matcher.Matcher.AllOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        Matcher.AllOf$com_ilargia_games_entitas_api_matcher_IMatcher_A = function () {
                            var matchers = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                matchers[_i - 0] = arguments[_i];
                            }
                            var allOfMatcher = Matcher.AllOf.apply(this, Matcher.mergeIndices.apply(this, matchers));
                            Matcher.setComponentNames(allOfMatcher, matchers);
                            return allOfMatcher;
                        };
                        Matcher.AnyOf = function () {
                            var _this = this;
                            var indices = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                indices[_i - 0] = arguments[_i];
                            }
                            if (((indices != null && indices instanceof Array) || indices === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                return (function () {
                                    var matcher = (new Matcher());
                                    matcher._anyOfIndices = Matcher.distinctIndices.apply(_this, indices);
                                    return matcher;
                                })();
                            }
                            else if (((indices != null && indices instanceof Array) || indices === null)) {
                                return com.ilargia.games.entitas.matcher.Matcher.AnyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        Matcher.AnyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A = function () {
                            var matchers = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                matchers[_i - 0] = arguments[_i];
                            }
                            var anyOfMatcher = Matcher.AnyOf.apply(null, Matcher.mergeIndices.apply(this, matchers));
                            Matcher.setComponentNames(anyOfMatcher, matchers);
                            return anyOfMatcher;
                        };
                        Matcher.mergeIndices = function (allOfIndices, anyOfIndices, noneOfIndices) {
                            var _this = this;
                            if (((allOfIndices != null && allOfIndices instanceof Array) || allOfIndices === null) && ((anyOfIndices != null && anyOfIndices instanceof Array) || anyOfIndices === null) && ((noneOfIndices != null && noneOfIndices instanceof Array) || noneOfIndices === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                return (function () {
                                    var indicesList = EntitasCache.getIntArray();
                                    if (allOfIndices != null) {
                                        for (var index151 = 0; index151 < allOfIndices.length; index151++) {
                                            var it = allOfIndices[index151];
                                            {
                                                indicesList.add(it);
                                            }
                                        }
                                    }
                                    if (anyOfIndices != null) {
                                        for (var index152 = 0; index152 < anyOfIndices.length; index152++) {
                                            var it = anyOfIndices[index152];
                                            {
                                                indicesList.add(it);
                                            }
                                        }
                                    }
                                    if (noneOfIndices != null) {
                                        for (var index153 = 0; index153 < noneOfIndices.length; index153++) {
                                            var it = noneOfIndices[index153];
                                            {
                                                indicesList.add(it);
                                            }
                                        }
                                    }
                                    var temp = new Array(indicesList.size());
                                    for (var i = 0; i < indicesList.size(); i++) {
                                        temp[i] = indicesList.get(i);
                                    }
                                    var mergeIndices = Matcher.distinctIndices.apply(_this, temp);
                                    EntitasCache.pushIntArray(indicesList);
                                    return mergeIndices;
                                })();
                            }
                            else if (((allOfIndices != null && allOfIndices instanceof Array) || allOfIndices === null) && anyOfIndices === undefined && noneOfIndices === undefined) {
                                return com.ilargia.games.entitas.matcher.Matcher.mergeIndices$com_ilargia_games_entitas_api_matcher_IMatcher_A(allOfIndices);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        Matcher.mergeIndices$com_ilargia_games_entitas_api_matcher_IMatcher_A = function () {
                            var matchers = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                matchers[_i - 0] = arguments[_i];
                            }
                            var indices = new Array(matchers.length);
                            for (var i = 0; i < matchers.length; i++) {
                                var matcher_2 = matchers[i];
                                if (matcher_2.getIndices().length !== 1) {
                                    throw new MatcherException(matcher_2);
                                }
                                indices[i] = matcher_2.getIndices()[0];
                            }
                            return indices;
                        };
                        Matcher.getComponentNames = function (matchers) {
                            for (var i = 0; i < matchers.length; i++) {
                                var matcher_3 = matchers[i];
                                if (matcher_3 != null && matcher_3.componentNames != null) {
                                    return matcher_3.componentNames;
                                }
                            }
                            return null;
                        };
                        Matcher.setComponentNames = function (matcher, matchers) {
                            var componentNames = Matcher.getComponentNames(matchers);
                            if (componentNames != null) {
                                matcher.componentNames = componentNames;
                            }
                        };
                        Matcher.distinctIndices = function () {
                            var indices = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                indices[_i - 0] = arguments[_i];
                            }
                            var indicesSet = EntitasCache.getIntHashSet();
                            for (var index154 = 0; index154 < indices.length; index154++) {
                                var indice = indices[index154];
                                {
                                    indicesSet.add(indice);
                                }
                            }
                            var uniqueIndices = new Array(indicesSet.size());
                            var i = 0;
                            for (var index155 = indicesSet.iterator(); index155.hasNext();) {
                                var ind = index155.next();
                                {
                                    uniqueIndices[i] = ind;
                                    i++;
                                }
                            }
                            Arrays.sort(uniqueIndices);
                            EntitasCache.pushIntHashSet(indicesSet);
                            return uniqueIndices;
                        };
                        Matcher.appendIndices = function (sb, prefix, indexArray, componentNames) {
                            var SEPARATOR = ", ";
                            sb.append(prefix);
                            sb.append("(");
                            var lastSeparator = indexArray.length - 1;
                            for (var i = 0, indicesLength = indexArray.length; i < indicesLength; i++) {
                                var index = indexArray[i];
                                if (componentNames == null) {
                                    sb.append(index);
                                }
                                else {
                                    sb.append(componentNames[index]);
                                }
                                if (i < lastSeparator) {
                                    sb.append(SEPARATOR);
                                }
                            }
                            sb.append(")");
                        };
                        Matcher.applyHash = function (hash, indices, i1, i2) {
                            if (indices != null) {
                                for (var i = 0, indicesLength = indices.length; i < indicesLength; i++) {
                                    hash ^= indices[i] * i1;
                                }
                                hash ^= indices.length * i2;
                            }
                            return hash;
                        };
                        Matcher.equalIndices = function (i1, i2) {
                            if ((i1 == null) !== (i2 == null)) {
                                return false;
                            }
                            if (i1 == null) {
                                return true;
                            }
                            if (i1.length !== i2.length) {
                                return false;
                            }
                            for (var i = 0, indicesLength = i1.length; i < indicesLength; i++) {
                                if (i1[i] !== i2[i]) {
                                    return false;
                                }
                            }
                            return true;
                        };
                        Matcher.prototype.getIndices = function () {
                            if (this._indices == null) {
                                this._indices = Matcher.mergeIndices(this._allOfIndices, this._anyOfIndices, this._noneOfIndices);
                            }
                            return this._indices;
                        };
                        Matcher.prototype.getAllOfIndices = function () {
                            return this._allOfIndices;
                        };
                        Matcher.prototype.getAnyOfIndices = function () {
                            return this._anyOfIndices;
                        };
                        Matcher.prototype.getNoneOfIndices = function () {
                            return this._noneOfIndices;
                        };
                        Matcher.prototype.anyOf = function () {
                            var _this = this;
                            var indices = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                indices[_i - 0] = arguments[_i];
                            }
                            if (((indices != null && indices instanceof Array) || indices === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                return (function () {
                                    _this._anyOfIndices = Matcher.distinctIndices.apply(_this, indices);
                                    _this._indices = null;
                                    return _this;
                                })();
                            }
                            else if (((indices != null && indices instanceof Array) || indices === null)) {
                                return this.anyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        Matcher.prototype.anyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A = function () {
                            var matchers = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                matchers[_i - 0] = arguments[_i];
                            }
                            return (this['__jswref_0'] = this).anyOf.apply(this['__jswref_0'], Matcher.mergeIndices.apply(this, matchers));
                        };
                        Matcher.prototype.noneOf = function () {
                            var _this = this;
                            var indices = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                indices[_i - 0] = arguments[_i];
                            }
                            if (((indices != null && indices instanceof Array) || indices === null)) {
                                var __args = Array.prototype.slice.call(arguments);
                                return (function () {
                                    _this._noneOfIndices = Matcher.distinctIndices.apply(_this, indices);
                                    _this._indices = null;
                                    return _this;
                                })();
                            }
                            else if (((indices != null && indices instanceof Array) || indices === null)) {
                                return this.noneOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
                            }
                            else
                                throw new Error('invalid overload');
                        };
                        Matcher.prototype.noneOf$com_ilargia_games_entitas_api_matcher_IMatcher_A = function () {
                            var matchers = [];
                            for (var _i = 0; _i < arguments.length; _i++) {
                                matchers[_i - 0] = arguments[_i];
                            }
                            return this.noneOf.apply(this, Matcher.mergeIndices.apply(this, matchers));
                        };
                        Matcher.prototype.matches = function (entity) {
                            var matchesAllOf = this._allOfIndices == null || entity.hasComponents(this._allOfIndices);
                            var matchesAnyOf = this._anyOfIndices == null || entity.hasAnyComponent(this._anyOfIndices);
                            var matchesNoneOf = this._noneOfIndices == null || !entity.hasAnyComponent(this._noneOfIndices);
                            return matchesAllOf && matchesAnyOf && matchesNoneOf;
                        };
                        Matcher.prototype.hashCode = function () {
                            if (!this._isHashCached) {
                                var hash = this.constructor.hashCode();
                                hash = Matcher.applyHash(hash, this._allOfIndices, 3, 53);
                                hash = Matcher.applyHash(hash, this._anyOfIndices, 307, 367);
                                hash = Matcher.applyHash(hash, this._noneOfIndices, 647, 683);
                                this._hash = hash;
                                this._isHashCached = true;
                            }
                            return this._hash;
                        };
                        Matcher.prototype.equals = function (obj) {
                            if (obj == null || obj.constructor !== this.constructor || obj.toString() !== this.hashCode()) {
                                return false;
                            }
                            var matcher = obj;
                            if (!Matcher.equalIndices(matcher.getAllOfIndices(), this._allOfIndices)) {
                                return false;
                            }
                            if (!Matcher.equalIndices(matcher.getAnyOfIndices(), this._anyOfIndices)) {
                                return false;
                            }
                            if (!Matcher.equalIndices(matcher.getNoneOfIndices(), this._noneOfIndices)) {
                                return false;
                            }
                            return true;
                        };
                        Matcher.prototype.toString = function () {
                            return "Matcher{, _indices=" + this.components(this._indices) + ", _allOfIndices=" + this.components(this._allOfIndices) + ", _anyOfIndices=" + this.components(this._anyOfIndices) + ", _noneOfIndices=" + this.components(this._noneOfIndices) + ", _hash=" + this._hash + '}';
                        };
                        Matcher.prototype.components = function (indices) {
                            var builder = new java.lang.StringBuilder();
                            if (indices != null) {
                                for (var index156 = 0; index156 < indices.length; index156++) {
                                    var i = indices[index156];
                                    {
                                        builder.append(this.componentNames[i]);
                                    }
                                }
                            }
                            return builder.toString();
                        };
                        return Matcher;
                    }());
                    matcher_1.Matcher = Matcher;
                    Matcher["__class"] = "com.ilargia.games.entitas.matcher.Matcher";
                    Matcher["__interfaces"] = ["com.ilargia.games.entitas.api.matcher.ICompoundMatcher", "com.ilargia.games.entitas.api.matcher.IAnyOfMatcher", "com.ilargia.games.entitas.api.matcher.IMatcher", "com.ilargia.games.entitas.api.matcher.IAllOfMatcher", "com.ilargia.games.entitas.api.matcher.INoneOfMatcher"];
                })(matcher = entitas.matcher || (entitas.matcher = {}));
            })(entitas = games.entitas || (games.entitas = {}));
        })(games = ilargia.games || (ilargia.games = {}));
    })(ilargia = com.ilargia || (com.ilargia = {}));
})(com || (com = {}));
