package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components;

import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;
import com.ilargia.games.entitas.codeGeneration.plugins.data.MemberData;

import java.util.List;


public class ComponentData extends CodeGeneratorData {


    private String fullTypeName;
    private List<MemberData> memberData;

    public static String toComponentName(String fullTypeName, boolean ignoreNamespaces) {
        return ignoreNamespaces
                ? fullTypeName.split(",")[fullTypeName.split(",").length]
                : fullTypeName.replaceAll(".","/");
    }

    public String getFullTypeName() {
        return fullTypeName;
    }

    public List<MemberData> getMemberData() {
        return memberData;
    }

    public void setFullTypeName(String fullTypeName) {
        this.fullTypeName = fullTypeName;
    }

    public void setMemberData(List<MemberData> memberData) {
        this.memberData = memberData;
    }
}
