package com.ilargia.games.systems;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.ilargia.games.EntityIndexExtension;
import com.ilargia.games.components.Input;
import com.ilargia.games.components.Position;
import com.ilargia.games.core.*;
import com.ilargia.games.entitas.interfaces.IReactiveSystem;
import com.ilargia.games.entitas.interfaces.ISetPool;
import com.ilargia.games.entitas.interfaces.ISetPools;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class AddViewSystem implements ISetPool<Pool>, IReactiveSystem<Entity> {

    private Pool _pool;

    @Override
    public void setPool(Pool pool) {
        _pool = pool;
    }

    @Override
    public TriggerOnEvent getTrigger() {
        return CoreMatcher.Asset().OnEntityAdded();
    }

    @Override
    public void execute(Array<Entity> entities) {
        for(Entity e : entities) {
//            var res = Resources.Load<GameObject>(e.getAsset().name);
//            GameObject gameObject = null;
//            try {
//                gameObject = UnityEngine.Object.Instantiate(res);
//            } catch(Exception) {
//                Debug.Log("Cannot instantiate " + res);
//            }
//
//            if(gameObject != null) {
//                gameObject.transform.SetParent(_viewContainer, false);
//                e.addView(gameObject);
//                gameObject.Link(e, _pool);
//
//                if(e.hasPosition()) {
//                    Position pos = e.getPosition();
//                    gameObject.transform.position = new Vector3(pos.x, pos.y + 1, 0f);
//                }
//            }
        }
    }
}
