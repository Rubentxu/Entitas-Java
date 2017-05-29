package ilargia.entitas.gradle;


public class CodeGenerationPluginExtension {
    private String configCodeGen = "Entitas.properties";

    public String getConfigCodeGen() {
        return configCodeGen;
    }

    public void setConfigCodeGen(String configCodeGen) {
        this.configCodeGen = configCodeGen;
    }
}
