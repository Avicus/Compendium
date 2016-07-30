package net.avicus.compendium;

import java.util.*;

/**
 * Alternates between a set of objects.
 */
public class Alternator<T> {
    private final List<T> items;
    private int next;

    public Alternator(Collection<T> items) {
        this.items = new ArrayList<>(items);
        this.next = 0;
    }

    public Alternator(T... items) {
        this(Arrays.asList(items));
    }

    public T next() {
        if (this.items.isEmpty())
            throw new NoSuchElementException("No items in the collection.");

        T item = this.items.get(this.next);

        this.next++;
        if (this.next >= this.items.size())
            this.next = 0;

        return item;
    }
}
