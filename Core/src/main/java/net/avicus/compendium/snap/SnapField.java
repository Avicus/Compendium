package net.avicus.compendium.snap;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SnapField<C, T> implements Annotationable {
    @Getter private final SnapClass<C> snapClass;
    @Getter private final String name;

    public SnapField(SnapClass<C> snapClass, Class<T> fieldType, String name) {
        this.snapClass = snapClass;
        this.name = name;
    }

    public void set(Object instance, T value) throws SnapException {
        set(Optional.of(instance), value);
    }

    public void set(T value) throws SnapException {
        set(Optional.empty(), value);
    }

    public T get(Object instance) throws SnapException {
        return get(Optional.of(instance));
    }

    public T get() throws SnapException {
        return get(Optional.empty());
    }

    @SuppressWarnings("unchecked")
    public Class<T> getFieldType() {
        return (Class<T>) getField().getType();
    }

    @Override
    public List<Annotation> getAnnotations() {
        return Arrays.asList(getField().getDeclaredAnnotations());
    }

    private void set(Optional<Object> instance, T value) throws SnapException {
        Field field = getField();
        try {
            field.set(instance.orElse(null), value);
        } catch (IllegalArgumentException e) {
            throw new SnapException("Illegal argument: '" + this.name + "'.", e);
        } catch (IllegalAccessException e) {
            throw new SnapException("Illegal access: '" + this.name + "'.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private T get(Optional<Object> instance) throws SnapException {
        try {
            return (T) getField().get(instance.orElse(null));
        } catch (IllegalAccessException e) {
            throw new SnapException("Illegal access: '" + this.name + "'.", e);
        } catch (ClassCastException e) {
            throw new SnapException("Class cast invalid: '" + this.name + "'.", e);
        }
    }

    private Field getField() throws SnapException {
        try {
            Field field = this.snapClass.getClazz().getDeclaredField(this.name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new SnapException("No such field: '" + this.name + "'.", e);
        }
    }
}
