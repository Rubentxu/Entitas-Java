package ilargia.egdx.logicbricks.index;

public class KeyIndex {
    public int targetEntity;
    public String nameReference;

    public KeyIndex(int targetEntity, String nameReference){
        setIndex(targetEntity, nameReference);
    }

    public KeyIndex setIndex(int targetEntity, String nameReference) {
        this.targetEntity = targetEntity;
        this.nameReference = nameReference;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyIndex index = (KeyIndex) o;

        if (targetEntity != index.targetEntity) return false;
        return nameReference.equals(index.nameReference);

    }

    @Override
    public int hashCode() {
        int result = targetEntity;
        result = 31 * result + nameReference.hashCode();
        return result;
    }
}
