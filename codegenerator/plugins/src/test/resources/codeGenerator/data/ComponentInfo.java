package ilargia.entitas.codeGenerator.data;

import ilargia.entitas.codeGenerator.CodeGeneratorOld;
import org.jboss.forge.roaster.model.source.*;

import java.util.List;

public class ComponentInfo {

    public String nameComponent;
    public String fullTypeName;
    public List<MethodSource<JavaClassSource>> constructores;
    public List<FieldSource<JavaClassSource>> memberInfos;
    public List<String> internalEnums;
    public List<String> contexts;
    public List<TypeVariableSource<JavaClassSource>> generics;
    public List<Import> imports;
    public boolean isSingleEntity;
    public String singleComponentPrefix;
    public boolean generateComponent;
    public boolean generateMethods;
    public boolean generateIndex;
    public boolean hideInBlueprintInspector;
    public String typeName;
    public boolean isSingletonComponent;
    public Integer index;
    public String subDir;
    public Integer totalComponents;


    public ComponentInfo(String fullTypeName, String typeName, List<FieldSource<JavaClassSource>> memberInfos, List<String> contexts,
                         boolean isSingleEntity, String singleComponentPrefix,
                         boolean generateComponent, boolean generateMethods, boolean generateIndex, boolean hideInBlueprintInspector,
                         List<MethodSource<JavaClassSource>> constructores, List<String> enums, List<Import> imports, String subDir, List<TypeVariableSource<JavaClassSource>> generics) {

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
        this.nameComponent = (typeName.contains(CodeGeneratorOld.COMPONENT_SUFFIX))
                ? typeName
                : typeName + CodeGeneratorOld.COMPONENT_SUFFIX;

        isSingletonComponent = memberInfos == null || memberInfos.size() == 0;
        this.constructores = constructores;
        this.internalEnums = enums;
        this.imports = imports;
        this.index = null;
        this.totalComponents = 0;
        this.subDir = subDir;
        this.generics = generics;

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
                ",\n    subDIr=" + subDir +
                "\n}";
    }


}
