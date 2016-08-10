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
     * @param items The items to weight association.
     */
    public WeightedRandomizer(Random random, Map<T, Double> items) {
        this.random = random;
        this.items = items;
    }

    /**
     * Constructor. Uses provided random.
     * @param random The random generator.
     */
    public WeightedRandomizer(Random random) {
        this(random, new HashMap<>());
    }

    /**
     * Constructor. Uses standard random.
     * @param items The items to weight association.
     */
    public WeightedRandomizer(Map<T, Double> items) {
        this(RANDOM, items);
    }

    /**
     * Constructor. Uses standard random.
     */
    public WeightedRandomizer() {
        this(RANDOM);
    }

    /**
     * Get the likelihood of picking a specific item.
     * @param item The item.
     * @return The probability in [0, 1].
     * @throws NoSuchElementException
     */
    public double getLikelihood(T item) throws NoSuchElementException {
        Double weight = this.items.get(item);
        if (weight == null)
            throw new NoSuchElementException();

        return weight / totalWeight();
    }

    /**
     * Generate the total weight.
     * @return
     */
    private double totalWeight() {
        double total = 0;
        for (Double weight : this.items.values())
            total += weight;
        return total;
    }

    /**
     * Select a number of next items randomly.
     * @return The next items.
     * @throws NoSuchElementException
     */
    public List<T> next(int count) throws NoSuchElementException {
        if (this.items.isEmpty())
            throw new NoSuchElementException();

        // Generate total value
        double total = totalWeight();

        // Generate association: item -> their chance of getting selected
        Map<T, Double> distribution = new HashMap<>();
        for (T item : this.items.keySet()) {
            double weight = this.items.get(item);
            distribution.put(item, weight / total);
        }

        // Generate association: range [x ,x + probability] -> item
        Map<Range, T> ranges = new HashMap<>();
        double lastInterval = 0;

        for (T item : distribution.keySet()) {
            double max = lastInterval + distribution.get(item);
            ranges.put(new Range(lastInterval, max), item);
            lastInterval = max;
        }

        List<T> items = new ArrayList<>();

        while (items.size() < count) {
            double random = this.random.nextDouble();

            for (Range range : ranges.keySet())
                if (range.contains(random))
                    items.add(ranges.get(range));
        }

        return items;
    }

    /**
     * Select the single next random item.
     * @return The next item.
     * @throws NoSuchElementException
     */
    public T next() throws NoSuchElementException {
        return next(1).get(0);
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
