package com.ilargia.games.entitas.codeGenerator.generators;

import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContextGeneratorTest {

    static final public int totalComponents = 0;
    private ContextGenerator generator;
    private Set<String> poolNames;

    @Before
    public void setUp() throws Exception {
        generator = new ContextGenerator();
        poolNames = new HashSet<>();
        poolNames.add("pruebas");
        poolNames.add("test");
        poolNames.add("core");

    }

    @Test
    public void componentSize() {
        List<JavaClassSource> result = generator.generate(poolNames, "com.pruebas.entitas");

        System.out.println(Roaster.format(result.get(0).toString()));
        //assertEquals(3, IComponent.getComponentSize());


    }

}
