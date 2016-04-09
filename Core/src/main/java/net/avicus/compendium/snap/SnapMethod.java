package net.avicus.compendium.snap;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SnapMethod<C,T extends Object> implements Annotationable {
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

    public T getStatic(Object... args) throws SnapException {
        return get(Optional.empty(), args);
    }

    @Override
    public List<Annotation> getAnnotations() {
        return Arrays.asList(getMethod().getDeclaredAnnotations());
    }

    @SuppressWarnings("unchecked")
    private T get(Optional<Object> instance, Object... args) throws SnapException {
        try {
            return (T) getMethod().invoke(instance.orElse(null), args);
        } catch (IllegalAccessException e) {
            throw new SnapException("illegal access", e);
        } catch (ClassCastException e) {
            throw new SnapException("class cast invalid", e);
        } catch (InvocationTargetException e) {
            throw new SnapException("invocation error", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Method getMethod() throws SnapException {
        try {
            Method method = this.snapClass.getClazz().getMethod(this.name, this.argumentTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            throw new SnapException("no such field", e);
        }
    }
}
