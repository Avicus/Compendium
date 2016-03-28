package net.avicus.compendium.snap;

public class SnapUtils {
    public static Class<?>[] objectsToTypes(Object... objects) {
        Class<?>[] types = new Class[objects.length];
        for (int i = 0; i < objects.length; i++)
            types[i] = objects[i].getClass();
        return types;
    }
}
