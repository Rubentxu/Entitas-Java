package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.data.MemberData;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class MemberDataProvider implements IComponentDataProvider {

    public static String COMPONENT_MEMBER_INFOS = "component_memberInfos";

    public static List<MemberData> getMemberData(ComponentData data) {
        return (List<MemberData>) data.get(COMPONENT_MEMBER_INFOS);
    }

    public static void setMemberData(ComponentData data, List<MemberData> memberInfos) {
        data.put(COMPONENT_MEMBER_INFOS, memberInfos);
    }

    @Override
    public void provide(Class type, ComponentData data) {
        List<MemberData> memberDatas = Stream.of(type.getFields())
                .filter(field -> Modifier.isPublic(field.getModifiers()))
                .map(field -> new MemberData(field.getType(), field.getType().getSimpleName(), field.getAnnotations()))
                .collect(Collectors.toList());

        setMemberData(data, memberDatas);
    }

}
