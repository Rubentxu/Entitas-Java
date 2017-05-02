package com.ilargia.games.entitas.api.system;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

public interface IInitializeSystem extends ISystem {
    void initialize();
}
