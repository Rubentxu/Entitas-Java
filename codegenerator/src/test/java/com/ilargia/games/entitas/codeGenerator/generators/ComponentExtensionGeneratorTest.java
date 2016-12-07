package com.ilargia.games.entitas.codeGenerator.generators;

import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ComponentExtensionGeneratorTest {

    private ComponentExtensionsGenerator generator;
    private List<String> poolNames;

    @Before
    public void setUp() throws Exception {
        generator = new ComponentExtensionsGenerator();
        poolNames = new ArrayList<String>();
        poolNames.add("pruebas");
        poolNames.add("core");

    }

    @Test
    public void generateComponentInfos() {
        List<ComponentInfo> componentInfos = new ArrayList();
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        List<FieldSource<JavaClassSource>> memberInfos2 = new ArrayList<>();
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        final FieldSource<JavaClassSource> field = javaClass.addField("public float x;");
        final FieldSource<JavaClassSource> field2 = javaClass.addField("public float y;");
        final FieldSource<JavaClassSource> field3 = javaClass.addField("public boolean isMovable;");
        memberInfos.add(field);
        memberInfos.add(field2);
        memberInfos2.add(field3);


        componentInfos.add(new ComponentInfo("com.ilargia.games.entitas.components.Position", "Position", memberInfos, poolNames,
                false, "", true, true, false, false,null,null));
        componentInfos.add(new ComponentInfo("com.ilargia.games.entitas.components.Movable", "Movable", memberInfos2, poolNames,
                true, "", true, true, false, false,null,null));

        List<JavaClassSource> result = generator.generate(componentInfos, "com.pruebas.entitas");

        assertEquals(4, result.size());

    }

}
