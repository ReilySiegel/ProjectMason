package edu.wpi.teamo.views.pathfindingpage;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import edu.wpi.teamo.utils.itemsifters.SingleLocationSearcher;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.LinkedList;
import java.util.List;

public class PathSelectionControls {

    private static final String allTypesKey = "All";

    private final JFXButton findPathButton;
    private final SingleLocationSearcher startLocationSearcher;
    private final SingleLocationSearcher endLocationSearcher;
    JFXComboBox<String> startTypeFilterBox;
    JFXComboBox<String> endTypeFilterBox;
    JFXListView<Label> endSearchList;
    JFXListView<Label> startSearchList;
    private final JFXTextField startSearchBar;
    private final JFXTextField endSearchBar;
    private final HBox startSearchWindow;
    private final HBox endSearchWindow;
    private final VBox planningWindow;

    enum SelectionState {
        CHOOSING_START,
        CHOOSING_END,
        IDLE
    }
    SelectionState state = SelectionState.IDLE;

    Runnable onSelectStart;
    Runnable onSelectEnd;
    Runnable onChoosing;
    Runnable onFindPath;
    Runnable onPlanNewPath;

    private NodeInfo selectedStartNode = null;
    private NodeInfo selectedEndNode = null;

    public PathSelectionControls(HBox endSearchWindow,
                                 HBox startSearchWindow,
                                 JFXTextField endSearchBar,
                                 JFXTextField startSearchBar,
                                 JFXListView<Label> endSearchList,
                                 JFXListView<Label> startSearchList,
                                 JFXComboBox<String> endTypeFilterBox,
                                 JFXComboBox<String> startTypeFilterBox,
                                 JFXButton findPathButton,
                                 VBox planningWindow,
                                 Runnable handleSelectedStart,
                                 Runnable handleSelectedEnd,
                                 Runnable handleChoosing,
                                 Runnable onFindPath,
                                 Runnable onPlanNewPath) {

        this.findPathButton     = findPathButton;
        this.endSearchWindow    = endSearchWindow;
        this.startSearchWindow  = startSearchWindow;
        this.endSearchBar       = endSearchBar;
        this.startSearchBar     = startSearchBar;
        this.endSearchList      = endSearchList;
        this.startSearchList    = startSearchList;
        this.endTypeFilterBox   = endTypeFilterBox;
        this.startTypeFilterBox = startTypeFilterBox;
        this.onSelectStart      = handleSelectedStart;
        this.onSelectEnd        = handleSelectedEnd;
        this.onChoosing         = handleChoosing;
        this.onFindPath         = onFindPath;
        this.onPlanNewPath      = onPlanNewPath;
        this.planningWindow     = planningWindow;

        startTypeFilterBox.setOnAction(this::handleSwitchTypeFilter);
        endTypeFilterBox.setOnAction(this::handleSwitchTypeFilter);
        findPathButton.setOnAction(this::handleFindPath);

        startSearchBar.setOnMouseClicked(event -> switchToChoosingStart());
        endSearchBar.setOnMouseClicked(event -> switchToChoosingEnd());

        this.startLocationSearcher = new SingleLocationSearcher(startSearchBar, startSearchList);
        this.endLocationSearcher = new SingleLocationSearcher(endSearchBar, endSearchList);
        initSearchers();

        startLocationSearcher.addHardFilter((node) -> {
            String typeFilter = startTypeFilterBox.getValue();
            return    typeFilter == null
                   || typeFilter.equals(allTypesKey)
                   || typeFilter.equals(node.getNodeType());
        });

        endLocationSearcher.addHardFilter((node) -> {
            String typeFilter = endTypeFilterBox.getValue();
            return    typeFilter == null
                    || typeFilter.equals(allTypesKey)
                    || typeFilter.equals(node.getNodeType());
        });

        switchToIdle();
        hide();
    }

    private void handleFindPath(ActionEvent actionEvent) {
        if (!planningWindow.isVisible()) {
            onPlanNewPath.run();
            show();
        }
        else if (validateSelections()) {
            switchToIdle();
            onFindPath.run();
            hide();
        }
    }

    private boolean validateSelections() {
        boolean valid = true;
        if (selectedStartNode == null) {
            valid = false;
        }
        if (selectedEndNode == null) {
            valid = false;
        }
        return valid;
    }

