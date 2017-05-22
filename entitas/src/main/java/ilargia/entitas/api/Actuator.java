package ilargia.entitas.api;


@FunctionalInterface
public interface Actuator<C extends IComponent> {
    void modify(C component);

}
