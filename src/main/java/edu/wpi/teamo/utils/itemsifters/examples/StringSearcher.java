package edu.wpi.teamo.utils.itemsifters.examples;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.utils.itemsifters.ItemSearch;
import javafx.scene.control.Label;

import java.util.Locale;
import java.util.stream.Stream;

/**
 * This is an example of how to use ItemSearch. See ItemSearch for an example of how to use ItemSifter.
 */
public class StringSearcher {
    private final ItemSearch<String, Label> itemSearch;

    public StringSearcher(JFXTextField searchBar, JFXListView<Label> resultsList) {

        /*
         * Create a matcher, which is a function that takes in an item and a string,
         * and returns true if the string matches the text.
         * This one simply checks to see if the search text exists inside the string item.
         */
        ItemSearch.TextMatcher<String> matcher = (stringItem, searchText) -> {
            String lowercaseSearchText = searchBar.getText().toLowerCase(Locale.ROOT);
            String lowercaseString = stringItem.toLowerCase(Locale.ROOT);
            return lowercaseString.contains(lowercaseSearchText);
        };

        /*
         * Create a function used by the display to create a label for every string it receives.
         * Every time you update the ItemSifter, it will give all of its filtered items to the display,
         * which will run this function on each one, populating the list view with its result.
         */
        ItemSearch.CellCreator<String, Label> cellCreator = (string) -> {
            //recive item and create a javaFX item to contain it
            Label label = new Label(string);

            //add an event handler so we can click on the labels and do something with them
            label.setOnMouseClicked(event -> {
                System.out.println(label.getText() + " was clicked!");
            });
            label.setOnMouseEntered(event -> {
                label.setStyle("-fx-background-color: gold");
            });
            label.setOnMouseExited(event -> {
                label.setStyle("-fx-background-color: transparent");
            });

            //return the javaFX item to be displayed
            return label;
        };

        this.itemSearch = new ItemSearch<>(searchBar, resultsList, matcher, cellCreator);

        /*
         * Set the comparator so the strings are sorted in alphabetical order.
         * Strings are already comparable, so this is very easy.
         */
        this.itemSearch.setComparator(String::compareTo);
    }

    public void addStrings(Stream<String> strings) {
        strings.forEach(this::addString);
    }

    public void addString(String string) {
        itemSearch.addItem(string);
    }

    public void update() {
        itemSearch.update();
    }

}
