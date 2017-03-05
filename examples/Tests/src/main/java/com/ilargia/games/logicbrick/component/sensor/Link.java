package com.ilargia.games.logicbrick.component.sensor;


import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.api.IEntity;
import com.ilargia.games.entitas.codeGenerator.Component;

@Component(pools = {"Sensor"})
public class Link<TEntity extends IEntity> implements IComponent {
    public TEntity target;
    public boolean isOpen;
    public boolean isChanged;
    public boolean pulse;

    public Link(TEntity target) {
        this.target = target;
        this.isOpen = false;
        this.isChanged = false;
        this.pulse = false;
    }
}
