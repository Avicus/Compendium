package net.avicus.compendium.snap;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class SnapMethod<C,T> {
    @Getter private final SnapClass<C> snapClass;
    @Getter private final Class<T> fieldType;
    @Getter private final String name;
    @Getter private final Class<?>[] argumentTypes;

    public SnapMethod(SnapClass<C> snapClass, Class<T> methodType, String name, Class<?>... argumentTypes) {
        this.snapClass = snapClass;
        this.fieldType = methodType;
        this.name = name;
        this.argumentTypes = argumentTypes;
    }

    public T get(Object instance, Object... args) throws SnapException {
        return get(Optional.of(instance), args);
    }

    public T get(Object... args) throws SnapException {
        return get(Optional.empty(), args);
    }

    @SuppressWarnings("unchecked")
    private T get(Optional<Object> instance, Object... args) throws SnapException {
        try {
            return (T) getMethod(args).invoke(instance.orElse(null), args);
        } catch (IllegalAccessException e) {
            throw new SnapException("illegal access", e);
        } catch (ClassCastException e) {
            throw new SnapException("class cast invalid", e);
        } catch (InvocationTargetException e) {
            throw new SnapException("invocation error", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Method getMethod(Object[] args) throws SnapException {
        try {
            Method method = this.snapClass.getClazz().getMethod(this.name, this.argumentTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new SnapException("no such field", e);
        }
    }
}
