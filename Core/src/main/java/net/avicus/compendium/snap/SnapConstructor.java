package net.avicus.compendium.snap;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class SnapConstructor<T> {
    private final SnapClass<T> snapClass;
    private final Class[] argumentTypes;

    public SnapConstructor(SnapClass<T> snapClass, Class[] argumentTypes) {
        this.snapClass = snapClass;
        this.argumentTypes = argumentTypes;
        getConstructor(); // ensures this is a valid constructor
    }

    public T newInstance(Object... arguments) {
        if (this.argumentTypes.length != arguments.length)
            throw new SnapException("constructor expected " + argumentTypes.length + " args but provided " + arguments.length);

        Constructor<T> constructor = getConstructor();
        try {
            return constructor.newInstance(arguments);
        } catch (InstantiationException e) {
            throw new SnapException("failed to instantiate", e);
        } catch (IllegalAccessException e) {
            throw new SnapException("illegal access", e);
        } catch (InvocationTargetException e) {
            throw new SnapException("invocation failure", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Constructor<T> getConstructor() {
        try {
            return (Constructor<T>) snapClass.getClazz().getConstructor(this.argumentTypes);
        } catch (NoSuchMethodException e) {
            throw new SnapException("no constructor for " + Arrays.toString(this.argumentTypes), e);
        }
    }
}
