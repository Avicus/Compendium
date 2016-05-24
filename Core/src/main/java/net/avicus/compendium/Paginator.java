package net.avicus.compendium;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Pagintes a list of items.
 * @param <T>
 */
public class Paginator<T> {
    private List<T> list;
    private int perPage;

    /**
     * @param list The full list of items.
     * @param perPage The number of items per page.
     */
    public Paginator(List<T> list, int perPage) {
        this.list = new ArrayList<>(list);
        this.perPage = perPage;
    }

    /**
     * Sort the list.
     * @param comparator
     */
    public void sort(Comparator<T> comparator) {
        Collections.sort(this.list, comparator);
    }

    /**
     * Set the full list of items.
     * @param list The full list of items.
     */
    public void setList(List<T> list) {
        this.list = new ArrayList<>(list);
    }

    /**
     * Get the full list of items.
     * @return The full list of items.
     */
    public List<T> getList() {
        return this.list;
    }

    /**
     * Set the number of items per page.
     * @param perPage The number of items per page.
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    /**
     * Get the number of items per page.
     * @return The number of items per page.
     */
    public int getPerPage() {
        return this.perPage;
    }

    /**
     * Get the number of pages.
     * @return The number of pages.
     */
    public int getPageCount() {
        if (this.list.isEmpty())
            return 0;
        return (int) Math.ceil((double) this.list.size() / (double) this.perPage);
    }

    /**
     * Get the index of an item.
     * @param item The item.
     * @return The index of the item.
     * @throws IllegalArgumentException If the item is not in the list.
     */
    public int getIndex(T item) throws IllegalArgumentException {
        if (!this.list.contains(item))
            throw new IllegalArgumentException("Item is not in the list.");
        return this.list.indexOf(item);
    }

    /**
     * Get the index of an item.
     * @param item The item.
     * @return The page index of the item.
     * @throws IllegalArgumentException If the item is not in the list.
     */
    public int getPageIndex(T item) throws IllegalArgumentException {
        if (!this.list.contains(item))
            throw new IllegalArgumentException("Item is not in the list.");

        int index = this.list.indexOf(item);
        return index / this.perPage;
    }

    /**
     * Get the page of items that contains the given item.
     * @param item The item.
     * @return The list of items.
     * @throws IllegalArgumentException If the item is not in the list.
     */
    public List<T> getPage(T item) throws IllegalArgumentException {
        int index = getPageIndex(item);
        return getPage(index);
    }

    /**
     * Check if a page exists.
     * @param pageIndex
     * @return
     */
    public boolean hasPage(int pageIndex) {
        return pageIndex >= 0 && pageIndex < getPageCount();
    }

    /**
     * Get the page at the page index.
     * @param pageIndex The index of the page (0 = first)
     * @return The list of items.
     * @throws IllegalArgumentException If the page is invalid.
     */
    public List<T> getPage(int pageIndex) throws IllegalArgumentException {
        if (!hasPage(pageIndex))
            throw new IllegalArgumentException("Invalid page.");

        int from = pageIndex * this.perPage;
        int to = Math.min(this.list.size(), from + this.perPage);

        return this.list.subList(from, to);
    }
}