package com.ilargia.games.entitas.codeGenerator.generators;

import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PoolsGeneratorTest {

    static final public int totalComponents = 0;
    private PoolsGenerator generator;
    private String[] poolNames;

    @Before
    public void setUp() throws Exception {
        generator = new PoolsGenerator();
        poolNames = new String[]{"pruebas", "test", "core"};


    }

    @Test
    public void componentSize() {
        List<JavaClassSource> result = generator.generate(poolNames,"com.pruebas.entitas");

        System.out.println(Roaster.format(result.get(0).toString()));
        //assertEquals(3, IComponent.getComponentSize());


    }

}
