package ilargia.entitas.codeGeneration.plugins.cli.commands;


public class EditConfig extends AbstractCommand {
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

            // System.diagnostics.Process.Start(Preferences.PATH_PROPERTIES);
        }
    }
}
