package edu.wpi.teamo.utils.itemsifters;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.utils.itemsifters.displays.IDisplay;
import edu.wpi.teamo.utils.itemsifters.displays.ListViewDisplay;

import java.util.Comparator;
import java.util.stream.Stream;


public class ItemSearch<Item, ListCell> {

    private final ItemSifter<Item> itemSifter;

    /**
     * This is a function that will be called every time the ItemSearch updates. The machine will run this function
     * on every item, to determine if the item matches your text. It takes an item, and the search text.
     * Here is an example: (person, searchText) -> return person.getName().contains(searchText)
     * This will return true if the text contains part of their name. This example is case sensitive!
     */
    public interface TextMatcher<T> {
        boolean isMatching(T item, String text);
    }

    /**
     * This is a function that will be called for every item that passes the filters and is ready to be displayed.
     * It takes in the item, and returns a javaFX item to be put into the list view.
     * Example: (item) -> return new Label(item);
     */
    public interface CellCreator<T, Cell> {
        Cell makeCell(T item);
    }

    /**
     * An object that handles searching and displaying items.
     * Give it your search input, and a list to display the search results.
     * @param searchBar a JFXTextField object that you wish to act as the search bar.
     * @param searchResultsList a JFXListView<Item> object that will list the search results and allow selection.
     * @param matcher This is a function that will be called every time the ItemSearch updates. The machine will run this function
     *                on every item, to determine if the item matches your text. It takes an item, and the search text.
     *                Here is an example: (person, searchText) -> return person.getName().contains(searchText)
     *                This will return true if the text contains part of their name. This example is case sensitive!
     * @param cellCreator a function that defines the list items.
     *                    It must take an item, and return the list cell.
     *                    The returned cell's type must be the same as the cells of the ListView you are using.
     */
    public ItemSearch(JFXTextField searchBar,
                      JFXListView<ListCell> searchResultsList,
                      TextMatcher<Item> matcher,
                      CellCreator<Item, ListCell> cellCreator) {

        /*
         * Make an ItemSifter object. This is the core of the API.
         * Add a hard filter to the sifter that gets the text from the search bar,
         * and uses the text with your matching function to filter each item.
         */
        itemSifter = new ItemSifter<>();
        itemSifter.addHardFilter(item -> matcher.isMatching(item, searchBar.getText()));

        /*
         * Create a list view display object, which takes the cell creator.
         * This object will receive items from the itemSifter when it updates, and display them.
         */
        IDisplay<Item> display = new ListViewDisplay<>(searchResultsList, cellCreator::makeCell);
        itemSifter.addDisplay(display);

        searchBar.setOnKeyTyped(event -> update());
    }

    public ItemSearch(JFXTextField searchBar,
                      JFXListView<ListCell> searchResultsList,
                      TextMatcher<Item> matcher,
                      CellCreator<Item, ListCell> cellCreator,
                      ListViewDisplay.HeaderCellCreator<ListCell> headerCellCreator) {

        itemSifter = new ItemSifter<>();
        itemSifter.addHardFilter(item -> matcher.isMatching(item, searchBar.getText()));

        ListViewDisplay<Item, ListCell> display = new ListViewDisplay<>(searchResultsList, cellCreator::makeCell);
        display.setHeaderCellCreator(headerCellCreator);
        itemSifter.addDisplay(display);

        searchBar.setOnKeyTyped(event -> update());
    }

    public void setComparator(Comparator<Item> comparator) {
        itemSifter.setComparator(comparator);
    }

    public void addItems(Stream<Item> items) {
        items.forEach(this::addItem);
    }

    public void addHardFilter(Sift.Filter<Item> filter) {
        itemSifter.addHardFilter(filter);
    }

    public void addSoftFilter(Sift.Filter<Item> filter) {
        itemSifter.addSoftFilter(filter);
    }

    public void removeItem(Item item) {
        itemSifter.removeItem(item);
    }

    public void addItem(Item item) {
        itemSifter.addItem(item);
    }

    public void clearItems() {
        itemSifter.clearItems();
    }

    public void update() {
        itemSifter.update();
    }
}
