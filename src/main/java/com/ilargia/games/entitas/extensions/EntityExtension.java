package com.ilargia.games.entitas.extensions;


import com.ilargia.games.entitas.Entity;
import com.ilargia.games.entitas.interfaces.IComponent;

import java.lang.reflect.Type;

public final class EntityExtension {

    private static String COMPONENT_SUFFIX = "Component";

    public static String addComponentSuffix(String componentName) {
        return componentName.endsWith(COMPONENT_SUFFIX)
                ? componentName
                : componentName + COMPONENT_SUFFIX;
    }

    public static String RemoveComponentSuffix(String componentName) {
        return componentName.endsWith(COMPONENT_SUFFIX)
                ? componentName.substring(0, componentName.length() - COMPONENT_SUFFIX.length())
                : componentName;
    }

    /// Adds copies of all specified components to the target entity.
    /// If replaceExisting is true it will replace exisintg components.
    public static void copyTo(Entity entity, Entity target, boolean replaceExisting, Integer[] indices) throws InstantiationException, IllegalAccessException {
//        Integer[] componentIndices = indices.length == 0
//                ? entity.getComponentIndices()
//                : indices;
//        for(int i = 0; i < componentIndices.length; i++) {
//            Integer index = componentIndices[i];
//            IComponent component = entity.getComponent(index);
//            Type type = component.getClass().getGenericSuperclass();
//            IComponent clonedComponent = target.createComponent(index);
//            component.copyPublicMemberValues(clonedComponent);
//
//            if(replaceExisting) {
//                target.replaceComponent(index, (IComponent) clonedComponent);
//            } else {
//                target.addComponent(index, (IComponent) clonedComponent);
//            }
//        }
    }
}

