package ilargia.entitas.codeGeneration.interfaces;


import java.util.Properties;

public interface IConfigurable {

    Properties getDefaultProperties();

    void configure(Properties properties);

}
