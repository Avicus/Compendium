package net.avicus.compendium;

import net.avicus.compendium.alternator.ManualAlternator;
import net.avicus.compendium.alternator.TimedAlternator;
import org.junit.Test;

public class TextTests {

  @Test
  public void alternator() {
    ManualAlternator<String> alternator = new ManualAlternator<>(
        "A",
        "B",
        "C",
        "D",
        "E"
    );

    for (int i = 0; i < 8; i++) {
      System.out.println(alternator.next());
    }
  }

  @Test
  public void timedAlternator() {
    TimedAlternator<String> alternator = new TimedAlternator<>(
        1000,
        "A",
        "B",
        "C",
        "D",
        "E"
    );

    while (true) {
      String next = alternator.next();
      System.out.println(next);

      if (next.equals("E")) {
        break;
      }

      try {
        Thread.sleep(250);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
