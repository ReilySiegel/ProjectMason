package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.geometry.Insets;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LocationSearcher {

    JFXListView<JFXCheckBox> searchResultsList;
    JFXTextField searchBar;

    List<NodeInfo> selectedLocations;
    List<NodeInfo> locations;

    Consumer<NodeInfo> onCheckNode = null;
    Consumer<JFXCheckBox> onMakeCheckbox = null;


    /**
     * An object that handles searching and selecting multiple locations.
     * Give it your search input, and a list to display the search results.
     * You may get the selected nodes or just their IDs.
     * @param searchBar a JFXTextField object that you wish to act as the search bar.
     * @param searchResultsList a JFXListView<JFXCheckBox> object that will list the search results and allow selection.
     */
    public LocationSearcher(JFXTextField searchBar, JFXListView<JFXCheckBox> searchResultsList) {
        this.searchResultsList = searchResultsList;
        this.searchBar = searchBar;

        this.selectedLocations = new LinkedList<>();
        this.locations = new LinkedList<>();

        this.searchBar.setOnKeyTyped(event -> handleSearchInput());
    }

    /**
     * Load the search engine with nodes to search for.
     * Side effect: Displays the nodes in the search results window.
     * @param locations List of NodeInfo objects to be searched and selected.
     */
    public void setLocations(List<NodeInfo> locations) {
        updateMatchingLocations(locations);
        this.locations = locations;
    }

    public List<NodeInfo> getSelectedLocations() {
        return selectedLocations;
    }

    public List<String> getSelectedLocationIDs() {
        return selectedLocations.stream().map(NodeInfo::getNodeID).collect(Collectors.toList());
    }

    public void clearSelectedLocations() {
        selectedLocations = new LinkedList<>();
        handleSearchInput();
    }

    public void setOnCheckNode(Consumer<NodeInfo> onCheckNode) {
        this.onCheckNode = onCheckNode;
    }

    private void handleSearchInput() {

        String searchText = searchBar.getText();

        List<NodeInfo> matchingLocations = locations.stream()
                .filter((NodeInfo node) -> nodeMatchesText(node, searchText))
                .collect(Collectors.toList());

        updateMatchingLocations(matchingLocations);
    }

    private boolean nodeMatchesText(NodeInfo node, String searchText) {
        boolean matching;

        searchText = searchText.toLowerCase(Locale.ROOT);

        matching  = String.valueOf(node.getXPos()).toLowerCase(Locale.ROOT).contains(searchText);
        matching |= String.valueOf(node.getYPos()).toLowerCase(Locale.ROOT).contains(searchText);
        matching |= node.getShortName().toLowerCase(Locale.ROOT).contains(searchText);
        matching |= node.getNodeType().toLowerCase(Locale.ROOT).contains(searchText);
        matching |= node.getBuilding().toLowerCase(Locale.ROOT).contains(searchText);
        matching |= node.getLongName().toLowerCase(Locale.ROOT).contains(searchText);
        matching |= node.getNodeID().toLowerCase(Locale.ROOT).contains(searchText);
        matching |= node.getFloor().toLowerCase(Locale.ROOT).contains(searchText);

        return matching;
    }

    private void updateMatchingLocations(List<NodeInfo> matchingLocations) {
        searchResultsList.getItems().removeAll(searchResultsList.getItems());

        for (NodeInfo node : selectedLocations) {
            searchResultsList.getItems().add(createCheckBox(node, true));
        }

        for (NodeInfo node : matchingLocations) {
            searchResultsList.getItems().add(createCheckBox(node, false));
        }

    }

    public void setOnMakeCheckbox(Consumer<JFXCheckBox> onMakeCheckbox) {
        this.onMakeCheckbox = onMakeCheckbox;
    }

    private JFXCheckBox createCheckBox(NodeInfo node, boolean selected) {
        String text = String.format("%s", node.getLongName());

        JFXCheckBox checkBox = new JFXCheckBox();
        checkBox.setPadding(new Insets(10));
        checkBox.setSelected(selected);
        checkBox.setStyle("-jfx-checked-color: #5e81ac");
        checkBox.setText(text);

        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) selectedLocations.add(node);
            else selectedLocations.remove(node);

            if (onCheckNode != null) onCheckNode.accept(node);
        });

        if (onMakeCheckbox != null) onMakeCheckbox.accept(checkBox);

        return checkBox;
    }

    /**
     * gets path to a stylesheet that makes the list transparent. Will get rid of soon hopefully.
     * Load it like this:
     * App.getPrimaryStage().getScene().getStylesheets().add(LocationSearcher.getStylePath());
     */
    public static String getStylePath() {
        return "edu/wpi/teamo/fxml/CSS/LocationSearch.css";
    }


}
