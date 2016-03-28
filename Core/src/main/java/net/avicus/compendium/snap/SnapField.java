package net.avicus.compendium.snap;

import java.lang.reflect.Field;
import java.util.Optional;

public class SnapField<C, T> {
    private final SnapClass<C> snapClass;
    private final Class<T> fieldType;
    private final String name;

    public SnapField(SnapClass<C> snapClass, Class<T> fieldType, String name) {
        this.snapClass = snapClass;
        this.fieldType = fieldType;
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

    private void set(Optional<Object> instance, T value) throws SnapException {
        Field field = getField();
        try {
            field.set(instance.orElse(null), value);
        } catch (IllegalArgumentException e) {
            throw new SnapException("illegal argument", e);
        } catch (IllegalAccessException e) {
            throw new SnapException("illegal access", e);
        }
    }

    @SuppressWarnings("unchecked")
    private T get(Optional<Object> instance) throws SnapException {
        try {
            return (T) getField().get(instance.orElse(null));
        } catch (IllegalAccessException e) {
            throw new SnapException("illegal access", e);
        } catch (ClassCastException e) {
            throw new SnapException("class cast invalid", e);
        }
    }

    private Field getField() throws SnapException {
        try {
            Field field = this.snapClass.getClazz().getField(this.name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new SnapException("no such field", e);
        }
    }
}
