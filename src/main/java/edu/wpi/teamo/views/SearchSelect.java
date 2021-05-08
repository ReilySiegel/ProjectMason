package edu.wpi.teamo.views;

import edu.wpi.teamo.utils.itemsifters.displays.SelectionListDisplay;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.utils.itemsifters.*;
import java.util.Comparator;
import java.util.List;


public class SearchSelect<T, Cell> {
    private boolean enabled = true;

    private final ItemSifter<T> itemSifter;
    private final SelectionListDisplay<T, Cell> display;
    private final JFXTextField searchBar;

    public interface Matcher<T> extends ItemSearch.TextMatcher<T> {
        boolean isMatching(T item, String text);
    }
    Sift.Filter<T> textMatchFilter; /*keep a reference so we can reset it and keep any other hard filters*/

    /**
     * An object that handles searching and selecting multiple items.
     * Give it your search input, and a list to display the search results.
     * You may get the selected items whenever you like.
     * @param searchBar a JFXTextField object that you wish to act as the search bar.
     * @param searchResultsList a JFXListView<JFXCheckBox> object that will list the search results and allow selection.
     * @param matcher a function that takes an item (of the defined type) and a string,
 *                    and returns true if the string, or any other filters in the scope of the definition, match the item.
     * @param selectionCellCreator a function that defines the list items.
     *                    It must take an item, and a boolean representing if it is selected, and return the list cell.
     *                    The returned cell's type must be the same as the cells of the ListView you are using.
     */
    public SearchSelect(JFXTextField searchBar,
                        JFXListView<Cell> searchResultsList,
                        Matcher<T> matcher,
                        SelectionListDisplay.SelectionCellCreator<T, Cell> selectionCellCreator) {
        itemSifter = new ItemSifter<>();
        setMatcher(matcher);

        this.searchBar = searchBar;

        display = new SelectionListDisplay<>(searchResultsList, selectionCellCreator);
        itemSifter.addDisplay(display);

        searchBar.setOnKeyTyped(event -> update());
    }

    public void setComparator(Comparator<T> comparator) {
        this.itemSifter.setComparator(comparator);
    }

    /**
     * Load the search engine with nodes to search for.
     * Side effect: Displays the nodes in the search results window.
     * @param items List of T objects to be searched and selected.
     */
    public void setItems(List<T> items) {
        itemSifter.clearItems();
        itemSifter.addItems(items.stream());
        update();
    }

    public List<T> getSelectedItems() {
        return display.getSelectedItems();
    }

    public void clearSelectedItems() {
        display.clearSelectedItems();
        update();
    }

    public void update() {
        if (!enabled) return;
        itemSifter.update();
    }

    public void selectItem(T item) {
        display.selectItem(item);
    }

    public void deSelectItem(T item) {
        display.deSelectItem(item);
    }

    public void setMatcher(Matcher<T> matcher) {
        itemSifter.removeHardFilter(textMatchFilter);
        this.textMatchFilter = (item) -> matcher.isMatching(item, searchBar.getText());
        itemSifter.addHardFilter(textMatchFilter);
    }

    public void addHardFilter(Sift.Filter<T> filter) {
        itemSifter.addHardFilter(filter);
    }

    public void addSoftFilter(Sift.Filter<T> filter) {
        itemSifter.addSoftFilter(filter);
    }

    public void setCellCreator(SelectionListDisplay.SelectionCellCreator<T, Cell> cellCreator) {
        display.setSelectionCellCreator(cellCreator);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
