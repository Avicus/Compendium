package net.avicus.compendium.snap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

public class SnapConstructor<T> implements Annotationable {
    private final SnapClass<T> snapClass;
    private final Class[] argumentTypes;

    public SnapConstructor(SnapClass<T> snapClass, Class[] argumentTypes) {
        this.snapClass = snapClass;
        this.argumentTypes = argumentTypes;
        getConstructor(); // ensures this is a valid constructor
    }

    public T newInstance(Object... arguments) {
        if (this.argumentTypes.length != arguments.length)
            throw new SnapException("Constructor expected " + argumentTypes.length + " arguments but provided " + arguments.length + ".");

        Constructor<T> constructor = getConstructor();
        try {
            return constructor.newInstance(arguments);
        } catch (InstantiationException e) {
            throw new SnapException("Failed to instantiate.", e);
        } catch (IllegalAccessException e) {
            throw new SnapException("Illegal constructor access.", e);
        } catch (InvocationTargetException e) {
            throw new SnapException("Constructor invocation failed.", e);
        }
    }

    @Override
    public List<Annotation> getAnnotations() {
        return Arrays.asList(getConstructor().getAnnotations());
    }

    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {
        try {
            return (Constructor<T>) snapClass.getClazz().getConstructor(this.argumentTypes);
        } catch (NoSuchMethodException e) {
            throw new SnapException("No constructor for " + Arrays.toString(this.argumentTypes) + " found.", e);
        }
    }
}
