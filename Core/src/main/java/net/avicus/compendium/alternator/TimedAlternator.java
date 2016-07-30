package net.avicus.compendium.alternator;

import java.util.Collection;

/**
 * An alternator that switches to the next item after a period of time.
 * @param <T>
 */
public class TimedAlternator<T> extends ManualAlternator<T> {
    private final long delay;
    private long lastNextTime = 0;
    private T lastNext;

    public TimedAlternator(long delay, Collection<T> items) {
        super(items);
        this.delay = delay;
    }

    public TimedAlternator(long delay, T... items) {
        super(items);
        this.delay = delay;
    }

    @Override
    public T next() {
        long now = System.currentTimeMillis();

        if (now - this.lastNextTime < this.delay)
            return this.lastNext;

        this.lastNextTime = now;

        T next = super.next();
        this.lastNext = next;
        return next;
    }
}
