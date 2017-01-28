package com.ilargia.games.systems;

import com.badlogic.gdx.utils.Array;
import com.ilargia.games.entitas.Group;
import com.ilargia.games.entitas.interfaces.*;
import com.ilargia.games.entitas.matcher.TriggerOnEvent;


public class RemoveViewSystem implements ISetPool<Pool>, IReactiveSystem<Entity>, IEnsureComponents {


    @Override
    public void setPool(Pool pool) {
        pool.getGroup(CoreMatcher.View()).OnEntityRemoved = RemoveViewSystem::onEntityRemoved;
    }

    @Override
    public IMatcher getEnsureComponents() {
        return CoreMatcher.View();
    }

    @Override
    public TriggerOnEvent getTrigger() {
        return CoreMatcher.Asset().OnEntityRemoved();
    }

    @Override
    public void execute(Array<Entity> entities) {
        for(Entity e : entities) {
            e.removeView();
        }
    }

    public static void onEntityRemoved(Group<Entity> group, Entity entity, int index, IComponent component) {
        View viewComponent = (View) component;
//        var gameObject = viewComponent.gameObject;
//        var spriteRenderer = gameObject.GetComponent<SpriteRenderer>();
//        var color = spriteRenderer.color;
//        color.a = 0f;
//        spriteRenderer.material.DOColor(color, 0.2f);
//        gameObject.transform
//                .DOScale(Vector3.one * 1.5f, 0.2f)
//                .OnComplete(() => {
//                gameObject.Unlink();
//        Object.Destroy(gameObject);
//                    });
    }

}
