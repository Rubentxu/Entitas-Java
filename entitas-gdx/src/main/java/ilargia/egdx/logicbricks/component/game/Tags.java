package ilargia.egdx.logicbricks.component.game;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;
import ilargia.entitas.factories.EntitasCollections;

import java.util.Set;

@Component(pools = {"Game"})
public class Tags implements IComponent {
    public Set<String> values;

    public Tags(String  ...values) {
        if (this.values == null) {
            this.values = EntitasCollections.createSet(String.class);
        } else {
            this.values.clear();
        }
        for (String value : values) {
            this.values.add(value);
        }
    }
}

