package com.ilargia.games.core.component.sensor;


import com.badlogic.gdx.physics.box2d.Contact;
import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.factories.EntitasCollections;

import java.util.Set;

@Component(pools = {"Sensor"})
public class CollisionSensor implements IComponent {
    // Config Values
    public String targetTag;

    // Signal Values
    public Set<Contact> contactList;

    public CollisionSensor(String targetTag) {
        this.targetTag = targetTag;
        if (this.contactList == null) {
            this.contactList = EntitasCollections.createSet(Contact.class);
        } else {
            this.contactList.clear();
        }
    }
}

