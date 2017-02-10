/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.matcher {
    import IEntity = com.ilargia.games.entitas.api.IEntity;

    import IAllOfMatcher = com.ilargia.games.entitas.api.matcher.IAllOfMatcher;

    import IAnyOfMatcher = com.ilargia.games.entitas.api.matcher.IAnyOfMatcher;

    import IMatcher = com.ilargia.games.entitas.api.matcher.IMatcher;

    import INoneOfMatcher = com.ilargia.games.entitas.api.matcher.INoneOfMatcher;

    import EntitasCache = com.ilargia.games.entitas.caching.EntitasCache;

    import MatcherException = com.ilargia.games.entitas.exceptions.MatcherException;

    import Arrays = java.util.Arrays;

    import List = java.util.List;

    import Set = java.util.Set;

    export class Matcher<TEntity extends IEntity> implements IAllOfMatcher<TEntity>, IAnyOfMatcher<TEntity>, INoneOfMatcher<TEntity> {
        public componentNames : string[];

        private _indices : number[];

        private _allOfIndices : number[];

        private _anyOfIndices : number[];

        private _noneOfIndices : number[];

        private _hash : number;

        private _isHashCached : boolean = false;

        constructor() {
            this._hash = 0;
        }

        public static AllOf<TEntity extends IEntity>(...indices : any[]) : any {
            if(((indices != null && indices instanceof Array) || indices === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    let matcher : Matcher<any> = <any>(new Matcher<TEntity>());
                    matcher._allOfIndices = Matcher.distinctIndices.apply(this, indices);
                    return matcher;
                })();
            } else if(((indices != null && indices instanceof Array) || indices === null)) {
                return <any>com.ilargia.games.entitas.matcher.Matcher.AllOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
            } else throw new Error('invalid overload');
        }

        public static AllOf$com_ilargia_games_entitas_api_matcher_IMatcher_A<TEntity extends IEntity>(...matchers : IMatcher<any>[]) : IAllOfMatcher<TEntity> {
            let allOfMatcher : Matcher<any> = <Matcher<TEntity>>Matcher.AllOf.apply(this, Matcher.mergeIndices.apply(this, matchers));
            Matcher.setComponentNames<any>(allOfMatcher, matchers);
            return allOfMatcher;
        }

        public static AnyOf<TEntity extends IEntity>(...indices : any[]) : any {
            if(((indices != null && indices instanceof Array) || indices === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    let matcher : Matcher<TEntity> = <any>(new Matcher<TEntity>());
                    matcher._anyOfIndices = Matcher.distinctIndices.apply(this, indices);
                    return matcher;
                })();
            } else if(((indices != null && indices instanceof Array) || indices === null)) {
                return <any>com.ilargia.games.entitas.matcher.Matcher.AnyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
            } else throw new Error('invalid overload');
        }

        public static AnyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A<TEntity extends IEntity>(...matchers : IMatcher<TEntity>[]) : IAnyOfMatcher<TEntity> {
            let anyOfMatcher : Matcher<any> = <Matcher<TEntity>>Matcher.AnyOf.apply(null, Matcher.mergeIndices.apply(this, matchers));
            Matcher.setComponentNames<any>(anyOfMatcher, matchers);
            return anyOfMatcher;
        }

        public static mergeIndices(allOfIndices? : any, anyOfIndices? : any, noneOfIndices? : any) : any {
            if(((allOfIndices != null && allOfIndices instanceof Array) || allOfIndices === null) && ((anyOfIndices != null && anyOfIndices instanceof Array) || anyOfIndices === null) && ((noneOfIndices != null && noneOfIndices instanceof Array) || noneOfIndices === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    let indicesList : List<number> = EntitasCache.getIntArray();
                    if(allOfIndices != null) {
                        for(let index151=0; index151 < allOfIndices.length; index151++) {
                            let it = allOfIndices[index151];
                            {
                                indicesList.add(it);
                            }
                        }
                    }
                    if(anyOfIndices != null) {
                        for(let index152=0; index152 < anyOfIndices.length; index152++) {
                            let it = anyOfIndices[index152];
                            {
                                indicesList.add(it);
                            }
                        }
                    }
                    if(noneOfIndices != null) {
                        for(let index153=0; index153 < noneOfIndices.length; index153++) {
                            let it = noneOfIndices[index153];
                            {
                                indicesList.add(it);
                            }
                        }
                    }
                    let temp : number[] = new Array(indicesList.size());
                    for(let i : number = 0; i < indicesList.size(); i++) {
                        temp[i] = indicesList.get(i);
                    }
                    let mergeIndices : number[] = Matcher.distinctIndices.apply(this, temp);
                    EntitasCache.pushIntArray(indicesList);
                    return mergeIndices;
                })();
            } else if(((allOfIndices != null && allOfIndices instanceof Array) || allOfIndices === null) && anyOfIndices === undefined && noneOfIndices === undefined) {
                return <any>com.ilargia.games.entitas.matcher.Matcher.mergeIndices$com_ilargia_games_entitas_api_matcher_IMatcher_A(allOfIndices);
            } else throw new Error('invalid overload');
        }

        static mergeIndices$com_ilargia_games_entitas_api_matcher_IMatcher_A(...matchers : IMatcher<any>[]) : number[] {
            let indices : number[] = new Array(matchers.length);
            for(let i : number = 0; i < matchers.length; i++) {
                let matcher : IMatcher<any> = matchers[i];
                if(matcher.getIndices().length !== 1) {
                    throw new MatcherException(matcher);
                }
                indices[i] = matcher.getIndices()[0];
            }
            return indices;
        }

        static getComponentNames<TEntity extends IEntity>(matchers : IMatcher<TEntity>[]) : string[] {
            for(let i : number = 0; i < matchers.length; i++) {
                let matcher : Matcher<any> = <Matcher<any>>matchers[i];
                if(matcher != null && matcher.componentNames != null) {
                    return matcher.componentNames;
                }
            }
            return null;
        }

        static setComponentNames<TEntity extends IEntity>(matcher : Matcher<TEntity>, matchers : IMatcher<TEntity>[]) {
            let componentNames : string[] = Matcher.getComponentNames<any>(matchers);
            if(componentNames != null) {
                matcher.componentNames = componentNames;
            }
        }

        static distinctIndices(...indices : number[]) : number[] {
            let indicesSet : Set<number> = EntitasCache.getIntHashSet();
            for(let index154=0; index154 < indices.length; index154++) {
                let indice = indices[index154];
                {
                    indicesSet.add(indice);
                }
            }
            let uniqueIndices : number[] = new Array(indicesSet.size());
            let i : number = 0;
            for(let index155=indicesSet.iterator();index155.hasNext();) {
                let ind = index155.next();
                {
                    uniqueIndices[i] = ind;
                    i++;
                }
            }
            Arrays.sort(uniqueIndices);
            EntitasCache.pushIntHashSet(indicesSet);
            return uniqueIndices;
        }

        private static appendIndices(sb : java.lang.StringBuilder, prefix : string, indexArray : number[], componentNames : string[]) {
            let SEPARATOR : string = ", ";
            sb.append(prefix);
            sb.append("(");
            let lastSeparator : number = indexArray.length - 1;
            for(let i : number = 0, indicesLength : number = indexArray.length; i < indicesLength; i++) {
                let index : number = indexArray[i];
                if(componentNames == null) {
                    sb.append(index);
                } else {
                    sb.append(componentNames[index]);
                }
                if(i < lastSeparator) {
                    sb.append(SEPARATOR);
                }
            }
            sb.append(")");
        }

        private static applyHash(hash : number, indices : number[], i1 : number, i2 : number) : number {
            if(indices != null) {
                for(let i : number = 0, indicesLength : number = indices.length; i < indicesLength; i++) {
                    hash ^= indices[i] * i1;
                }
                hash ^= indices.length * i2;
            }
            return hash;
        }

        private static equalIndices(i1 : number[], i2 : number[]) : boolean {
            if((i1 == null) !== (i2 == null)) {
                return false;
            }
            if(i1 == null) {
                return true;
            }
            if(i1.length !== i2.length) {
                return false;
            }
            for(let i : number = 0, indicesLength : number = i1.length; i < indicesLength; i++) {
                if(i1[i] !== i2[i]) {
                    return false;
                }
            }
            return true;
        }

        public getIndices() : number[] {
            if(this._indices == null) {
                this._indices = Matcher.mergeIndices(this._allOfIndices, this._anyOfIndices, this._noneOfIndices);
            }
            return this._indices;
        }

        public getAllOfIndices() : number[] {
            return this._allOfIndices;
        }

        public getAnyOfIndices() : number[] {
            return this._anyOfIndices;
        }

        public getNoneOfIndices() : number[] {
            return this._noneOfIndices;
        }

        public anyOf(...indices : any[]) : any {
            if(((indices != null && indices instanceof Array) || indices === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    this._anyOfIndices = Matcher.distinctIndices.apply(this, indices);
                    this._indices = null;
                    return this;
                })();
            } else if(((indices != null && indices instanceof Array) || indices === null)) {
                return <any>this.anyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
            } else throw new Error('invalid overload');
        }

        public anyOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(...matchers : IMatcher<TEntity>[]) : IAnyOfMatcher<TEntity> {
            return (this['__jswref_0'] = (<IAllOfMatcher<TEntity>>this)).anyOf.apply(this['__jswref_0'], Matcher.mergeIndices.apply(this, matchers));
        }

        public noneOf(...indices : any[]) : any {
            if(((indices != null && indices instanceof Array) || indices === null)) {
                let __args = Array.prototype.slice.call(arguments);
                return <any>(() => {
                    this._noneOfIndices = Matcher.distinctIndices.apply(this, indices);
                    this._indices = null;
                    return this;
                })();
            } else if(((indices != null && indices instanceof Array) || indices === null)) {
                return <any>this.noneOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(indices);
            } else throw new Error('invalid overload');
        }

        public noneOf$com_ilargia_games_entitas_api_matcher_IMatcher_A(...matchers : IMatcher<any>[]) : INoneOfMatcher<TEntity> {
            return this.noneOf.apply(this, Matcher.mergeIndices.apply(this, matchers));
        }

        public matches(entity : IEntity) : boolean {
            let matchesAllOf : boolean = this._allOfIndices == null || entity.hasComponents(this._allOfIndices);
            let matchesAnyOf : boolean = this._anyOfIndices == null || entity.hasAnyComponent(this._anyOfIndices);
            let matchesNoneOf : boolean = this._noneOfIndices == null || !entity.hasAnyComponent(this._noneOfIndices);
            return matchesAllOf && matchesAnyOf && matchesNoneOf;
        }

        public hashCode() : number {
            if(!this._isHashCached) {
                let hash : number = (<any>this.constructor).hashCode();
                hash = Matcher.applyHash(hash, this._allOfIndices, 3, 53);
                hash = Matcher.applyHash(hash, this._anyOfIndices, 307, 367);
                hash = Matcher.applyHash(hash, this._noneOfIndices, 647, 683);
                this._hash = hash;
                this._isHashCached = true;
            }
            return this._hash;
        }

        public equals(obj : any) : boolean {
            if(obj == null || (<any>obj.constructor) !== (<any>this.constructor) || (<any>obj.toString()) !== this.hashCode()) {
                return false;
            }
            let matcher : Matcher<TEntity> = <Matcher<TEntity>>obj;
            if(!Matcher.equalIndices(matcher.getAllOfIndices(), this._allOfIndices)) {
                return false;
            }
            if(!Matcher.equalIndices(matcher.getAnyOfIndices(), this._anyOfIndices)) {
                return false;
            }
            if(!Matcher.equalIndices(matcher.getNoneOfIndices(), this._noneOfIndices)) {
                return false;
            }
            return true;
        }

        public toString() : string {
            return "Matcher{, _indices=" + this.components(this._indices) + ", _allOfIndices=" + this.components(this._allOfIndices) + ", _anyOfIndices=" + this.components(this._anyOfIndices) + ", _noneOfIndices=" + this.components(this._noneOfIndices) + ", _hash=" + this._hash + '}';
        }

        private components(indices : number[]) : string {
            let builder : java.lang.StringBuilder = new java.lang.StringBuilder();
            if(indices != null) {
                for(let index156=0; index156 < indices.length; index156++) {
                    let i = indices[index156];
                    {
                        builder.append(this.componentNames[i]);
                    }
                }
            }
            return builder.toString();
        }
    }
    Matcher["__class"] = "com.ilargia.games.entitas.matcher.Matcher";
    Matcher["__interfaces"] = ["com.ilargia.games.entitas.api.matcher.ICompoundMatcher","com.ilargia.games.entitas.api.matcher.IAnyOfMatcher","com.ilargia.games.entitas.api.matcher.IMatcher","com.ilargia.games.entitas.api.matcher.IAllOfMatcher","com.ilargia.games.entitas.api.matcher.INoneOfMatcher"];


}

