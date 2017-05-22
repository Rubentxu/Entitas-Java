package ilargia.entitas.exceptions;


import ilargia.entitas.api.entitas.EntitasException;
import ilargia.entitas.group.Group;

public class GroupSingleEntityException extends EntitasException {

    public GroupSingleEntityException(Group group) {
        super("Cannot get the single entity from " + group + "!\nGroup contains " + group.getCount() + " entities:", "");
    }
}
