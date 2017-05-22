package ilargia.entitas.components;

import ilargia.entitas.api.IComponent;
import ilargia.entitas.codeGenerator.Component;


@Component(pools = {"Test", "Test2"}, isSingleEntity = true)
public class Player implements IComponent {
}