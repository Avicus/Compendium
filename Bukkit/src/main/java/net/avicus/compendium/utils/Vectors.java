package net.avicus.compendium.utils;

import org.bukkit.util.Vector;

public class Vectors {
    public static boolean isLess(Vector a, Vector b) {
        return a.getX() < b.getX() &&
                a.getY() < b.getY() &&
                a.getZ() < b.getZ();
    }

    public static boolean isGreater(Vector a, Vector b) {
        return a.getX() > b.getX() &&
                a.getY() > b.getY() &&
                a.getZ() > b.getZ();
    }
}
