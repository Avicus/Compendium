package net.avicus.compendium.snap;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;

public class SnapClass<T> {
    @Getter private final Class<? extends T> clazz;

    public SnapClass(Class<? extends T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public SnapClass(String forName) {
        try {
            this.clazz = (Class<T>) Class.forName(forName);
        } catch (ClassNotFoundException e) {
            throw new SnapException("class not found", e);
        }
    }

    public T create(Object... arguments)  {
        try {
            return this.clazz.getConstructor(SnapUtils.objectsToTypes(arguments)).newInstance(arguments);
        } catch (InstantiationException e) {
            throw new SnapException("failed to instantiate", e);
        } catch (IllegalAccessException e) {
            throw new SnapException("illegal access", e);
        } catch (InvocationTargetException e) {
            throw new SnapException("invocation error", e);
        } catch (NoSuchMethodException e) {
            throw new SnapException("constructor not found", e);
        }
    }

    public SnapMethod<T, ?> getMethod(String name, Class<?>[] argumentTypes) {
        return new SnapMethod<T, Object>(this, Object.class, name, argumentTypes);
    }

    public <V> SnapMethod<T, V> getMethod(String name, Class<V> value, Class<?>[] argumentTypes) {
        return new SnapMethod<T, V>(this, value, name, argumentTypes);
    }

    public SnapField<T, ?> getField(String name) {
        return new SnapField<T, Object>(this, Object.class, name);
    }

    public <V> SnapField<T, V> getField(String name, Class<V> value) {
        return new SnapField<T, V>(this, value, name);
    }
}
