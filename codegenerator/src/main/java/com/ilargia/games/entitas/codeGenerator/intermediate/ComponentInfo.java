package com.ilargia.games.entitas.codeGenerator.intermediate;

import com.ilargia.games.entitas.codeGenerator.CodeGenerator;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;
import java.util.List;

public class ComponentInfo {

    public String nameComponent;
    public String fullTypeName;
    public List<FieldSource<JavaClassSource>> memberInfos;
    public String[] pools;
    public boolean isSingleEntity;
    public String singleComponentPrefix;
    public boolean generateComponent;
    public boolean generateMethods;
    public boolean generateIndex;
    public boolean hideInBlueprintInspector;

    public String typeName;
    public boolean isSingletonComponent;

    public ComponentInfo(String fullTypeName, String typeName, List<FieldSource<JavaClassSource>> memberInfos, String[] pools,
                         boolean isSingleEntity, String singleComponentPrefix,
                         boolean generateComponent, boolean generateMethods, boolean generateIndex, boolean hideInBlueprintInspector) {

        this.fullTypeName = fullTypeName;
        this.memberInfos = memberInfos;
        this.pools = pools;
        this.isSingleEntity = isSingleEntity;
        this.singleComponentPrefix = singleComponentPrefix;
        this.generateComponent = generateComponent;
        this.generateMethods = generateMethods;
        this.generateIndex = generateIndex;
        this.hideInBlueprintInspector = hideInBlueprintInspector;
        this.typeName = capitalize(typeName);
        this.nameComponent = (typeName.contains(CodeGenerator.COMPONENT_SUFFIX))
                                ? typeName.toLowerCase()
                                :typeName.toLowerCase()+ CodeGenerator.COMPONENT_SUFFIX;

        isSingletonComponent = memberInfos.size() == 0;
    }

    private String capitalize(final String String) {
        char[] chars = String.toLowerCase().toCharArray();
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

    @Override
    public String toString() {
        return "ComponentInfo{" +
                "fullTypeName='" + fullTypeName + '\'' +
                ", memberInfos=" + memberInfos +
                ", pools=" + Arrays.toString(pools) +
                ", isSingleEntity=" + isSingleEntity +
                ", singleComponentPrefix='" + singleComponentPrefix + '\'' +
                ", generateComponent=" + generateComponent +
                ", generateMethods=" + generateMethods +
                ", generateIndex=" + generateIndex +
                ", hideInBlueprintInspector=" + hideInBlueprintInspector +
                ", typeName='" + typeName + '\'' +
                ", isSingletonComponent=" + isSingletonComponent +
                '}';
    }
}
