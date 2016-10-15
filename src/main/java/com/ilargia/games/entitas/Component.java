package com.ilargia.games.entitas;

import com.badlogic.gdx.utils.ObjectMap;

public class Component {
    private static int nextTypeId = -1;
    static ObjectMap<Class<? extends Component>,Integer> IDS = new ObjectMap<>();


    public static int getIdComponent(Class<? extends Component> component) {
       if(!IDS.containsKey(component)) {
         IDS.put(component, ++nextTypeId);
       }
        return IDS.get(component);
    }

}