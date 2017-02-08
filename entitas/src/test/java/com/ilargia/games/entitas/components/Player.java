package com.ilargia.games.entitas.components;

import com.ilargia.games.entitas.api.IComponent;
import com.ilargia.games.entitas.codeGenerator.Component;


@Component(pools = {"Test", "Test2"}, isSingleEntity = true)
public class Player implements IComponent {
}