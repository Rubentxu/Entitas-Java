package ilargia.entitas;


import ilargia.entitas.api.entitas.IAERC;

/**
*  Automatic Entity Reference Counting (AERC) is used internally to prevent pooling retained entities.
* If you use retain manually you also have to release it manually at some point.
* UnsafeAERC doesn't check if the entity has already been retained or released.
* It's faster, but you lose the information about the owners.
* @author Rubentxu
* */
public class UnsafeAERC implements IAERC {

    int _retainCount;

    public int retainCount() { return _retainCount; }

    public void retain(Object owner) {
        _retainCount += 1;
    }

    public void release(Object owner) {
        _retainCount -= 1;
    }

}

