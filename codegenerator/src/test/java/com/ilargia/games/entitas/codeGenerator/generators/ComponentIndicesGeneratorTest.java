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

public class ComponentIndicesGeneratorTest {

    static final public int totalComponents = 0;
    private ComponentIndicesGenerator generator;


    @Before
    public void setUp() throws Exception {
        generator = new ComponentIndicesGenerator();


    }

    private List<ComponentInfo> createComponentInfos(JavaClassSource javaClass) {
        List<ComponentInfo> componentInfos = new ArrayList<>();

        componentInfos.add(createPlayer(javaClass));
        componentInfos.add(createBounds(javaClass));
        componentInfos.add(createBall(javaClass));
        componentInfos.add(createView(javaClass));
        componentInfos.add(createMotion(javaClass));
        componentInfos.add(createScore(javaClass));

        return componentInfos;
    }

    private ComponentInfo createPlayer(JavaClassSource javaClass) {
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        memberInfos.add(javaClass.addField("public ID id;"));

        List<String> poolNames = new ArrayList<String>();
        poolNames.add("Otro");

        return new ComponentInfo("com.ilargia.games.entitas.codeGenerator.components.Player", "Player", memberInfos, poolNames,
                false, "", true, true, false, false, null, null, null,"");
    }

    private ComponentInfo createBounds(JavaClassSource javaClass) {
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        memberInfos.add(javaClass.addField("public Tag tag;"));
        memberInfos.add(javaClass.addField("public Rectangle rectangle;"));

        List<String> poolNames = new ArrayList<String>();
        poolNames.add("Core");


        return new ComponentInfo("com.ilargia.games.entitas.codeGenerator.components.Bounds", "Bounds", memberInfos, poolNames,
                false, "", true, true, false, false, null, null, null,"");
    }

    private ComponentInfo createView(JavaClassSource javaClass) {
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        memberInfos.add(javaClass.addField("public Shape2D shape;"));

        List<String> poolNames = new ArrayList<String>();
        poolNames.add("Core");
        poolNames.add("Otro");

        return new ComponentInfo("com.ilargia.games.entitas.codeGenerator.components.View", "View", memberInfos, poolNames,
                false, "", true, true, false, false, null, null, null,"");
    }

    private ComponentInfo createBall(JavaClassSource javaClass) {
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        memberInfos.add(javaClass.addField("public boolean resetBall;"));

        List<String> poolNames = new ArrayList<String>();
        poolNames.add("Core");
        poolNames.add("Otro");

        return new ComponentInfo("com.ilargia.games.entitas.codeGenerator.components.Ball", "Ball", memberInfos, poolNames,
                false, "", true, true, false, false, null, null, null,"");
    }

    private ComponentInfo createMotion(JavaClassSource javaClass) {
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        memberInfos.add(javaClass.addField("public Vector2 velocity;"));

        List<String> poolNames = new ArrayList<String>();
        poolNames.add("Core");

        return new ComponentInfo("com.ilargia.games.entitas.codeGenerator.components.Motion", "Motion", memberInfos, poolNames,
                false, "", true, true, false, false, null, null, null,"" );
    }

    private ComponentInfo createScore(JavaClassSource javaClass) {
        List<FieldSource<JavaClassSource>> memberInfos = new ArrayList<>();
        memberInfos.add(javaClass.addField("public int value;"));

        List<String> poolNames = new ArrayList<String>();
        poolNames.add("Core");

        return new ComponentInfo("com.ilargia.games.entitas.codeGenerator.components.Score", "Score", memberInfos, poolNames,
                false, "", true, true, false, false, null, null, null,"");
    }

    @Test
    public void generateComponentInfos() {

        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        List<ComponentInfo> componentInfos = createComponentInfos(javaClass);

        List<JavaClassSource> result = generator.generate(componentInfos, "com.pruebas.entitas");

        assertEquals(2, result.size());

    }


}
