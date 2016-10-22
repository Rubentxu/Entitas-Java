package com.ilargia.games.entitas.exceptions;

import com.ilargia.games.entitas.Group;

public class SingleEntityException extends EntitasException {

    public SingleEntityException(Group group) {
        super( "Cannot get the single entity from " + group +
                "!\nGroup contains " + group.getcount() + " entities:","");
    }

}