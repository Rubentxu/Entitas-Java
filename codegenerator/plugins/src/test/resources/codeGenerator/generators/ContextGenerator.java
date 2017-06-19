package ilargia.entitas.codeGenerator.generators;


import ilargia.entitas.codeGenerator.CodeGeneratorOld;
import ilargia.entitas.codeGenerator.data.ComponentInfo;
import ilargia.entitas.codeGenerator.interfaces.IComponentCodeGenerator;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ContextGenerator implements IComponentCodeGenerator {


    @Override
    public List<JavaClassSource> generate(List<ComponentInfo> infos, String pkgDestiny) {
        Map<String, List<ComponentInfo>> mapContextsComponents = CodeGeneratorOld.generateMap(infos);

        List<JavaClassSource> result = new ArrayList<>();

        result.addAll((List) mapContextsComponents.keySet().stream()
                .map(contextName -> generateContext(contextName, mapContextsComponents.get(contextName), pkgDestiny)
                ).collect(Collectors.toList()));

        return result;
    }

    private JavaClassSource generateContext(String contextName, List<ComponentInfo> infos, String pkgDestiny) {
        JavaClassSource contextClass = Roaster.parse(JavaClassSource.class, String.format("public class %1$sContext extends Context<%1$sEntity> {}", contextName));

//        if(infos.size() > 0 && infos.get(0).directory !=null) {
//            pkgDestiny+= "."+infos.get(0).directory;
//
//        }
        if (infos.size() > 0 && !pkgDestiny.endsWith(infos.get(0).subDir)) {
            pkgDestiny += "." + infos.get(0).subDir;

        }
        contextClass.setPackage(pkgDestiny);


        contextClass.addMethod()
                .setName(contextName + "Context")
                .setPublic()
                .setConstructor(true)
                .setParameters(String.format("int totalComponents, int startCreationIndex, ContextInfo contextInfo, EntityBaseFactory<%1$sEntity> factoryMethod", contextName))
                .setBody("super(totalComponents, startCreationIndex, contextInfo, factoryMethod);");
        contextClass.addImport("ilargia.entitas.api.*");


        for (ComponentInfo info : infos) {
            if (info.isSingleEntity) {
                addContextMethods(contextName, info, contextClass);
            }

        }
        return contextClass;
    }

    private void addContextMethods(String contextName, ComponentInfo info, JavaClassSource contextClass) {
        addImports(info.memberInfos, contextClass);
        addContextGetMethods(contextName, info, contextClass);
        addContextHasMethods(contextName, info, contextClass);
        addContextAddMethods(contextName, info, contextClass);
        addContextReplaceMethods(contextName, info, contextClass);
        addContextRemoveMethods(contextName, info, contextClass);
    }

    private void addImports(List<FieldSource<JavaClassSource>> memberInfos, JavaClassSource source) {
        for (FieldSource<JavaClassSource> info : memberInfos) {
            if (info.getOrigin().getImport(info.getType().toString()) != null) {
                if (source.getImport(info.getType().toString()) == null) {
                    source.addImport(info.getType());
                }
            }
        }
    }

    private void addContextGetMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        source.addMethod()
                .setName(String.format("get%1$sEntity", info.typeName))
                .setReturnType(contextName + "Entity")
                .setPublic()
                .setBody(String.format("return getGroup(%1$sMatcher.%2$s()).getSingleEntity();"
                        , CodeGeneratorOld.capitalize(info.contexts.get(0)), info.typeName));

        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("get%1$s", info.typeName))
                    .setReturnType(info.typeName)
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity().get%1$s();"
                            , info.typeName));

        }
    }

    private void addContextHasMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        if (info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("is%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity() != null;",
                            info.typeName));

            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnType(contextName + "Context")
                    .setPublic()
                    .setParameters("boolean value")
                    .setBody(String.format("%2$sEntity entity = get%1$sEntity();\n" +
                            "        if(value != (entity != null)) {\n" +
                            "             if(value) {\n" +
                            "                  createEntity().set%1$s(true);\n" +
                            "             } else {\n" +
                            "                  destroyEntity(entity);\n" +
                            "             }\n" +
                            "        }\n return this;", info.typeName, contextName));


        } else {
            source.addMethod()
                    .setName(String.format("has%1$s", info.typeName))
                    .setReturnType("boolean")
                    .setPublic()
                    .setBody(String.format("return get%1$sEntity() != null; ",
                            info.typeName));

        }
    }

    private void addContextAddMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("set%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(info.constructores != null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos))
                    .setBody(String.format("if(has%1$s()) {\n" +
                                    "            throw new EntitasException(\"Could not set %1$s!\" + this + \" already has an entity with %1$s!\", " +
                                    "\"You should check if the context already has a %1$sEntity before setting it or use context.Replace%1$s().\");" +
                                    "         }\n" +
                                    "         %3$sEntity entity = createEntity();\n" +
                                    "         entity.add%1$s(%2$s);\n" +
                                    "         return entity;"
                            , info.typeName,
                            info.constructores != null && info.constructores.size() > 0
                                    ? memberNamesFromConstructor(info.constructores.get(0))
                                    : memberNames(info.memberInfos)
                            , contextName));

            source.addImport(info.fullTypeName);

        }
    }

    private void addContextReplaceMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("replace%1$s", info.typeName))
                    .setReturnType(contextName + "Entity")
                    .setPublic()
                    .setParameters(info.constructores != null && info.constructores.size() > 0
                            ? memberNamesWithTypeFromConstructor(info.constructores.get(0))
                            : memberNamesWithType(info.memberInfos))
                    .setBody(String.format("%3$sEntity entity = get%1$sEntity();" +
                                    "         if(entity == null) {" +
                                    "            entity = set%1$s(%2$s);" +
                                    "         } else { " +
                                    "           entity.replace%1$s(%2$s);" +
                                    "         }" +
                                    "         return entity;"
                            , info.typeName, info.constructores != null && info.constructores.size() > 0
                                    ? memberNamesFromConstructor(info.constructores.get(0))
                                    : memberNames(info.memberInfos)
                            , contextName));


        }
    }

    private void addContextRemoveMethods(String contextName, ComponentInfo info, JavaClassSource source) {
        if (!info.isSingletonComponent) {
            source.addMethod()
                    .setName(String.format("remove%1$s", info.typeName))
                    .setReturnType(contextName + "Context")
                    .setPublic()
                    .setBody(String.format("destroyEntity(get%1$sEntity()); return this;"
                            , info.typeName, memberNames(info.memberInfos)));

        }

    }

    public String memberNamesWithType(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> info.getType() + " " + info.getName())
                .collect(Collectors.joining(", "));


    }

    public String memberNames(List<FieldSource<JavaClassSource>> memberInfos) {
        return memberInfos.stream()
                .map(info -> info.getName())
                .collect(Collectors.joining(", "));
    }

    public String memberNamesFromConstructor(MethodSource<JavaClassSource> constructor) {
        return constructor.getParameters().stream()
                .map(info -> info.getName())
                .collect(Collectors.joining(", "));
    }

    public String memberNamesWithTypeFromConstructor(MethodSource<JavaClassSource> constructor) {
        return constructor.getParameters().stream()
                .map(info -> info.toString())
                .collect(Collectors.joining(", "));
    }


}
