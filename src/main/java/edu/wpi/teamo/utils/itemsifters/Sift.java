package edu.wpi.teamo.utils.itemsifters;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * This is a class that holds hard and soft filters, and runs them on item streams.
 * An item must pass all hard filters to make it through.
 * An item must pass at least one soft filter to make it through.
 * If no filters, all items make it through.
 *
 * A filter is a function that the sifter will run on every item.
 * It takes an item, and returns true if the item passes the filter.
 * Example:
 *      private JFXCheckBox showValidItems;
 *      Filter<Item> validFilter = (item) -> {
 *          boolean showValidChecked = showValidItems.isSelected();
 *          boolean itemIsValid = item.isValid()
 *          boolean passesFilter = showValidChecked && itemIsValid;
 *          return passesFilter;
 *      }
 * The main idea here is that the filter is defined in the scope of any inputs needed, such as a checkbox,
 * allowing the sifter to refer to those inputs for each item whenever it updates.
 * @param <T> The type of item being filtered.
 */
public class Sift<T> {

    public interface Filter<T> {
        boolean filter(T item);
    }

    private List<Filter<T>> softFilters;
    private List<Filter<T>> hardFilters;

    public Sift() {
        softFilters = new LinkedList<>();
        hardFilters = new LinkedList<>();
    }

    public Stream<T> sift(Stream<T> items) {

        Stream<T> filtered = items.filter(this::applyHardFilters);

        filtered = filtered.filter(this::applySoftFilters);

        return filtered;
    }

    /**
     * Returns true if item passes ALL hard filters.
     * This is a filter on its own. If no hard filters, always true.
     */
    private boolean applyHardFilters(T item) {
        if (hardFilters.isEmpty()) return true;
        return hardFilters.stream().allMatch((filter) -> filter.filter(item));
    }

    /**
     * Returns true if item passes ANY soft filters.
     * This is a filter on its own. If no soft filters, always true.
     */
    private boolean applySoftFilters(T item) {
        if (softFilters.isEmpty()) return true;
        return softFilters.stream().anyMatch((filter) -> filter.filter(item));
    }

    public void addHardFilter(Filter<T> filter) {
        hardFilters.add(filter);
    }

    public void addSoftFilter(Filter<T> filter) {
        softFilters.add(filter);
    }

    public void removeHardFilter(Filter<T> filter) {
        hardFilters.remove(filter);
    }

    public void removeSoftFilter(Filter<T> filter) {
        softFilters.remove(filter);
    }

    public void clearHardFilters() {
        hardFilters.clear();
    };

    public void clearSoftFilters() {
        softFilters.clear();
    };
}
