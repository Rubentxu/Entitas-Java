package com.ilargia.games.entitas.codeGenerator.intermediate;

import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;
import org.jboss.forge.roaster.model.source.MethodSource;

import java.util.List;

public class ComponentInfo {

    public String nameComponent;
    public String fullTypeName;
    public List<MethodSource<JavaClassSource>> constructores;
    public List<FieldSource<JavaClassSource>> memberInfos;
    public List<String> internalEnums;
    public List<String> contexts;
    public boolean isSingleEntity;
    public String singleComponentPrefix;
    public boolean generateComponent;
    public boolean generateMethods;
    public boolean generateIndex;
    public boolean hideInBlueprintInspector;
    public String typeName;
    public boolean isSingletonComponent;
    public Integer index;
    public Integer totalComponents;

    public ComponentInfo(String fullTypeName, String typeName, List<FieldSource<JavaClassSource>> memberInfos, List<String> contexts,
                         boolean isSingleEntity, String singleComponentPrefix,
                         boolean generateComponent, boolean generateMethods, boolean generateIndex, boolean hideInBlueprintInspector,
                         List<MethodSource<JavaClassSource>> constructores, List<String> enums) {

        this.fullTypeName = fullTypeName;
        this.memberInfos = memberInfos;
        this.contexts = contexts;
        this.isSingleEntity = isSingleEntity;
        this.singleComponentPrefix = (singleComponentPrefix != null && !singleComponentPrefix.isEmpty()) ? singleComponentPrefix : "";
        this.generateComponent = generateComponent;
        this.generateMethods = generateMethods;
        this.generateIndex = generateIndex;
        this.hideInBlueprintInspector = hideInBlueprintInspector;
        this.typeName = typeName;
        this.nameComponent = (typeName.contains(CodeGenerator.COMPONENT_SUFFIX))
                ? typeName
                : typeName + CodeGenerator.COMPONENT_SUFFIX;

        isSingletonComponent = memberInfos == null || memberInfos.size() == 0;
        this.constructores = constructores;
        this.internalEnums = enums;
        this.index = null;
        this.totalComponents = 0;
    }


    @Override
    public String toString() {
        return "ComponentInfo {" +
                "\n    nameComponent='" + nameComponent + '\'' +
                ",\n    fullTypeName='" + fullTypeName + '\'' +
                ",\n    memberInfos=" + memberInfos +
                ",\n    contexts=" + contexts +
                ",\n    isSingleEntity=" + isSingleEntity +
                ",\n    singleComponentPrefix='" + singleComponentPrefix + '\'' +
                ",\n    generateComponent=" + generateComponent +
                ",\n    generateMethods=" + generateMethods +
                ",\n    generateIndex=" + generateIndex +
                ",\n    hideInBlueprintInspector=" + hideInBlueprintInspector +
                ",\n    typeName='" + typeName + '\'' +
                ",\n    isSingletonComponent=" + isSingletonComponent +
                "\n}";
    }


}
