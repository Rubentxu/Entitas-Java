package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.codeGenerator.Component;
import com.ilargia.games.entitas.interfaces.IComponent;


@Component(pools = {"Test", "Test2"}, isSingleEntity = true)
public class Player implements IComponent {
}