package ilargia.entitas.codeGeneration.plugins.dataProviders.components.providers;

import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.util.ArrayList;
import java.util.List;


public class MemberDataProvider implements IComponentDataProvider {

    public static String COMPONENT_MEMBER_INFOS = "component_memberInfos";
    public static String COMPONENT_FLAG_INFOS = "component_flag";

    public static boolean isFlagComponent(ComponentData data) {
        return (boolean) data.get(COMPONENT_FLAG_INFOS);
    }

    public static void setFlagComponent(ComponentData data, boolean flag) {
        data.put(COMPONENT_FLAG_INFOS, flag);
    }

    public static List<FieldSource<JavaClassSource>> getMemberData(ComponentData data) {
        return (List<FieldSource<JavaClassSource>>) data.get(COMPONENT_MEMBER_INFOS);
    }

    public static void setMemberData(ComponentData data, List<FieldSource<JavaClassSource>> memberInfos) {
        data.put(COMPONENT_MEMBER_INFOS, memberInfos);
    }

    @Override
    public void provide(ComponentData data) {
        List<FieldSource<JavaClassSource>> fields = data.getSource().getFields();
        if(fields != null && fields.size() > 0) {
            setMemberData(data, data.getSource().getFields());
            setFlagComponent(data,false);
        } else {
            setMemberData(data, new ArrayList<FieldSource<JavaClassSource>>());
            setFlagComponent(data,true);
        }

    }

}
