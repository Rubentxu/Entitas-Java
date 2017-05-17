package com.ilargia.games.entitas.codeGeneration.cli.commands;


import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGeneratorUtil;

public class Doctor extends AbstractCommand {
    @Override
    public String trigger() {
        return "doctor";
    }

    @Override
    public String description() {
        return "Checks the config for potential problems";
    }

    @Override
    public String example() {
        return "entitas doctor";
    }

    @Override
    public void run(String[] args) {
        System.out.println("Entitas Code Generator version " + 1);

        if (assertProperties()) {

            new Status().run(args);

            System.out.println("Dry Run");

            CodeGeneratorUtil
                    .codeGeneratorFromProperties()
                    .dryRun();

        }
    }
}
