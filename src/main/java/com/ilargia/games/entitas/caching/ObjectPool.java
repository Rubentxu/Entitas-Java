package com.ilargia.games.entitas.caching;

import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObjectPool<T> {
    public interface Factory<T> {
        T create();
    }
    private Factory<T> _factoryMethod;
    private Consumer<T> _resetMethod;
    private Stack<T> _pool;

    public ObjectPool() {

    }

    public ObjectPool(Factory<T> factoryMethod, Consumer<T> resetMethod) {
        _factoryMethod = factoryMethod;
        _resetMethod = resetMethod;
        _pool = new Stack<T>();
    }


    public T get() {
        return _pool.size() == 0
                ? _factoryMethod.create()
                : _pool.pop();
    }

    public void push(T obj) {
        if (_resetMethod != null) {
            _resetMethod.accept(obj);
        }
        _pool.push(obj);
    }

    public void reset() {
        _pool.clear();
    }
}


