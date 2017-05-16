package com.ilargia.games.entitas.codeGeneration.cli.commands;


import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGeneration.codeGenerator.CodeGeneratorUtil;

public class DryRun extends AbstractCommand{
    @Override
    public String trigger() {
        return "dry";
    }

    @Override
    public String description() {
        return "Simulates generating files without writing to disk";
    }

    @Override
    public String example() {
        return "entitas dry";
    }

    @Override
    public void run(String[] args) {
        System.out.println("Entitas Code Generator version " + 1);

        if (assertProperties()) {
            CodeGenerator codeGenerator = CodeGeneratorUtil.codeGeneratorFromProperties();

            codeGenerator.OnProgress = (title, info, progress) -> {
                int p = (int)(progress * 100);
                System.out.println(String.format("{0}: {1} ({2}%)", title, info, p));
            };

            codeGenerator.dryRun();
        }
    }
}
