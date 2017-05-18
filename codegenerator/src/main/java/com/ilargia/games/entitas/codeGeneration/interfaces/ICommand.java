package com.ilargia.games.entitas.codeGeneration.interfaces;


public interface ICommand {

    String trigger();

    String description();

    String example();

    void run(String[] args);

}
