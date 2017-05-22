package ilargia.entitas.api;


import java.util.Arrays;

public class ContextInfo {

    public String contextName;
    public String[] componentNames;
    public Class[] componentTypes;

    public ContextInfo(String contextName, String[] componentNames, Class[] componentTypes) {
        this.contextName = contextName;
        this.componentNames = componentNames;
        this.componentTypes = componentTypes;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContextInfo)) return false;

        ContextInfo that = (ContextInfo) o;

        if (contextName != null ? !contextName.equals(that.contextName) : that.contextName != null) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(componentNames, that.componentNames)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(componentTypes, that.componentTypes);
    }

    @Override
    public int hashCode() {
        int result = contextName != null ? contextName.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(componentNames);
        result = 31 * result + Arrays.hashCode(componentTypes);
        return result;
    }

    @Override
    public String toString() {
        return "ContextInfo{" +
                "contextName='" + contextName + '\'' +
                ", componentNames=" + Arrays.toString(componentNames) +
                '}';
    }
}
