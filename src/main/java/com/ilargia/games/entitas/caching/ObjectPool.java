package com.ilargia.games.entitas.caching;

import com.ilargia.games.entitas.AbstractEntityIndex;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObjectPool<T> {
    Function<Class<T>,T> _factoryMethod;
    Consumer<T> _resetMethod;
    Stack<T> _pool;

    public ObjectPool() {

    }

    public ObjectPool(Function<Class<T>,T> factoryMethod, Consumer<T> resetMethod) {
        _factoryMethod = factoryMethod;
        _resetMethod = resetMethod;
        _pool = new Stack<T>();
    }


    public T get(Class<T> clazz) {
        return _pool.size() == 0
                ? _factoryMethod.apply(clazz)
                : _pool.pop();
    }

    public void push(T obj) {
        if(_resetMethod != null) {
            _resetMethod.accept(obj);
        }
        _pool.push(obj);
    }
}


