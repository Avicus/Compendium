package net.avicus.compendium;

import org.junit.Test;

public class TextTests {
    @Test
    public void alternator() {
        Alternator<String> alternator = new Alternator<>(
                "A",
                "B",
                "C",
                "D",
                "E"
        );

        for (int i = 0; i < 8; i++)
            System.out.println(alternator.next());
    }
}
