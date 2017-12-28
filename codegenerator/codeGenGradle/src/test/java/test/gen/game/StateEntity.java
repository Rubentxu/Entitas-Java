package test.gen.game;

import ilargia.entitas.Entity;
import ilargia.fixtures.components.game.Score;

/**
 * //-------------------------------------------------------------------------//
 * <auto-generated>// This code was generated by {0}.//// Changes to this file
 * may cause incorrect behavior and will be lost if// the code is regenerated.//
 * </auto-generated>//----------------------------------------------------------
 * ---------------
 */
public class StateEntity extends Entity {

	public StateEntity() {
	}

	public Score getScore() {
		return (Score) getComponent(StateComponentsLookup.Score);
	}

	public boolean hasScore() {
		return hasComponent(StateComponentsLookup.Score);
	}

	public StateEntity addScore(int value) {
		Score component = (Score) recoverComponent(StateComponentsLookup.Score);
		if (component == null) {
			component = new Score(value);
		} else {
			component.value = value;
		}
		addComponent(StateComponentsLookup.Score, component);
		return this;
	}

	public StateEntity replaceScore(int value) {
		Score component = (Score) recoverComponent(StateComponentsLookup.Score);
		if (component == null) {
			component = new Score(value);
		} else {
			component.value = value;
		}
		replaceComponent(StateComponentsLookup.Score, component);
		return this;
	}

	public StateEntity removeScore() {
		removeComponent(StateComponentsLookup.Score);
		return this;
	}
}