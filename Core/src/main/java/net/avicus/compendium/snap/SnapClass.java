package net.avicus.compendium.snap;

import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SnapClass<T> implements Annotationable {
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

    public SnapConstructor<T> getConstructor(Class<?>... argumentTypes)  {
        return new SnapConstructor<>(this, argumentTypes);
    }

    public SnapMethod<T, ?> getMethod(String name, Class<?>[] argumentTypes) {
        return new SnapMethod<>(this, Object.class, name, argumentTypes);
    }

    public <V> SnapMethod<T, V> getMethod(String name, Class<V> value, Class<?>[] argumentTypes) {
        return new SnapMethod<>(this, value, name, argumentTypes);
    }

    public SnapField<T, ?> getField(String name) {
        return new SnapField<>(this, Object.class, name);
    }

    public <V> SnapField<T, V> getField(String name, Class<V> value) {
        return new SnapField<>(this, value, name);
    }

    public List<SnapField> getFields() {
        return getFields(this.clazz.getDeclaredFields());
    }

    public List<SnapField> getPublicFields() {
        return getFields(this.clazz.getFields());
    }

    public List<SnapClass<?>> getClasses() {
        return getClasses(this.clazz.getDeclaredClasses());
    }

    public List<SnapClass<?>> getPublicClasses() {
        return getClasses(this.clazz.getClasses());
    }

    @Override
    public List<Annotation> getAnnotations() {
        return Arrays.asList(this.clazz.getDeclaredAnnotations());
    }

    private List<SnapClass<?>> getClasses(Class[] classesArray) {
        List<SnapClass<?>> classes = new ArrayList<>();
        for (Class<?> clazz : classesArray)
            classes.add(new SnapClass<>(clazz));
        return classes;
    }

    private List<SnapField> getFields(Field[] fieldsArray) {
        List<SnapField> fields = new ArrayList<>();
        for (Field field : fieldsArray)
            fields.add(new SnapField<>(this, field.getType(), field.getName()));
        return fields;
    }
}
