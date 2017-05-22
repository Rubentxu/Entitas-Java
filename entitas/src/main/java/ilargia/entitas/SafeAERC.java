package ilargia.entitas;

import ilargia.entitas.api.entitas.IAERC;
import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.exceptions.EntityIsAlreadyRetainedByOwnerException;
import ilargia.entitas.exceptions.EntityIsNotRetainedByOwnerException;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Set;

public class SafeAERC implements IAERC {
    private IEntity _entity;
    private Set<Object> _owners;

    @Override
    public int retainCount() { return _owners.size(); }

    public SafeAERC(IEntity entity) {
        _entity = entity;
        _owners = EntitasCollections.createSet(Object.class);
    }

    public Set<Object> owners() {
        return _owners;
    }

    public void retain(Object owner) {
        if (!_owners.add(owner)) {
            throw new EntityIsAlreadyRetainedByOwnerException(_entity, owner);
        }
    }

    @Override
    public void release(Object owner) {
        if (!_owners.remove(owner)) {
            throw new EntityIsNotRetainedByOwnerException(_entity, owner);
        }
    }

}
