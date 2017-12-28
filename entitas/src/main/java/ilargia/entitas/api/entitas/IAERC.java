package ilargia.entitas.api.entitas;


public interface IAERC {

    int retainCount();

    void retain(Object owner);

    void release(Object owner);

}
