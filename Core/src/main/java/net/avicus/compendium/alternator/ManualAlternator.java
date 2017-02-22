package net.avicus.compendium.alternator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Alternates between a set of objects by calling {@link #next()}.
 */
public class ManualAlternator<T> implements Alternator<T> {
    private final List<T> items;
    private int next;

    public ManualAlternator(Collection<T> items) {
        this.items = new ArrayList<>(items);
        this.next = 0;
    }

    public ManualAlternator(T... items) {
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
