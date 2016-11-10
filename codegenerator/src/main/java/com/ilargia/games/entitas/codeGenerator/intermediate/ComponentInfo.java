package com.ilargia.games.entitas.codeGenerator.intermediate;

import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.Arrays;
import java.util.List;

public class ComponentInfo {

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

    public ComponentInfo(String fullTypeName, String name, List<FieldSource<JavaClassSource>> memberInfos, String[] pools,
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

        String[] nameSplit = fullTypeName.split(".");
        typeName = name;

        isSingletonComponent = memberInfos.size() == 0;
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
