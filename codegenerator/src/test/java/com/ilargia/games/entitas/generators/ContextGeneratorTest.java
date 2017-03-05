package com.ilargia.games.entitas.generators;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ContextGeneratorTest {

    static final public int totalComponents = 0;
    private ContextGenerator generator;
    private List<String> contextNames;

    @Before
    public void setUp() throws Exception {
        generator = new ContextGenerator();
        contextNames = new ArrayList<>();
        contextNames.add("pruebas");
        contextNames.add("test");
        contextNames.add("core");

    }

    @Test
    public void componentSize() {
        //List<JavaClassSource> result = generator.generate(contextNames, "com.pruebas.entitas");

        //System.out.println(Roaster.format(result.get(0).toString()));
        //assertEquals(3, IComponent.getComponentSize());


    }

}
