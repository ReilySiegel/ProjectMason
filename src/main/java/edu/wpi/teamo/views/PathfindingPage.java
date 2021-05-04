package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.algos.*;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PathfindingPage extends SubPageController implements Initializable {

    @FXML
    private JFXButton helpButton;

    @FXML
    private JFXComboBox<String> floorComboBox;
    String floor;

    @FXML
    private AnchorPane parentNode;

    @FXML
    private GridPane gridPane;

    @FXML
    private StackPane parentStackPane;

    @FXML
    JFXButton chooseStartButton;

    @FXML
    JFXButton chooseEndButton;

    @FXML
    JFXButton findPathButton;

    @FXML
    private AnchorPane pathPane;

    @FXML
    private VBox searchWindow;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private JFXListView<JFXCheckBox> searchResultsView;

    @FXML
    private JFXListView<String> textualDirView;

    @FXML
    private HBox algoSwitchWindow;

    @FXML
    private JFXComboBox<String> algoSwitcher;

    @FXML
    private VBox textualWindow;

    @FXML
    private JFXButton textualUnitsBtn;

    private LocationSearcher locationSearcher;

    private boolean metric = true;

    LinkedList<AlgoNode> calculatedPath = null;

    @FXML
    private JFXButton selectParkingSpotButton;

    String parkingSpotID = null;

    boolean selectingStart = false;
    boolean selectingEnd = false;

    String selectedStartID = null;
    String selectedEndID = null;

    Map map;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setPickOnBounds(false);

        setSelectParkingSpotButtonVisibility(false);

        initFloorSwitcher();
        initAlgoSwitcher();

        textualWindow.setVisible(false);

        selectParkingSpotButton.setOnAction(this::handleSelectParkingSpot);
        chooseStartButton.setOnAction(this::handleChooseStart);
        floorComboBox.setOnAction(this::handleFloorSwitch);
        chooseEndButton.setOnAction(this::handleChoseEnd);
        findPathButton.setOnAction(this::handleFindPath);
        algoSwitcher.setOnAction(this::handleAlgoSwitch);
        helpButton.setOnAction(this::handleHelpButton);

        locationSearcher = new LocationSearcher(searchBar, searchResultsView);
        locationSearcher.setOnCheckNode(this::onClickNode);
        setSearchWindowVisibility(false);

        App.getPrimaryStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                textualWindow.setVisible(false);
                searchWindow.setVisible(false);
                selectingStart = false;
                selectingEnd = false;
                setSearchWindowVisibility(false);
                chooseStartButton.setDisable(selectingStart);
                chooseEndButton.setDisable(selectingEnd);
                locationSearcher.clearSelectedLocations();
            }
        });

        map = new Map(pathPane);
        map.setOnDrawNode(this::onDrawNode);
        update();
    }

    private void handleSelectParkingSpot(ActionEvent actionEvent) {
        try {
            if (Session.isLoggedIn() && Session.getAccount().getParkingSpot() != null) {
                String id = Session.getAccount().getParkingSpot();
                NodeInfo node = App.mapService.getNode(id);
                onClickNode(node);
            }
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private void handleAlgoSwitch(ActionEvent actionEvent) {
        App.context.switchAlgoManagerByCode(algoSwitcher.getValue());
    }

    private void initFloorSwitcher() {
        floorComboBox.getItems().add(Map.floorL2Key);
        floorComboBox.getItems().add(Map.floorL1Key);
        floorComboBox.getItems().add(Map.floorGKey);
        floorComboBox.getItems().add(Map.floor1Key);
        floorComboBox.getItems().add(Map.floor2Key);
        floorComboBox.getItems().add(Map.floor3Key);
        floorComboBox.setValue(Map.floor1Key);
        floor = Map.floor1Key;
    }

    private void initAlgoSwitcher() {
        algoSwitchWindow.setVisible(Session.isLoggedIn() && Session.getAccount().isAdmin());

        algoSwitcher.getItems().add(Context.aStarCode);
        algoSwitcher.getItems().add(Context.dfsCode);
        algoSwitcher.getItems().add(Context.bfsCode);
        algoSwitcher.getItems().add(Context.bestFirstCode);
        algoSwitcher.getItems().add(Context.DijkstraCode);

        if (App.context.getAlgoManager().getClass() == DFSManager.class) {
            algoSwitcher.setValue(Context.dfsCode);
        }
        else if (App.context.getAlgoManager().getClass() == BFSManager.class) {
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
    }

    //    Consumer<NodeInfo> onClickNode = (NodeInfo node) -> System.out.println("Node " + node.getNodeID() + "was clicked");
    void onClickNode(NodeInfo node) {
        if (selectingStart) {
            chooseStartButton.setText(node.getShortName());
            selectedStartID = node.getNodeID();
            selectingStart = false;
        }
        if (selectingEnd) {
            chooseEndButton.setText(node.getShortName());
            selectedEndID = node.getNodeID();
            selectingEnd = false;
        }
        setSearchWindowVisibility(false);
        chooseStartButton.setDisable(selectingStart);
        chooseEndButton.setDisable(selectingEnd);
        locationSearcher.clearSelectedLocations();
        setSelectParkingSpotButtonVisibility(false);
    }

    private void setSelectParkingSpotButtonVisibility(boolean visible) {
        if (!Session.isLoggedIn() || Session.getAccount().getParkingSpot() == null) visible = false;
        selectParkingSpotButton.setVisible(visible);
        selectParkingSpotButton.setManaged(visible);
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
                showError(App.resourceBundle.getString("key.log_in_to_use_this_feature"));
            }
        }
        catch (InvalidParameterException e) {
            showError(App.resourceBundle.getString("key.parking_spot_error"));
        }

    }

    private void handleChooseStart(ActionEvent e) {
        if (selectingStart) {
            selectingStart = false;
        }
        else {
            selectingStart = true;
            selectingEnd = false;
        }
        setSearchWindowVisibility(true);
        chooseStartButton.setDisable(selectingStart);
        chooseEndButton.setDisable(selectingEnd);
        textualWindow.setVisible(false);
        setSelectParkingSpotButtonVisibility(true);
    }

    private void handleChoseEnd(ActionEvent e) {
        if (selectingEnd) {
            selectingEnd = false;
        }
        else {
            selectingStart = false;
            selectingEnd = true;
        }
        setSearchWindowVisibility(true);
        chooseEndButton.setDisable(selectingEnd);
        chooseStartButton.setDisable(selectingStart);
        textualWindow.setVisible(false);
        setSelectParkingSpotButtonVisibility(true);
    }

    private void handleFindPath(ActionEvent e) {
        if (selectedStartID != null && selectedEndID != null) {
            findPath(selectedStartID, selectedEndID);
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
    private void textualUnitsOnClick(MouseEvent e) {
        metric = !metric;
        if(metric) textualUnitsBtn.setText(App.resourceBundle.getString("key.units_metric"));
        else textualUnitsBtn.setText(App.resourceBundle.getString("key.units_us"));
        if(textualDirView.getItems().size() > 0 && calculatedPath != null) {
            textualDirView.getItems().remove(0, textualDirView.getItems().size() - 1);
            textualDirView.getItems().setAll(TextDirManager.getTextualDirections(calculatedPath, metric));
        }
    }

    void findPath(String startID, String endID) {

        LinkedList<AlgoNode> path = new LinkedList<>();
        try {
            path = App.context.getPath(startID, endID);
            if (path != null) {
                floor = path.get(0).getFloor();
                calculatedPath = path;
            }
        } catch (SQLException | NullPointerException throwables) {
            throwables.printStackTrace();
        }

        textualWindow.setVisible(true);
        textualDirView.getItems().setAll(TextDirManager.getTextualDirections(path, metric));

        List<String> floors = new LinkedList<>();
        for (AlgoNode node : path) {
            if (!floors.contains(node.getFloor())) {
                floors.add(node.getFloor());
            }
        }
        update();
    }

    void handleFloorSwitch(ActionEvent e) {
        floor =  floorComboBox.getValue();
        update();
    }

    void update() {

        if (Session.isLoggedIn() && Session.getAccount() != null) {
            parkingSpotID = Session.getAccount().getParkingSpot();
        }

        LinkedList<NodeInfo> nodes = new LinkedList<>();
        try {
            nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        map.clearShapes();
        map.drawNodes(nodes, floor);
        displayPath(calculatedPath);
        floorComboBox.setValue(floor);
        locationSearcher.setLocations(nodes);
    }

    public void displayPath(LinkedList<AlgoNode> path) {
        if (path != null) {
            map.drawPath(path, floor);
            StringBuilder text = new StringBuilder("Directions:\n");
            for (AlgoNode node : path) {
                text.append(node.getLongName()).append("\n");
            }
        }
    }

    private void setSearchWindowVisibility(boolean visible) {
        searchWindow.setVisible(visible);
    }

    void showError(String message) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.error")));
        content.setBody(new Text(message));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();
    }
}