    private void handleSwitchTypeFilter(ActionEvent actionEvent) {
        startLocationSearcher.update();
        endLocationSearcher.update();
    }

    public void onClickNode(NodeInfo node) {
        switch (state) {
            case CHOOSING_START:
                chooseStartNode(node);
                break;
            case CHOOSING_END:
                chooseEndNode(node);
                break;
            case IDLE:
                break;
        }
    }

    private void chooseStartNode(NodeInfo node) {
        selectStartNode(node);
        closeStartSearchWindow();
        switchToIdle();
    }

    private void chooseEndNode(NodeInfo node) {
        selectEndNode(node);
        closeEndSearchWindow();
        switchToIdle();
    }

    private void switchToIdle() {
        closeStartSearchWindow();
        closeEndSearchWindow();
        state = SelectionState.IDLE;
    }

    private void switchToChoosingStart() {
        state = SelectionState.CHOOSING_START;
        openStartSearchWindow();
        closeEndSearchWindow();
        onChoosing.run();
    }

    private void switchToChoosingEnd() {
        state = SelectionState.CHOOSING_END;
        closeStartSearchWindow();
        openEndSearchWindow();
        onChoosing.run();
    }

    private void selectStartNode(NodeInfo startNode) {
        startSearchBar.setPromptText(startNode.getLongName());
        startSearchBar.setText("");
        selectedStartNode = startNode;
        onSelectStart.run();
    }

    private void selectEndNode(NodeInfo endNode) {
        endSearchBar.setPromptText(endNode.getLongName());
        endSearchBar.setText("");
        selectedEndNode = endNode;
        onSelectEnd.run();
    }

    public void reset() {
        switchToIdle();
    }

    private void hide() {
        findPathButton.setText(App.resourceBundle.getString("key.find_path"));
        closeStartSearchWindow();
        closeEndSearchWindow();
        planningWindow.setVisible(false);
        planningWindow.setMouseTransparent(true);
        reset();
    }

    private void show() {
        findPathButton.setText(App.resourceBundle.getString("key.compute_path"));
        planningWindow.setMouseTransparent(false);
        planningWindow.setVisible(true);
        switchToIdle();
    }

    public void openStartSearchWindow() {
        startSearchWindow.setVisible(true);
        startSearchWindow.setManaged(true);
    }

    public void initSearchers() {
        startLocationSearcher.setOnClickNode(this::chooseStartNode);
        endLocationSearcher.setOnClickNode(this::chooseEndNode);
        this.startLocationSearcher.update();
        this.endLocationSearcher.update();

        if (!endTypeFilterBox.getItems().contains(allTypesKey)) {
            endTypeFilterBox.getItems().add(allTypesKey);
        }
        endTypeFilterBox.setValue(allTypesKey);

        if (!startTypeFilterBox.getItems().contains(allTypesKey)) {
            startTypeFilterBox.getItems().add(allTypesKey);
        }
        startTypeFilterBox.setValue(allTypesKey);

        startSearchBar.focusedProperty().addListener(event -> {
            if (startSearchBar.focusedProperty().getValue()) {
                switchToChoosingStart();
            }
        });

        endSearchBar.focusedProperty().addListener(event -> {
            if (endSearchBar.focusedProperty().getValue()) {
                switchToChoosingEnd();
            }
        });

    }

    public void openEndSearchWindow() {
        endSearchWindow.setVisible(true);
        endSearchWindow.setManaged(true);
    }

    public void closeStartSearchWindow() {
        startSearchWindow.setVisible(false);
        startSearchWindow.setManaged(false);
    }

    public void closeEndSearchWindow() {
        endSearchWindow.setVisible(false);
        endSearchWindow.setManaged(false);
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

    public void setLocations(List<NodeInfo> nodes) {
        /* fill the type filter box with the available types for filtering */
        List<String> types = new LinkedList<>();
        types.add(allTypesKey);
        nodes.forEach(node -> {
            if (!types.contains(node.getNodeType())) types.add(node.getNodeType());
        });
        startTypeFilterBox.getItems().setAll(types);
        endTypeFilterBox.getItems().setAll(types);

        startTypeFilterBox.setValue(allTypesKey);
        endTypeFilterBox.setValue(allTypesKey);

        startLocationSearcher.setLocations(nodes);
        endLocationSearcher.setLocations(nodes);
    }


}
