package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.geometry.Insets;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.*;

public class LocationSearcher extends SearchSelect<NodeInfo, JFXCheckBox> {
    Consumer<NodeInfo> onCheckNode = null;

    /**
     * An object that handles searching and selecting multiple locations.
     * Give it your search input, and a list to display the search results.
     * You may get the selected nodes or just their IDs.
     * @param searchBar a JFXTextField object that you wish to act as the search bar.
     * @param searchResultsList a JFXListView<JFXCheckBox> object that will list the search results and allow selection.
     */
    public LocationSearcher(JFXTextField searchBar, JFXListView<JFXCheckBox> searchResultsList) {
        super(searchBar, searchResultsList, null, null);

        setCellCreator(this::makeCheckbox);
        setMatcher(this::nodeMatchesText);
        setItemSorter(this::sortAlphabeticalOrder);
    }

    public List<NodeInfo> sortAlphabeticalOrder(List<NodeInfo> nodes) {
        NodeInfo[] nodeArray = nodes.toArray(new NodeInfo[0]);
        HeapSort.sort(nodeArray);
        return Arrays.asList(nodeArray);
    }

    public JFXCheckBox makeCheckbox(NodeInfo node, boolean isSelected) {
        String text = String.format("%s", node.getLongName());

        JFXCheckBox checkBox = new JFXCheckBox();
        checkBox.setPadding(new Insets(10));
        checkBox.setSelected(isSelected);
        checkBox.setText(text);

        checkBox.setOnAction(event -> {
            if (checkBox.isSelected()) selectItem(node);
            else deSelectItem(node);

            if (onCheckNode != null) onCheckNode.accept(node);
        });

        return checkBox;
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
        setItems(locations);
    }

    public List<NodeInfo> getSelectedLocations() {
        return getSelectedItems();
    }

    public List<String> getSelectedLocationIDs() {
        return getSelectedLocations().stream().map(NodeInfo::getNodeID).collect(Collectors.toList());
    }

    public void clearSelectedLocations() {
        clearSelectedItems();
    }

    public void setOnCheckNode(Consumer<NodeInfo> onCheckNode) {
        this.onCheckNode = onCheckNode;
    }

}
