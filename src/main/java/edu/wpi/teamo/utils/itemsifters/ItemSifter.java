package edu.wpi.teamo.utils.itemsifters;

import edu.wpi.teamo.utils.itemsifters.displays.IDisplay;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The ItemSifter is a machine that holds items, filters, comparators and displays.
 *
 * Filters are functions that take an item, and return true if its passes.
 * See filter examples in StringSearcher, ItemSifterTests, and the ISift javadoc.
 *
 * Comparators are functions that take two items, and compare them. See java.util.Comparator.
 * The comparator is used to sort the items. If not provided, the items are not sorted.
 *
 * Displays are functions that take a stream of items after they have been filtered and sorted.
 * It is up to you how you want to display the items, but see ListViewDisplay for a good example.
 *
 * When the sifter updates, it runs all the items through its filters,
 * sorts the filtered items with its comparator (optional),
 * and sends the items to all displays that are bound to it.
 *
 * update(): filter -> sort -> display
 *
 * Please email me at nllopez@wpi.edu to help me improve this so that it is more useful/understandable
 */
public class ItemSifter<T> {

    private Comparator<T> comparator = null;
    private List<IDisplay<T>> displays;
    private List<T> items;
    private Sift<T> sift;

    public ItemSifter() {
        displays = new LinkedList<>();
        items = new LinkedList<>();
        sift = new Sift<>();
    }

    public void update() {
        Stream<T> processedItems = processItems();
        displayItems(processedItems);
    }

    public Stream<T> processItems() {
        Stream<T> itemStream = items.stream();

        itemStream = siftItems(itemStream);

        itemStream = sortItems(itemStream);

        return itemStream;
    }

    private Stream<T> siftItems(Stream<T> items) {
        if (sift != null) {
            items = sift.sift(items);
        }
        return items;
    }

    public Stream<T> sortItems(Stream<T> items) {
        List<T> itemList = items.collect(Collectors.toList());
        if (comparator != null) itemList.sort(comparator);
        return itemList.stream();
    }

    private void displayItems(Stream<T> items) {
        List<T> itemList = items.collect(Collectors.toList());
        for (IDisplay<T> display : displays) {
            display.update(itemList.stream());
        }
    }

    public void addDisplay(IDisplay<T> display) {
        displays.add(display);
    }

    public void removeDisplay(IDisplay<T> display) {
        displays.remove(display);
    }

    public void addHardFilter(Sift.Filter<T> filter) {
        sift.addHardFilter(filter);
    }

    public void addSoftFilter(Sift.Filter<T> filter) {
        sift.addSoftFilter(filter);
    }

    public void clearHardFilters() {
        sift.clearHardFilters();
    }

    public void clearSoftFilters() {
        sift.clearSoftFilters();
    }

    public void removeHardFilter(Sift.Filter<T> filter) {
        sift.removeHardFilter(filter);
    }

    public void removeSoftFilter(Sift.Filter<T> filter) {
        sift.removeSoftFilter(filter);
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /** Add an item to the sifter. The item will be processed the next time you call update().
     * @param item The item to add.
     */
    public void addItem(T item) {
        items.add(item);
    }

    public void addItems(Stream<T> itemStream) {
        itemStream.forEach(this::addItem);
    }

    public Stream<T> getItems() {
        return items.stream();
    }

    public void clearItems() {
        items.clear();
    }

    public void removeItem(T item) {
        items.remove(item);
    }
}


