package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SearchSelect<T, Cell> {

    JFXListView<Cell> resultsList;
    JFXTextField searchBar;

    List<T> selectedItems;
    List<T> items;

    Consumer<T> onClickItem = null;
    Consumer<Cell> onMakeCheckbox = null;

    public interface Matcher<T> {
        boolean isMatching(T item, String text);
    }
    Matcher<T> matcher;

    public interface CellCreator<T, Cell> {
        Cell makeCell(T item, boolean selected);
    }
    CellCreator<T, Cell> cellCreator;

    public interface ItemSorter<T> {
        List<T> sort(List<T> itemList);
    }
    ItemSorter<T> itemSorter = null;

    /**
     * An object that handles searching and selecting multiple items.
     * Give it your search input, and a list to display the search results.
     * You may get the selected items whenever you like.
     * @param searchBar a JFXTextField object that you wish to act as the search bar.
     * @param searchResultsList a JFXListView<JFXCheckBox> object that will list the search results and allow selection.
     * @param matcher a function that takes an item (of the defined type) and a string,
 *                    and returns true if the string, or any other filters in the scope of the definition, match the item.
     * @param cellCreator a function that defines the list items.
     *                    It must take an item, and return the list cell.
     *                    The returned cell's type must be the same as the cells of the ListView you are using.
     */
    public SearchSelect(JFXTextField searchBar,
                        JFXListView<Cell> searchResultsList,
                        Matcher<T> matcher,
                        CellCreator<T, Cell> cellCreator) {

        this.resultsList = searchResultsList;
        this.searchBar = searchBar;

        this.selectedItems = new LinkedList<>();
        this.items = new LinkedList<>();

        this.matcher = matcher;
        this.cellCreator = cellCreator;

        this.searchBar.setOnKeyTyped(event -> updateFromSearchBar());
    }

    public void setItemSorter(ItemSorter<T> itemSorter) {
        this.itemSorter = itemSorter;
    }

    /**
     * Load the search engine with nodes to search for.
     * Side effect: Displays the nodes in the search results window.
     * @param items List of T objects to be searched and selected.
     */
    public void setItems(List<T> items) {
        updateMatchingItems(items);
        this.items = items;
    }

    public List<T> getSelectedItems() {
        return selectedItems;
    }

    public void clearSelectedItems() {
        selectedItems = new LinkedList<>();
        updateFromSearchBar();
    }

    private void updateFromSearchBar() {
        String searchText = searchBar.getText();
        List<T> matchingItems = filterMatchingItems(searchText, items);
        updateMatchingItems(matchingItems);
    }

    private List<T> filterMatchingItems(String text, List<T> items) {
        if (matcher == null) {
            throw new InvalidParameterException("Matcher has not been set");
        }

        return items.stream()
                    .filter((T item) -> matcher.isMatching(item, text))
                    .collect(Collectors.toList());
    }

    private void updateMatchingItems(List<T> matchingItems) {
        if (cellCreator == null) {
            throw new InvalidParameterException("Cell creator has not been set");
        }

        resultsList.getItems().removeAll(resultsList.getItems());

        for (T item : selectedItems) {
            Cell cell = cellCreator.makeCell(item, true);
            resultsList.getItems().add(cell);
        }

        if (itemSorter != null) {
            matchingItems = itemSorter.sort(matchingItems);
        }

        for (T item : matchingItems) {
            Cell cell = cellCreator.makeCell(item, false);
            resultsList.getItems().add(cell);
        }

    }

    public void selectItem(T item) {
        selectedItems.add(item);
    }

    public void deSelectItem(T item) {
        selectedItems.remove(item);
    }

    public void setMatcher(Matcher<T> matcher) {
        this.matcher = matcher;
    }

    public void setCellCreator(CellCreator<T, Cell> cellCreator) {
        this.cellCreator = cellCreator;
    }
}
