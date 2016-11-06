package com.ilargia.games.entitas.codeGenerator.generators;

import com.ilargia.games.entitas.Pool;
import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IPoolCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;


public class PoolsGenerator implements IPoolCodeGenerator {

    @Override
    public CodeGenFile[] generate(String[] poolNames) {

        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, "public class Pools extends com.ilargia.games.entitas.Pools {}");

        createPoolsMethod(javaClass, poolNames);
        createMethodAllPools(javaClass, poolNames);
        createMethodSetAllPools(javaClass, poolNames);
        createPoolFields(javaClass, poolNames);

        return new CodeGenFile[]{new CodeGenFile(
                "Pools",
                javaClass,
                "PoolsGenerator"
        )};

    }

    private void createPoolsMethod(JavaClassSource javaClass, String[] poolNames) {
        Arrays.stream(poolNames).forEach((poolName) -> {
            String createMethodName = String.format("create%1$sPool", capitalize(poolName));
            String body = String.format("return super.createPool(\"%1$s\", %2$s.totalComponents, %2$s.componentNames, %2$s.componentTypes);",
                    capitalize(poolName), capitalize(poolName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG);
            javaClass.addMethod()
                    .setPublic()
                    .setStatic(true)
                    .setName(createMethodName)
                    .setReturnType(Pool.class)
                    .setBody(body);

        });

    }

    private void createMethodAllPools(JavaClassSource javaClass, String[] poolNames) {

        String allPoolsList = Arrays.stream(poolNames).reduce("", (acc, poolName) -> {
            return acc + poolName.toLowerCase() + ", ";
        });

        javaClass.addMethod()
                .setPublic()
                .setName("allPools")
                .setReturnType("Pool[]")
                .setBody(String.format("return new Pool[] { %1$s };", allPoolsList));


    }

    private void createMethodSetAllPools(JavaClassSource javaClass, String[] poolNames) {
        String setAllPools = Arrays.stream(poolNames).reduce("\n", (acc, poolName) ->
                acc + "    " + poolName.toLowerCase() + " = create" + capitalize(poolName) + "Pool();\n "
        );

        javaClass.addMethod()
                .setReturnTypeVoid()
                .setPublic()
                .setName("setAllPools")
                .setBody(setAllPools);
    }

    private void createPoolFields(JavaClassSource javaClass, String[] poolNames) {
        Arrays.stream(poolNames).forEach((poolName) -> {
            javaClass.addField()
                    .setName(poolName.toLowerCase())
                    .setType(Pool.class)
                    .setPublic();

        });

    }

    private String capitalize(final String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i] == '.' || chars[i] == '\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
