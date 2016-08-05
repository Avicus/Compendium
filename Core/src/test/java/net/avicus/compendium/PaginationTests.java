package net.avicus.compendium;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaginationTests {
    @Test
    public void big() {
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 15; i++)
            ints.add(i);

        Paginator<Integer> paginator = new Paginator<>(ints, 5);

        assert paginator.hasPage(0);
        assert paginator.hasPage(2);
        assert paginator.hasPage(paginator.getPageCount() - 1);

        System.out.println(paginator.getPage(0));
        System.out.println(paginator.getPage(1));
        System.out.println(paginator.getPage(2));
    }

    @Test
    public void small() {
        Paginator<Integer> paginator = new Paginator<>(Arrays.asList(1), 5);

        assert paginator.hasPage(0);

        System.out.println(paginator.getPage(0));
    }
}
