package ilargia.entitas.gradle;


public class CodeGenerationPluginExtension {
    private String configFile = "Entitas.properties";

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }
}
