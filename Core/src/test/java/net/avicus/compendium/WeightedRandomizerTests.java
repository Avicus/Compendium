package net.avicus.compendium;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class WeightedRandomizerTests {

  private static final int TRIALS = 100000;

  @Test
  public void weighted() {
    WeightedRandomizer<String> randomizer = new WeightedRandomizer<>();
    randomizer.set("Adam", 0.5);
    randomizer.set("Bill", 0.25);
    randomizer.set("Charlie", 0.125);
    randomizer.set("Frank", 0.0625);
    randomizer.set("Dillon", 0.0625);

    Map<String, Double> weights = new HashMap<>();

    for (int i = 0; i < TRIALS; i++) {
      String next = randomizer.next();
      double weight = weights.getOrDefault(next, 0.0) + (1.0 / (double) TRIALS);
      weights.put(next, weight);
    }

    System.out.println(weights);
  }
}
