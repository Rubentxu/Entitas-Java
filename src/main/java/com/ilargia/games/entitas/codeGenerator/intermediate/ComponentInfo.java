package com.ilargia.games.entitas.codeGenerator.intermediate;

import java.util.List;

public class ComponentInfo {

    public String fullTypeName;
    public List<PublicMemberInfo> memberInfos;
    public String[] pools;
    public boolean isSingleEntity;
    public String singleComponentPrefix;
    public boolean generateComponent;
    public boolean generateMethods;
    public boolean generateIndex;
    public boolean hideInBlueprintInspector;

    public String typeName;
    public boolean isSingletonComponent;

    public ComponentInfo(String fullTypeName, List<PublicMemberInfo> memberInfos, String[] pools,
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
        typeName = nameSplit[nameSplit.length - 1];

        isSingletonComponent = memberInfos.size() == 0;
    }
}
