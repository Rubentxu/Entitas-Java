package com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.providers;

import com.ilargia.games.entitas.codeGeneration.plugins.data.MemberData;
import com.ilargia.games.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.List;
import java.util.stream.Collectors;


public class MemberDataComponentDataProvider implements IComponentDataProvider {

    public static String COMPONENT_MEMBER_INFOS = "component_memberInfos";

    @Override
    public void provide(JavaClassSource type, ComponentData data) {
       List<MemberData> memberDatas = type.getFields().stream()
                .filter(field -> field.isPublic())
                .map(field -> new MemberData(field.getType(), field.getName()))
                .collect(Collectors.toList());

        setMemberData(data, memberDatas);
    }

    public static  List<MemberData> getMemberData(ComponentData data) {
        return (List<MemberData>) data.get(COMPONENT_MEMBER_INFOS);
    }

    public static void setMemberData(ComponentData data, List<MemberData> memberInfos) {
        data.put(COMPONENT_MEMBER_INFOS, memberInfos);
    }
}
