package ilargia.entitas.codeGeneration.interfaces;


public interface ICodeGeneratorInterface {

    String getName();

    Integer gePriority();

    boolean isEnableByDefault();

    boolean runInDryMode();

}
