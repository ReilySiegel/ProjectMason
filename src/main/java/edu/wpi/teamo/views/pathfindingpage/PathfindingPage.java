package edu.wpi.teamo.views.pathfindingpage;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.algos.*;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.views.Map;
import edu.wpi.teamo.views.SubPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class PathfindingPage extends SubPageController implements Initializable {

    @FXML
    private JFXButton helpButton;

    @FXML
    private JFXButton helpForExercise;

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


    @FXML
    private JFXToggleButton exerciseMode;


    PathSelection pathSelection;


    PathDisplayControls pathDisplayControls;

    public List<Circle> parkingSpots;

    String parkingSpotID = null;

    boolean selectingParkingSpot = false;

    edu.wpi.teamo.views.Map map;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        exerciseMode.setSelected(false);
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

        setSelectParkingSpotButtonVisibility(Session.isLoggedIn());

        initFloorSwitcher();
        initAlgoSwitcher();

        selectSavedParkingSpotButton.setOnAction(this::handleSelectParkingSpot);
        saveNewParkingSpotButton.setOnAction(this::handleSaveNewParkingSpot);
        floorComboBox.setOnAction(this::handleFloorSwitch);
        algoSwitcher.setOnAction(this::handleAlgoSwitch);
        helpButton.setOnAction(this::handleHelpButton);

        helpForExercise.setOnAction(this::handleExerciseHelpButton);
        directBack.setOnAction(this::handleStepBack);
        directForward.setOnAction(this::handleStepForward);
        textualUnitsBtn.setOnAction(this::handleUnitSwitch);
        exerciseMode.setOnAction(this::handleExerciseMode);


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

    private void handleStep() {
        update();
        focusMapOnNode(pathDisplayControls.getNodeInFocus());
    }

    private void handleSelectedStart() {
        map.hideNodes();
        if (map.getEndingNodeCircle() != null) map.getEndingNodeCircle().setVisible(true);
        if (map.getStartingNodeCircle() != null) map.getStartingNodeCircle().setVisible(true);
    }

    private void handleSelectedEnd() {
        map.hideNodes();
        if (map.getEndingNodeCircle() != null) map.getEndingNodeCircle().setVisible(true);
        if (map.getStartingNodeCircle() != null) map.getStartingNodeCircle().setVisible(true);
    }

    private void handleChoosing() {
        map.showNodes();
        if (map.getEndingNodeCircle() != null) map.getEndingNodeCircle().setVisible(false);
        if (map.getStartingNodeCircle() != null) map.getStartingNodeCircle().setVisible(false);
    }

    private void handleSelectParkingSpot(ActionEvent actionEvent) {
        try {
            if (pathSelection.getState() != PathSelectionControls.SelectionState.IDLE) {
                if (Session.isLoggedIn() && Session.getAccount().getParkingSpot() != null) {
                    String id = Session.getAccount().getParkingSpot();
                    NodeInfo node = App.mapService.getNode(id);
                    onClickNode(node);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSaveNewParkingSpot(ActionEvent actionEvent) {
        if (selectingParkingSpot) {
            selectingParkingSpot = false;
        }
        else {
            selectingParkingSpot = true;
            showParkingSpots();
        }
    }

    private void handleAlgoSwitch(ActionEvent actionEvent) {
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
        algoSwitchWindow.setVisible(Session.isLoggedIn() && Session.getAccount().isAdmin());

        algoSwitcher.getItems().add(Context.aStarCode);
        //algoSwitcher.getItems().add(Context.dfsCode);
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

    private void handleExerciseMode(ActionEvent e){
        if (exerciseMode.isSelected()){
            algoSwitcher.setValue(Context.dfsCode);
        }
        else algoSwitcher.setValue(Context.aStarCode);
    }

    void onDrawNode(Pair<Circle, NodeInfo> p) {
        Circle circle = p.getKey();
        NodeInfo node = p.getValue();

        if (parkingSpotID != null && node.getNodeID().equals(parkingSpotID)) {
            circle.setRadius(circle.getRadius() * 3);
            circle.setFill(Color.BLACK);
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
            onRightClickNode(e, circle, node);
            e.consume();
        });

        if (parkingSpots != null && node.getNodeType().equals("PARK")) {
            parkingSpots.add(circle);
        }
    }

    public void showParkingSpots() {
        for (Circle circle : parkingSpots) {
            circle.setVisible(true);
        }
    }

    //    Consumer<NodeInfo> onClickNode = (NodeInfo node) -> System.out.println("Node " + node.getNodeID() + "was clicked");
    void onClickNode(NodeInfo node) {
        pathSelection.onClickNode(node);
        if (selectingParkingSpot) {
            try {
                if (Session.isLoggedIn()) {
                    Session.getAccount().setParkingSpot(node);
                    update();
                }
                else {
                    App.showError(App.resourceBundle.getString("key.log_in_to_use_this_feature"), parentStackPane);
                }
            }
            catch (InvalidParameterException e) {
                App.showError(App.resourceBundle.getString("key.parking_spot_error"), parentStackPane);
            }
            selectingParkingSpot = false;
        }
    }

    private void setSelectParkingSpotButtonVisibility(boolean visible) {
        parkingWindow.setManaged(visible);
        parkingWindow.setVisible(visible);
    }

    void onRightClickNode(MouseEvent e, Circle circle, NodeInfo node){
        if(e.isSecondaryButtonDown()){
            nodeContextMenu(e, circle, node);
        }
    }

    void nodeContextMenu( MouseEvent e, Circle circle, NodeInfo node){
        ContextMenu menu = new ContextMenu();

        MenuItem assignParkingNode = new MenuItem(App.resourceBundle.getString("key.assignParking"));
        assignParkingNode.setOnAction(event -> handleAssignParkingSpot(circle, node));

        menu.getItems().add(assignParkingNode);
        menu.show(pathPane.getScene().getWindow(), e.getScreenX(), e.getScreenY());

    }

    private void handleAssignParkingSpot(Circle circle, NodeInfo node){
        try {
            if (Session.isLoggedIn()) {
                Session.getAccount().setParkingSpot(node);
                update();
            }
            else {
                App.showError(App.resourceBundle.getString("key.log_in_to_use_this_feature"), parentStackPane);
            }
        }
        catch (InvalidParameterException e) {
            App.showError(App.resourceBundle.getString("key.parking_spot_error"), parentStackPane);
        }
    }

    private void handlePlanNewPath() {
        pathDisplayControls.hide();
    }

    private void handleFindPath() {
        NodeInfo startNode = pathSelection.getSelectedStartNode();
        NodeInfo endNode = pathSelection.getSelectedEndNode();
        map.hideNodes();
        if (startNode != null && endNode != null) {
            findPath(startNode.getNodeID(), endNode.getNodeID());
        }
    }


    @FXML
    private void handleHelpButton(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help")));
        content.setBody(new Text(App.resourceBundle.getString("key.pathfinding_help")));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setOnAction(event -> errorWindow.close());
        closeButton.setStyle("-jfx-button-type: RAISED");

        content.setActions(closeButton);
        errorWindow.show();
    }


    @FXML
    private void handleExerciseHelpButton(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help")));
        content.setBody(new Text(App.resourceBundle.getString("key.exercise_help")));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setOnAction(event -> errorWindow.close());
        closeButton.setStyle("-jfx-button-type: RAISED");

        content.setActions(closeButton);
        errorWindow.show();
    }


    private void handleUnitSwitch(ActionEvent e) {
        textualDirections.toggleUnit(calculatedPath, directionIterator, floor);
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
        parkingSpots = new LinkedList<>();

        parkingSpotID = getSavedParkingSpot();

        List<NodeInfo> nodes = getAllNodes();

        /* redraw map nodes */
        map.clearShapes();
        map.drawNodes(nodes, floorComboBox.getValue());
        if (pathSelection.getState() == PathSelectionControls.SelectionState.IDLE) {
            map.hideNodes();
        }

        /* redraw path */
        if (calculatedPath != null) {
            map.drawPath(calculatedPath, floorComboBox.getValue(), pathDisplayControls.getDirectionIterator());
        }

        pathSelection.setLocations(nodes);
    }

    private static String getSavedParkingSpot() {
        String parkingSpotID = null;
        if (Session.isLoggedIn() && Session.getAccount() != null) {
            parkingSpotID = Session.getAccount().getParkingSpot();
        }
        return parkingSpotID;
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
        content.setHeading(new Text(App.resourceBundle.getString("key.help")));
        content.setBody(new Text(App.resourceBundle.getString("key.pathfinding_help")));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setOnAction(event -> errorWindow.close());
        closeButton.setStyle("-jfx-button-type: RAISED");

        content.setActions(closeButton);
        errorWindow.show();
    }

}