package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.IPoolCodeGenerator;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class PoolsGenerator implements IPoolCodeGenerator {

    @Override
    public List<JavaClassSource> generate(Set<String> poolNames, String pkgDestiny) {
        List<JavaClassSource> result = new ArrayList<>();
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, "public class Pools extends com.ilargia.games.entitas.Pools {}");
        javaClass.setPackage(pkgDestiny);
        javaClass.addImport("com.ilargia.games.entitas.Pool");

        createPoolsMethod(javaClass, poolNames);
        createMethodAllPools(javaClass, poolNames);
        createMethodSetAllPools(javaClass, poolNames);
        createPoolFields(javaClass, poolNames);
        result.add(javaClass);
        return result;

    }

    private void createPoolsMethod(JavaClassSource javaClass, Set<String> poolNames) {
        poolNames.forEach((poolName) -> {
            String createMethodName = String.format("create%1$sPool", CodeGenerator.capitalize(poolName));
            String body = String.format("return com.ilargia.games.entitas.Pools.createPool(\"%1$s\", %2$s.totalComponents, %2$s.componentNames(), %2$s.componentTypes());",
                    CodeGenerator.capitalize(poolName), CodeGenerator.capitalize(poolName) + CodeGenerator.DEFAULT_COMPONENT_LOOKUP_TAG);
            javaClass.addMethod()
                    .setPublic()
                    .setStatic(true)
                    .setName(createMethodName)
                    .setReturnType("Pool")
                    .setBody(body);

        });

    }

    private void createMethodAllPools(JavaClassSource javaClass, Set<String> poolNames) {

        String allPoolsList = poolNames.stream().reduce("", (acc, poolName) -> {
            return acc + poolName.toLowerCase() + ", ";
        });

        javaClass.addMethod()
                .setPublic()
                .setName("allPools")
                .setReturnType("Pool[]")
                .setBody(String.format("return new Pool[] { %1$s };", allPoolsList));


    }

    private void createMethodSetAllPools(JavaClassSource javaClass, Set<String> poolNames) {
        String setAllPools = poolNames.stream().reduce("\n", (acc, poolName) ->
                acc + "    " + poolName.toLowerCase() + " = create" + CodeGenerator.capitalize(poolName) + "Pool();\n "
        );

        javaClass.addMethod()
                .setReturnTypeVoid()
                .setPublic()
                .setName("setAllPools")
                .setBody(setAllPools);
    }

    private void createPoolFields(JavaClassSource javaClass, Set<String> poolNames) {
        poolNames.forEach((poolName) -> {
            javaClass.addField()
                    .setName(poolName.toLowerCase())
                    .setType("Pool")
                    .setPublic();

        });

    }


}
