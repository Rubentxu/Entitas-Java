package ilargia.entitas.codeGeneration.cli.commands;


import ilargia.entitas.codeGeneration.CodeGenerator;
import ilargia.entitas.codeGeneration.CodeGeneratorUtil;

public class Generate extends AbstractCommand {
    @Override
    public String trigger() {
        return "gen";
    }

    @Override
    public String description() {
        return "Generates files based on Entitas.properties";
    }

    @Override
    public String example() {
        return "entitas gen";
    }

    @Override
    public void run(String[] args) {
        System.out.println("Entitas Code Generator version " + 1);

        if (assertProperties()) {
            CodeGenerator codeGenerator = null; //CodeGeneratorUtil.codeGeneratorFromProperties();

            codeGenerator.OnProgress = (title, info, progress) -> {
                int p = (int) (progress * 100);
                System.out.println(String.format("{0}: {1} ({2}%)", title, info, p));
            };
            codeGenerator.generate();
        }
    }
}
