package com.ilargia.games.entitas.provider;


import com.ilargia.games.entitas.intermediate.ComponentInfo;
import com.ilargia.games.entitas.providers.TypeReflectionProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class TypeReflectionProviderTest {

    private TypeReflectionProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new TypeReflectionProvider("src/test/java/com/ilargia/games/entitas/codeGenerator/component");

    }

    @Test
    public void componentSize() throws IOException {
        List<ComponentInfo> result = provider.componentInfos();

        for (ComponentInfo info : result) {
            System.out.println(info);
        }
        //assertEquals(3, IComponent.getComponentSize());

    }

}
