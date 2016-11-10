package com.ilargia.games.entitas.exceptions;


import com.ilargia.games.entitas.Group;

public class GroupSingleEntityException extends EntitasException {

    public GroupSingleEntityException(Group group) {
        super("Cannot get the single entity from " + group + "!\nGroup contains " + group.getcount() + " entities:", "");
    }
}