package ilargia.entitas.exceptions;

public class EntityDoesNotHaveComponentException extends RuntimeException {

    public EntityDoesNotHaveComponentException(String message, int index) {
        super(message + "\nEntity does not have a component at index " + index);
    }

}