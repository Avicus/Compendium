package net.avicus.compendium.number;

public interface NumberComparator {
    NumberComparator EQUALS = (a, b) -> a == b;

    NumberComparator LESS_THAN = (a, b) -> a < b;

    NumberComparator LESS_THAN_EQUAL = (a, b) -> a <= b;

    NumberComparator GREATER_THAN = (a, b) -> a > b;

    NumberComparator GREATER_THAN_EQUAL = (a, b) -> a >= b;

    static NumberComparator MODULO(int value) {
        return new ModuloNumberComparator(value);
    }

    /**
     * Perform the comparator on two values
     *
     * @param a
     * @param b
     * @return True if the comparator passes.
     */
    boolean perform(double a, double b);
}
