package net.avicus.compendium;

import net.avicus.compendium.snap.SnapClass;
import net.avicus.compendium.snap.SnapField;
import org.junit.Test;

public class SnapTests {
    @Test
    public void user() {
        SnapClass clazz = new SnapClass("net.avicus.compendium.SnapUser");
        Object instance = clazz.getConstructor(int.class, String.class).newInstance(1, "keenan");

        System.out.println("Constructed: " + clazz.getMethod("toString").get(instance));

        SnapField id = clazz.getField("id");
        SnapField name = clazz.getField("name");

        assert id.get(instance).equals(1);
        assert name.get(instance).equals("keenan");

        id.set(instance, 5);
        name.set(instance, "bill");

        assert id.get(instance).equals(5);
        assert name.get(instance).equals("bill");

        System.out.println("Result: " + instance);
    }
}
