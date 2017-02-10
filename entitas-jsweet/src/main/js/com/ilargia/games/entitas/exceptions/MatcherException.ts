/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.exceptions {
    import IMatcher = com.ilargia.games.entitas.api.matcher.IMatcher;

    export class MatcherException extends Error {
        public constructor(matcher : IMatcher<any>) {
            super("length matcher index must contain at least one, and has " + matcher.getIndices().length); this.message="length matcher index must contain at least one, and has " + matcher.getIndices().length;
        }
    }
    MatcherException["__class"] = "com.ilargia.games.entitas.exceptions.MatcherException";
    MatcherException["__interfaces"] = ["java.io.Serializable"];


}

