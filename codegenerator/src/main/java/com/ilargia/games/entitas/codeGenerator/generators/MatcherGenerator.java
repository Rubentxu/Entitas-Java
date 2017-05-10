package com.ilargia.games.entitas.codeGenerator.generators;


import com.ilargia.games.entitas.codeGenerator.CodeGeneratorOld;
import com.ilargia.games.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.data.ComponentInfo;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MatcherGenerator implements IComponentCodeGenerator {


    @Override
    public List<JavaClassSource> generate(List<ComponentInfo> infos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapContextsComponents = CodeGeneratorOld.generateMap(infos);
        List<JavaClassSource> result = new ArrayList<>();

        result.addAll((List) mapContextsComponents.keySet().stream()
                .map(contextName -> generateMatchers(contextName, mapContextsComponents.get(contextName), pkgDestiny)
                ).collect(Collectors.toList()));

        return result;
    }


    private JavaClassSource generateMatchers(String contextName, List<ComponentInfo> componentInfos, String pkgDestiny) {
        JavaClassSource javaClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$s {}",
                CodeGeneratorOld.capitalize(contextName) + "Matcher"));
        if(componentInfos.size() > 0 && !pkgDestiny.endsWith(componentInfos.get(0).subDir)) {
            pkgDestiny+= "."+componentInfos.get(0).subDir;

        }

        javaClass.setPackage(pkgDestiny);
        //javaClass.addImport("com.ilargia.games.entitas.interfaces.IMatcher");
        javaClass.addImport("com.ilargia.games.entitas.matcher.Matcher");

        for (ComponentInfo info : componentInfos) {
            addMatcher(contextName, info, javaClass);
            addMatcherMethods(contextName, info, javaClass);
        }
        System.out.println(javaClass);
        return javaClass;
    }

    private JavaClassSource addMatcher(String contextName, ComponentInfo info, JavaClassSource javaClass) {
        javaClass.addField()
                .setName("_matcher" + info.typeName)
                .setType("Matcher")
                .setPrivate()
                .setStatic(true);
        return null;
    }

    private void addMatcherMethods(String contextName, ComponentInfo info, JavaClassSource javaClass) {
        String body = "if (_matcher%2$s == null) {" +
                "   Matcher matcher = (Matcher)Matcher.AllOf(%1$s.%2$s);" +
                "   matcher.componentNames = %1$s.componentNames();" +
                "   _matcher%2$s = matcher;" +
                "}" +
                "return _matcher%2$s;";

        javaClass.addMethod()
                .setName(info.typeName)
                .setReturnType("Matcher")
                .setPublic()
                .setStatic(true)
                .setBody(String.format(body, CodeGeneratorOld.capitalize(contextName) + CodeGeneratorOld.DEFAULT_COMPONENT_LOOKUP_TAG,
                        info.typeName));

    }

}
