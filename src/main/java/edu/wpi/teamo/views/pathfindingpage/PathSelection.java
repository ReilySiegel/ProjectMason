package edu.wpi.teamo.views.pathfindingpage;

import com.jfoenix.controls.*;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.views.LocationSearcher;
import javafx.event.ActionEvent;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PathSelection {

    private static final String allTypesKey = "All";

    private final LocationSearcher locationSearcher;
    private final JFXButton chooseStartButton;
    private final JFXButton chooseEndButton;
    JFXComboBox<String> nodeTypeFilterBox;
    private final JFXTextField searchBar;
    private final VBox searchWindow;

    enum SelectionState {
        CHOOSING_START,
        CHOOSING_END,
        IDLE
    }
    SelectionState state = SelectionState.IDLE;

    Runnable onSelectStart;
    Runnable onSelectEnd;
    Runnable onChoosing;

    private NodeInfo selectedStartNode = null;
    private NodeInfo selectedEndNode = null;

    public PathSelection(JFXButton chooseStartButton,
                         JFXButton chooseEndButton,
                         VBox searchWindow,
                         JFXTextField searchBar,
                         JFXListView<JFXCheckBox> searchResultsView,
                         JFXComboBox<String> nodeTypeFilterBox,
                         Runnable onSelectStart,
                         Runnable onSelectEnd,
                         Runnable onChoosing) {

        this.chooseStartButton = chooseStartButton;
        this.nodeTypeFilterBox = nodeTypeFilterBox;
        this.chooseEndButton = chooseEndButton;
        this.onSelectStart = onSelectStart;
        this.searchWindow = searchWindow;
        this.onSelectEnd = onSelectEnd;
        this.onChoosing = onChoosing;
        this.searchBar = searchBar;

        chooseStartButton.setOnAction(this::handleChooseStartButton);
        chooseEndButton.setOnAction(this::handleChooseEndButton);
        nodeTypeFilterBox.setOnAction(this::handleSwitchTypeFilter);

        this.locationSearcher = new LocationSearcher(searchBar, searchResultsView);
        locationSearcher.setOnCheckNode(this::onClickNode);
        locationSearcher.addHardFilter((node) -> {
            String typeFilter = nodeTypeFilterBox.getValue();
            return    typeFilter == null
                   || typeFilter.equals(allTypesKey)
                   || typeFilter.equals(node.getNodeType());
        });

        reset();
    }

    private void handleSwitchTypeFilter(ActionEvent actionEvent) {
        locationSearcher.update();
    }

    public void onClickNode(NodeInfo node) {
        switch (state) {
            case CHOOSING_START:
                selectStartNode(node);
                switchToIdle();
                break;
            case CHOOSING_END:
                selectEndNode(node);
                switchToIdle();
                break;
            case IDLE:
                break;
        }
    }

    private void handleChooseStartButton(ActionEvent e) {
        switch(state) {
            case CHOOSING_START:
                switchToIdle();
                break;
            default:
                switchToChoosingStart();
                break;
        }
    }

    private void handleChooseEndButton(ActionEvent e) {
        switch(state) {
            case CHOOSING_END:
                switchToIdle();
                break;
            default:
                switchToChoosingEnd();
                break;
        }
    }

    private void switchToIdle() {
        chooseStartButton.setDisable(false);
        chooseEndButton.setDisable(false);
        closeSearchWindow();
        state = SelectionState.IDLE;
    }

    private void switchToChoosingStart() {
        state = SelectionState.CHOOSING_START;
        chooseStartButton.setDisable(true);
        chooseEndButton.setDisable(false);
        openSearchWindow();
        onChoosing.run();
    }

    private void switchToChoosingEnd() {
        state = SelectionState.CHOOSING_END;
        chooseStartButton.setDisable(false);
        chooseEndButton.setDisable(true);
        openSearchWindow();
        onChoosing.run();
    }

    private void selectStartNode(NodeInfo startNode) {
        chooseStartButton.setText(startNode.getShortName());
        selectedStartNode = startNode;
        onSelectStart.run();
    }

    private void selectEndNode(NodeInfo endNode) {
        chooseEndButton.setText(endNode.getShortName());
        selectedEndNode = endNode;
        onSelectEnd.run();
    }

    public void reset() {
        switchToIdle();
    }

    public void openSearchWindow() {
        if (!nodeTypeFilterBox.getItems().contains(allTypesKey)) {
            nodeTypeFilterBox.getItems().add(allTypesKey);
        }
        nodeTypeFilterBox.setValue(allTypesKey);

        searchBar.clear();
        locationSearcher.update();
        searchWindow.setVisible(true);
    }

    public void closeSearchWindow() {
        locationSearcher.clearSelectedLocations();
        searchWindow.setVisible(false);
    }

    public NodeInfo getSelectedStartNode() {
        return selectedStartNode;
    }

    public NodeInfo getSelectedEndNode() {
        return selectedEndNode;
    }

    public SelectionState getState() {
        return state;
    }

    public void setLocations(LinkedList<NodeInfo> nodes) {
        /* fill the type filter box with the available types for filtering */
        List<String> types = new LinkedList<>();
        types.add(allTypesKey);
        nodes.forEach(node -> {
            if (!types.contains(node.getNodeType())) types.add(node.getNodeType());
        });
        nodeTypeFilterBox.getItems().setAll(types);
        nodeTypeFilterBox.setValue(allTypesKey);

        locationSearcher.setLocations(nodes);
    }
}
