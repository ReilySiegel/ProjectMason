package edu.wpi.teamo.views.pathfindingpage;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.algos.*;
import edu.wpi.teamo.algos.AStar.AStarManager;
import edu.wpi.teamo.algos.BFS.BFSManager;
import edu.wpi.teamo.algos.BestFirst.BestFirstManager;
import edu.wpi.teamo.algos.DFS.GreedyDFSManager;
import edu.wpi.teamo.algos.Dijkstra.DijkstraManager;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.views.Map;
import edu.wpi.teamo.views.SubPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PathfindingPage extends SubPageController implements Initializable {

    @FXML
    private Label promptText;

    @FXML
    private JFXButton helpForExercise;

    @FXML
    private JFXToggleButton exerciseMode;

    @FXML
    private JFXButton helpButton;

    @FXML
    private JFXComboBox<String> floorComboBox;

    @FXML
    private AnchorPane parentNode;

    @FXML
    private GridPane gridPane;

    @FXML
    private StackPane parentStackPane;

    @FXML
    private VBox planningWindow;

    @FXML
    private JFXComboBox<String> startTypeFilterBox;
    @FXML
    private HBox startSearchWindow;
    @FXML
    private JFXTextField startSearchBar;
    @FXML
    private JFXListView<Label> startSearchList;

    @FXML
    private JFXComboBox<String> endTypeFilterBox;
    @FXML
    private HBox endSearchWindow;
    @FXML
    private JFXTextField endSearchBar;
    @FXML
    private JFXListView<Label> endSearchList;

    @FXML
    JFXButton findPathButton;

    @FXML
    private AnchorPane pathPane;

    @FXML
    private HBox algoSwitchWindow;

    @FXML
    private JFXComboBox<String> algoSwitcher;

    @FXML
    private VBox pathDisplayControlWindow;

    @FXML
    private JFXListView<HBox> textualDirView;

    @FXML
    private JFXButton textualUnitsBtn;

    LinkedList<AlgoNode> calculatedPath = null;

    @FXML
    private JFXButton saveNewParkingSpotButton;

    @FXML
    private JFXButton selectSavedParkingSpotButton;

    @FXML
    private HBox parkingWindow;

    @FXML
    private JFXButton forwardStepButton;

    @FXML
    private JFXButton backwardStepButton;

    @FXML
    private HBox pathStepHBox;

    @FXML
    private HBox currentDirectionDisplay;

    PathSelectionControls pathSelection;

    PathDisplayControls pathDisplayControls;

    ParkingSpotControls parkingSpotControls;

    edu.wpi.teamo.views.Map map;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setPickOnBounds(false);

        pathSelection = new PathSelectionControls(endSearchWindow,
                                                  startSearchWindow,
                                                  endSearchBar,
                                                  startSearchBar,
                                                  endSearchList,
                                                  startSearchList,
                                                  endTypeFilterBox,
                                                  startTypeFilterBox,
                                                  findPathButton,
                                                  planningWindow,
                                                  this::handleSelectedStart,
                                                  this::handleSelectedEnd,
                                                  this::handleChoosing,
                                                  this::handleFindPath,
                                                  this::handlePlanNewPath);

        TextualDirections textualDirections = new TextualDirections(textualDirView,
                                                                    textualUnitsBtn,
                                                                    currentDirectionDisplay);

        pathDisplayControls = new PathDisplayControls(pathDisplayControlWindow,
                                                      forwardStepButton,
                                                      backwardStepButton,
                                                      textualDirections,
                                                      pathStepHBox,
                                                      floorComboBox,
                                                      this::handleStep);

        parkingSpotControls = new ParkingSpotControls(selectSavedParkingSpotButton,
                                                      saveNewParkingSpotButton,
                                                      parkingWindow,
                                                      parentStackPane,
                                                      this::onPickParkingSpot);

        if (Session.isLoggedIn()) {
            parkingSpotControls.show();
        } else {
            parkingSpotControls.hide();
        }

        initFloorSwitcher();
        initAlgoSwitcher();

        // actions added for Exercise Mode
        helpForExercise.setOnAction(this::handleExerciseHelpButton);
        exerciseMode.setOnAction(this::handleExerciseMode);

        floorComboBox.setOnAction(this::handleFloorSwitch);
        algoSwitcher.setOnAction(this::handleAlgoSwitch);
        helpButton.setOnAction(this::handleHelpButton);
        //clearPathButton.setOnAction(this::handleClearPath);

        App.getPrimaryStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                pathDisplayControls.hide();
                pathSelection.reset();
            }
        });
        pathDisplayControls.hide();
        map = new Map(pathPane);
        map.setOnDrawNode(this::onDrawNode);

        update();
        map.hideNodes();
    }

    // methods added for Exercise Mode
    private void handleExerciseMode(ActionEvent e){
        if (exerciseMode.isSelected()){
            algoSwitcher.setValue(Context.dfsCode);
        }
        else algoSwitcher.setValue(Context.aStarCode);
    }

    private void handleExerciseHelpButton(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(App.resourceBundle.getString("key.help")));
        content.setBody(new Label(App.resourceBundle.getString("key.exercise_help")));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setOnAction(event -> errorWindow.close());
        closeButton.setStyle("-jfx-button-type: RAISED");

        content.setActions(closeButton);
        errorWindow.show();
    }


    private void handleStep() {
        update();
        focusMapOnNode(pathDisplayControls.getNodeInFocus());
    }

    private void handleSelectedStart() {
        map.hideNodes();
        if (map.getStartingNodeCircle() != null) map.getStartingNodeCircle().setVisible(true);
        if (map.getEndingNodeCircle() != null) map.getEndingNodeCircle().setVisible(true);
        if (map.getIndexCircle() != null) map.getIndexCircle().setVisible(true);
        clearPrompt();
    }

    private void handleSelectedEnd() {
        map.hideNodes();
        if (map.getStartingNodeCircle() != null) map.getStartingNodeCircle().setVisible(true);
        if (map.getEndingNodeCircle() != null) map.getEndingNodeCircle().setVisible(true);
        if (map.getIndexCircle() != null) map.getIndexCircle().setVisible(true);
        clearPrompt();
    }

    private void handleChoosing() {
        map.showNodes();
        if (map.getStartingNodeCircle() != null) map.getStartingNodeCircle().setVisible(false);
        if (map.getEndingNodeCircle() != null) map.getEndingNodeCircle().setVisible(false);
        if (map.getIndexCircle() != null) map.getIndexCircle().setVisible(false);
        String chooseKey = pathSelection.getState() == PathSelectionControls.SelectionState.CHOOSING_START
                           ? "key.select_path_start_node_prompt"
                           : "key.select_path_end_node_prompt";
        setPrompt(App.resourceBundle.getString(chooseKey));
    }

    private void onPickParkingSpot(NodeInfo parkingSpot) {
        onClickNode(parkingSpot);
    }

    private void handleAlgoSwitch(ActionEvent actionEvent) {
        // if statement to handle Exercise Mode
        if (algoSwitcher.getValue() != Context.dfsCode){
            exerciseMode.setSelected(false);
        }
        App.context.switchAlgoManagerByCode(algoSwitcher.getValue());
    }

    private void initFloorSwitcher() {
        floorComboBox.getItems().add(edu.wpi.teamo.views.Map.floorL2Key);
        floorComboBox.getItems().add(edu.wpi.teamo.views.Map.floorL1Key);
        floorComboBox.getItems().add(edu.wpi.teamo.views.Map.floorGKey);
        floorComboBox.getItems().add(edu.wpi.teamo.views.Map.floor1Key);
        floorComboBox.getItems().add(edu.wpi.teamo.views.Map.floor2Key);
        floorComboBox.getItems().add(edu.wpi.teamo.views.Map.floor3Key);
        floorComboBox.setValue(edu.wpi.teamo.views.Map.floor1Key);
    }

    private void initAlgoSwitcher() {
        boolean visible = Session.isLoggedIn() && Session.getAccount().isAdmin();
        algoSwitchWindow.setVisible(visible);
        algoSwitchWindow.setManaged(visible);

        algoSwitcher.getItems().add(Context.aStarCode);
       // algoSwitcher.getItems().add(Context.dfsCode);
        algoSwitcher.getItems().add(Context.bfsCode);
        algoSwitcher.getItems().add(Context.bestFirstCode);
        algoSwitcher.getItems().add(Context.DijkstraCode);
        algoSwitcher.getItems().add(Context.GreedyDFSCode);

        /*if (App.context.getAlgoManager().getClass() == DFSManager.class) {
            algoSwitcher.setValue(Context.dfsCode);
        }*/
        if (App.context.getAlgoManager().getClass() == BFSManager.class) {
            algoSwitcher.setValue(Context.bfsCode);
        }
        else if (App.context.getAlgoManager().getClass() == AStarManager.class) {
            algoSwitcher.setValue(Context.aStarCode);
        }
        else if (App.context.getAlgoManager().getClass() == BestFirstManager.class) {
            algoSwitcher.setValue(Context.bestFirstCode);
        }
        else if (App.context.getAlgoManager().getClass() == DijkstraManager.class) {
            algoSwitcher.setValue(Context.DijkstraCode);
        }
        else if (App.context.getAlgoManager().getClass() == GreedyDFSManager.class){
            algoSwitcher.setValue(Context.GreedyDFSCode);
        }
    }

    void onDrawNode(Pair<Circle, NodeInfo> p) {
        Circle circle = p.getKey();
        NodeInfo node = p.getValue();

        if (parkingSpotControls.hasParkingSpot()) {
            if (node.getNodeID().equals(parkingSpotControls.getSavedParkingSpotID())) {
                circle.setRadius(circle.getRadius() * 3);
                circle.setFill(Color.BLACK);
            }
        }

        circle.setOnMouseEntered(event -> {
            circle.setRadius(circle.getRadius() * 2);
            event.consume();
        });

        circle.setOnMouseExited(event -> {
            circle.setRadius(circle.getRadius() / 2);
            event.consume();
        });

        circle.setOnMousePressed((MouseEvent e) -> {
            onClickNode(node);
            e.consume();
        });

        if (node.getNodeType().equals("PARK")) {
            parkingSpotControls.addParkingSpotCircle(circle);
        }
    }

    void onClickNode(NodeInfo node) {
        pathSelection.onClickNode(node);
        parkingSpotControls.onClickNode(node);
    }

    private void handlePlanNewPath() {
        clearPath();
        map.centerMap();
        pathDisplayControls.hide();
    }


    // clear the path
    void clearPath(){
        LinkedList<AlgoNode> path = new LinkedList<>();
        pathDisplayControls.setDisplayedPath(path);
        if(calculatedPath != null) {
            calculatedPath.clear();
        }
        update();
    }


    private void handleFindPath() {
        NodeInfo startNode = pathSelection.getSelectedStartNode();
        NodeInfo endNode = pathSelection.getSelectedEndNode();
        map.hideNodes();
        if (startNode != null && endNode != null) {
            findPath(startNode.getNodeID(), endNode.getNodeID());
        }
    }

    void findPath(String startID, String endID) {
        LinkedList<AlgoNode> path = new LinkedList<>();
        try {
            path = App.context.getPath(startID, endID);
            if (path != null && path.size() > 0) {
                floorComboBox.setValue(path.get(0).getFloor());
                focusMapOnNode(path.get(0));
                calculatedPath = path;
            }
        } catch (SQLException | NullPointerException throwables) {
            System.out.println("No path calculated");
        }

        pathDisplayControls.setDisplayedPath(path);
        pathDisplayControls.show();
        update();
    }

    void update() {
        parkingSpotControls.clearParkingSpotCircles();

        List<NodeInfo> nodes = getAllNodes();

        /* redraw map nodes */
        map.clearShapes();
        map.drawNodes(nodes, floorComboBox.getValue(), "pathfinder-node");
        if (pathSelection.getState() == PathSelectionControls.SelectionState.IDLE) {
            map.hideNodes();
        }

        /* redraw path */
        if (calculatedPath != null) {
            map.drawPath(calculatedPath, floorComboBox.getValue(), pathDisplayControls.getDirectionIterator());
        }

        pathSelection.setLocations(nodes);
    }

    private List<NodeInfo> getAllNodes() {
        List<NodeInfo> nodes = new LinkedList<>();
        try {
            nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
            boolean mightBeSick = true;
            if (Session.isLoggedIn()) {
                mightBeSick = Session.getAccount().getUseEmergencyEntrance();
            }

            String entranceToFilterOut = mightBeSick ? App.normalEntrance : App.emergencyEntrance;
            nodes = nodes.stream()
                    .filter(node -> !node.getNodeID().equals(entranceToFilterOut))
                    .collect(Collectors.toList());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return nodes;
    }

    void focusMapOnNode(AlgoNode node) {
        map.centerOnMapCoords(node.getX(), node.getY());
    }

    void handleFloorSwitch(ActionEvent e) {
        update();
    }

    private void handleHelpButton(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label(App.resourceBundle.getString("key.help")));
        content.setBody(new Label(App.resourceBundle.getString("key.pathfinding_help")));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setOnAction(event -> errorWindow.close());
        closeButton.setStyle("-jfx-button-type: RAISED");

        content.setActions(closeButton);
        errorWindow.show();
    }

    private void setPrompt(String string) {
        promptText.setText(string);
    }

    private void clearPrompt() {
        promptText.setText("");
    }

}