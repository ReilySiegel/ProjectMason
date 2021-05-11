package edu.wpi.teamo.utils.itemsifters;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.database.map.NodeInfo;
import java.util.function.Consumer;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import java.util.Locale;
import java.util.List;

public class SingleLocationSearcher {
    ItemSearch<NodeInfo, Label> itemSearch;
    Consumer<NodeInfo> onClickNode = null;

    /**
     * An object that handles searching and selecting multiple locations.
     * Give it your search input, and a list to display the search results.
     * You may get the selected nodes or just their IDs.
     * @param searchBar a JFXTextField object that you wish to act as the search bar.
     * @param searchResultsList a JFXListView<JFXCheckBox> object that will list the search results and allow selection.
     */
    public SingleLocationSearcher(JFXTextField searchBar, JFXListView<Label> searchResultsList) {
        itemSearch = new ItemSearch<>(searchBar, searchResultsList, this::nodeMatchesText, this::makeCell);
        itemSearch.setComparator((n1, n2) -> n1.getLongName().compareToIgnoreCase(n2.getLongName()));
    }

    public Label makeCell(NodeInfo node) {
        String text = String.format("%s", node.getLongName());

        Label label = new Label();
        label.setPadding(new Insets(10));
        label.setText(text);

        label.setOnMouseClicked(event -> {
            if (onClickNode != null) onClickNode.accept(node);
        });

        label.setOnMouseEntered(event -> {
            label.setStyle("-fx-border-color: blue");
        });

        label.setOnMouseExited(event -> {
            label.setStyle("-fx-border-color: transparent");
        });

        return label;
    }

    public boolean nodeMatchesText(NodeInfo node, String text) {
        boolean matching;

        text = text.toLowerCase(Locale.ROOT);

        matching  = String.valueOf(node.getXPos()).toLowerCase(Locale.ROOT).contains(text);
        matching |= String.valueOf(node.getYPos()).toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getShortName().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getNodeType().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getBuilding().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getLongName().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getNodeID().toLowerCase(Locale.ROOT).contains(text);
        matching |= node.getFloor().toLowerCase(Locale.ROOT).contains(text);

        return matching;
    }

    /**
     * Load the search engine with nodes to search for.
     * Side effect: Displays the nodes in the search results window.
     * @param locations List of NodeInfo objects to be searched and selected.
     */
    public void setLocations(List<NodeInfo> locations) {
        itemSearch.clearItems();
        itemSearch.addItems(locations.stream());
        itemSearch.update();
    }

    public void setOnClickNode(Consumer<NodeInfo> onClickNode) {
        this.onClickNode = onClickNode;
    }

    public void addHardFilter(Sift.Filter<NodeInfo> filter) {
        itemSearch.addHardFilter(filter);
    }

    public void update() {
        itemSearch.update();
    }

}
