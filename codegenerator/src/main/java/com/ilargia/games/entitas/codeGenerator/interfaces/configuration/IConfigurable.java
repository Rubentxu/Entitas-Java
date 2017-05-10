package com.ilargia.games.entitas.codeGenerator.interfaces.configuration;


import java.util.Properties;

public interface IConfigurable {

    Properties getDefaultProperties();

    void configure(Properties properties);

}
