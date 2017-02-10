/* Generated from Java with JSweet 1.2.0-SNAPSHOT - http://www.jsweet.org */
namespace com.ilargia.games.entitas.api {
    import Arrays = java.util.Arrays;

    export class ContextInfo {
        public contextName : string;

        public componentNames : string[];

        public componentTypes : java.lang.Class<any>[];

        public constructor(contextName : string, componentNames : string[], componentTypes : java.lang.Class<any>[]) {
            this.contextName = contextName;
            this.componentNames = componentNames;
            this.componentTypes = componentTypes;
        }

        public equals(o : any) : boolean {
            if(this === o) return true;
            if(!(o != null && o instanceof com.ilargia.games.entitas.api.ContextInfo)) return false;
            let that : ContextInfo = <ContextInfo>o;
            if(this.contextName != null?!(this.contextName === that.contextName):that.contextName != null) return false;
            if(!Arrays.equals(this.componentNames, that.componentNames)) return false;
            return Arrays.equals(this.componentTypes, that.componentTypes);
        }

        public hashCode() : number {
            let result : number = this.contextName != null?(<any>this.contextName.toString()):0;
            result = 31 * result + Arrays.hashCode(this.componentNames);
            result = 31 * result + Arrays.hashCode(this.componentTypes);
            return result;
        }

        public toString() : string {
            return "ContextInfo{contextName=\'" + this.contextName + '\'' + ", componentNames=" + Arrays.toString(this.componentNames) + '}';
        }
    }
    ContextInfo["__class"] = "com.ilargia.games.entitas.api.ContextInfo";

}

