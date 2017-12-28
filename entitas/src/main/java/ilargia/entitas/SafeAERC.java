package ilargia.entitas;

import ilargia.entitas.api.entitas.IAERC;
import ilargia.entitas.api.entitas.IEntity;
import ilargia.entitas.exceptions.EntityIsAlreadyRetainedByOwnerException;
import ilargia.entitas.exceptions.EntityIsNotRetainedByOwnerException;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Set;

/**
 * Automatic Entity Reference Counting (AERC) is used internally to prevent pooling retained entities.
 * If you use retain manually you also have to release it manually at some point.
 * SafeAERC checks if the entity has already been retained or released.
 * It's slower, but you keep the information about the owners.
 * @author  Rubentxu
 *
 */
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
