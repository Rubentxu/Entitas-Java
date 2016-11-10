package com.ilargia.games.entitas.codeGenerator.provider;


import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import com.ilargia.games.entitas.codeGenerator.providers.TypeReflectionProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class TypeReflectionProviderTest {

    private TypeReflectionProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new TypeReflectionProvider(null,null,"src/test/java/com/ilargia/games/entitas/components");




    }

    @Test
    public void componentSize() throws IOException {
        ComponentInfo[] result = provider.componentInfos();

        System.out.println(result[0]);
        System.out.println(result[0].typeName);
        //assertEquals(3, IComponent.getComponentSize());


    }

}
