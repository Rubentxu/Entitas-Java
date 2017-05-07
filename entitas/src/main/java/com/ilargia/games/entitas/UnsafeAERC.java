package com.ilargia.games.entitas;


import com.ilargia.games.entitas.api.entitas.IAERC;

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

