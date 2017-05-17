package com.ilargia.games.entitas.codeGeneration.cli.commands;


import com.ilargia.games.entitas.codeGenerator.configuration.Preferences;

public class EditConfig extends AbstractCommand{
    @Override
    public String trigger() {
        return "edit";
    }

    @Override
    public String description() {
        return "Opens Entitas.properties config";
    }

    @Override
    public String example() {
        return "entitas edit";
    }

    @Override
    public void run(String[] args) {
        System.out.println("Entitas Code Generator version " + 1);

        if (assertProperties()) {
            System.out.println("Opening " + Preferences.PATH_PROPERTIES);
           // System.diagnostics.Process.Start(Preferences.PATH_PROPERTIES);
        }
    }
}
