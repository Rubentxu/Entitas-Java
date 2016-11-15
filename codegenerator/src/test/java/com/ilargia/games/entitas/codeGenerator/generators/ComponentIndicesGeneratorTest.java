package com.ilargia.games.entitas.codeGenerator.generators;

import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.intermediate.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ComponentIndicesGeneratorTest {

    static final public int totalComponents = 0;
    private ComponentIndicesGenerator generator;
    private List<String> poolNames;

    @Before
    public void setUp() throws Exception {
        generator = new ComponentIndicesGenerator();
        poolNames = new ArrayList<String>();
        poolNames.add("pruebas");
        poolNames.add("coreTest");

    }

    @Test
    public void generateComponentInfos() {
        List<ComponentInfo> componentInfos = new ArrayList<>();
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        final FieldSource<JavaClassSource> field = javaClass.addField("public byte content1[], content2;");
        final FieldSource<JavaClassSource> field2 = javaClass.addField("public String content3;");
        memberInfos.add(field);
        memberInfos.add(field2);

        componentInfos.add(new ComponentInfo("com.ilargia.games.entitas.components.Position","Position", memberInfos, poolNames,
               false, "", false, false, false, false));
        componentInfos.add(new ComponentInfo("com.ilargia.games.entitas.components.Movable","Movable", memberInfos, poolNames,
                false, "", false, false, false, false));
        componentInfos.add(new ComponentInfo("com.pruebas.Movable","Movable", memberInfos,poolNames,
                false, "", false, false, false, false));
        List<JavaClassSource> result = generator.generate(componentInfos,"com.pruebas.entitas");

        assertEquals(2, result.size());

    }


}
