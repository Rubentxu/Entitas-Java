package ilargia.entitas.codeGeneration.interfaces;


public interface ICommand {

    String trigger();

    String description();

    String example();

    void run(String[] args);

}
