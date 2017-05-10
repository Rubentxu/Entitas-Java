package com.ilargia.games.entitas.codeGeneration.plugins.data;


public class MethodData {

    public String returnType;
    public String methodName;
    public MemberData[] parameters;

    public MethodData(String returnType, String methodName, MemberData[] parameters) {
        this.returnType = returnType;
        this.methodName = methodName;
        this.parameters = parameters;
    }
}
