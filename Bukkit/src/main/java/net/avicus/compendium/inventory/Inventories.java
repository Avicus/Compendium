package net.avicus.compendium.inventory;

/**
 * Utility class used to get the number of rows of an inventory based on slot size.
 */
public class Inventories {

    private static final int ROW_SIZE = 9;

    private Inventories() {
    }

    public static int rowCount(int size) {
        return (size + ROW_SIZE - 1) / ROW_SIZE;
    }
}
