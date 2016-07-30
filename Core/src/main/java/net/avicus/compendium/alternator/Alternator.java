package net.avicus.compendium.alternator;

import java.util.Iterator;

/**
 * Something that alternates between different objects. An infinite iterator.
 * @param <T>
 */
public interface Alternator<T> extends Iterator<T> {
    default boolean hasNext() {
        throw new UnsupportedOperationException("Alternator always has a next value.");
    }

    T next();
}
