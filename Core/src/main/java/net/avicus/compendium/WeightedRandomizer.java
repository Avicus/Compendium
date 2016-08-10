package net.avicus.compendium;

import lombok.ToString;

import java.util.*;

/**
 * A handy utility for selecting weighted objects randomly.
 * @param <T>
 */
public class WeightedRandomizer<T> {
    private static final Random RANDOM = new Random();

    private final Random random;
    private final Map<T, Double> items;

    /**
     * Constructor. Uses provided random.
     * @param random The random generator.
     */
    public WeightedRandomizer(Random random) {
        this.random = random;
        this.items = new HashMap<>();
    }

    /**
     * Constructor. Uses standard random.
     */
    public WeightedRandomizer() {
        this(RANDOM);
    }

    /**
     * Select the next random item.
     * @return The next item.
     * @throws NoSuchElementException
     */
    public T next() throws NoSuchElementException {
        if (this.items.isEmpty())
            throw new NoSuchElementException();

        // Generate total value
        double total = 0;
        for (Double weight : this.items.values())
            total += weight;

        // Generate association: item -> their chance of getting selected
        Map<T, Double> distribution = new HashMap<>();
        for (T item : this.items.keySet()) {
            double weight = this.items.get(item);
            distribution.put(item, weight / total);
        }

        // Generate association: range [x ,x + probability] -> item
        Map<Range, T> intervals = new HashMap<>();
        double lastInterval = 0;

        for (T item : distribution.keySet()) {
            double max = lastInterval + distribution.get(item);
            intervals.put(new Range(lastInterval, max), item);
            lastInterval = max;
        }

        double random = this.random.nextDouble();

        for (Range range : intervals.keySet())
            if (range.contains(random))
                return intervals.get(range);

        throw new NoSuchElementException();
    }

    /**
     * Set the weight of an item.
     * @param item
     * @param weight Any value - it is only relative to all the other items you set.
     */
    public void set(T item, double weight) {
        this.items.put(item, weight);
    }

    /**
     * Remove an existing item from the randomizer.
     * @param item
     */
    public void remove(T item) {
        this.items.remove(item);
    }

    /**
     * Clear this randomizer.
     */
    public void clear() {
        this.items.clear();
    }

    /**
     * A range (double of min and max).
     */
    @ToString
    private static class Range {
        private final double min;
        private final double max;

        public Range(double min, double max) {
            this.min = min;
            this.max = max;
        }

        /**
         * Check if this range contains a value.
         * @param value
         * @return
         */
        public boolean contains(double value) {
            return value >= this.min && value < this.max;
        }
    }
}
