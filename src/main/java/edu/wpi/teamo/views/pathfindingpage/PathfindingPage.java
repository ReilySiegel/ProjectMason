package edu.wpi.teamo.views.pathfindingpage;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.algos.*;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.views.LocationSearcher;
import edu.wpi.teamo.views.Map;
import edu.wpi.teamo.views.SubPageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private JFXComboBox<String> nodeTypeFilterBox;

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
    private JFXListView<HBox> textualDirView;

    @FXML
    private HBox algoSwitchWindow;

    @FXML
    private JFXComboBox<String> algoSwitcher;

    @FXML
    private VBox textualWindow;

    @FXML
    private JFXButton textualUnitsBtn;

    private boolean metric = true;

    LinkedList<AlgoNode> calculatedPath = null;

    @FXML
    private JFXButton selectParkingSpotButton;

    @FXML
    private JFXButton directForward;

    @FXML
    private JFXButton directBack;

    @FXML
    private HBox pathStepHBox;

    @FXML
    private HBox currentDisplay;

    PathSelection pathSelection;

    public List<Circle> parkingSpots;

    int directionIterator = 0;
    int directionMax = 0;
    int directionMin = 0;
    AlgoNode lastNode = null;

    String parkingSpotID = null;

    boolean selectingParkingSpot = false;

    edu.wpi.teamo.views.Map map;

    private List<HBox> viewableList;
    private List<List<HBox>> floorPaths;
    private int currentIndex;

    private String activeFloor;
    private int adjustedDirIterator;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        gridPane.setPickOnBounds(false);

        pathSelection = new PathSelection(chooseStartButton,
                                          chooseEndButton,
                                          searchWindow,
                                          searchBar,
                                          searchResultsView,
                                          nodeTypeFilterBox,
                                          this::handleSelectedStart,
                                          this::handleSelectedEnd,
                                          this::handleChoosing);

        setSelectParkingSpotButtonVisibility(Session.isLoggedIn());

        initFloorSwitcher();
        initAlgoSwitcher();

        textualWindow.setVisible(false);

        selectParkingSpotButton.setOnAction(this::handleSelectParkingSpot);
        floorComboBox.setOnAction(this::handleFloorSwitch);
        findPathButton.setOnAction(this::handleFindPath);
        algoSwitcher.setOnAction(this::handleAlgoSwitch);
        helpButton.setOnAction(this::handleHelpButton);
        directBack.setOnAction(this::handleStepBack);
        directForward.setOnAction(this::handleStepForward);

        viewableList = new LinkedList<>();
        floorPaths = new ArrayList<>();
        currentIndex = 0;

        App.getPrimaryStage().getScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                textualWindow.setVisible(false);
                pathSelection.reset();
            }
        });
        setPathStepButtonVisibility(false);
        map = new edu.wpi.teamo.views.Map(pathPane);
        map.setOnDrawNode(this::onDrawNode);
        update();
        map.hideNodes();
    }

    private void handleSelectedStart() {
        map.hideNodes();
    }

    private void handleSelectedEnd() {
        map.hideNodes();
    }

    private void handleChoosing() {
        map.showNodes();
    }

    private void setPathStepButtonVisibility(boolean visible) {
        directForward.setVisible(visible);
        directForward.setManaged(visible);
        directBack.setVisible(visible);
        directBack.setManaged(visible);
        pathStepHBox.setVisible(visible);
        pathStepHBox.setManaged(visible);
    }

    private void handleSelectParkingSpot(ActionEvent actionEvent) {
        try {
            if (pathSelection.getState() != PathSelection.SelectionState.IDLE) {
                if (Session.isLoggedIn() && Session.getAccount().getParkingSpot() != null) {
                    String id = Session.getAccount().getParkingSpot();
                    NodeInfo node = App.mapService.getNode(id);
                    onClickNode(node);
                }
            }
            else if (selectingParkingSpot) {
                selectingParkingSpot = false;
            }
            else {
                selectingParkingSpot = true;
                showParkingSpots();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleAlgoSwitch(ActionEvent actionEvent) {
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

    public void handleStepForward(ActionEvent e)
    {
        //lastNode = calculatedPath.get(directionIterator);
        if(directionIterator<directionMax-1) {
            stepDirection(true);
        }
    }

    public void handleStepBack(ActionEvent e)
    {
        //lastNode = calculatedPath.get(directionIterator);
        if(directionIterator>directionMin) {
            stepDirection(false);
        }
    }
    private void stepDirection(boolean forward)
    {
        if (forward)
        {
            directionIterator++;
            adjustedDirIterator++;
        }
        else{
            directionIterator--;
            adjustedDirIterator--;
        }
        floor = calculatedPath.get(directionIterator).getFloor();

        //Monitor
        if(!activeFloor.equals(floor)) {
            textualDirView.getItems().remove(0, textualDirView.getItems().size() - 1);
            if(adjustedDirIterator >= floorPaths.get(currentIndex).size() - 1) {
                currentIndex++;
                adjustedDirIterator = 0;
            }
            else {
                currentIndex--;
                adjustedDirIterator = floorPaths.get(currentIndex).size() - 1;
            }
            textualDirView.getItems().setAll(floorPaths.get(currentIndex));
            activeFloor = floor;
        }

        for(HBox h: textualDirView.getItems())
        {
            h.setBackground(null);
        }
        BackgroundFill fill  = new BackgroundFill(new Color(0,0,0,.10),null,null);
        textualDirView.getItems().get(adjustedDirIterator).setBackground(new Background(fill,null));
        updateCurrentDisplay();

        focusMapOnNode(calculatedPath.get(directionIterator));

        update();
    }

    private void updateCurrentDisplay() {
        ImageView icon = new ImageView(((ImageView) textualDirView.getItems().get(adjustedDirIterator).getChildren().get(0)).getImage());
        icon.setFitWidth(40);
        icon.setFitHeight(40);
        Text s = new Text(((Text) textualDirView.getItems().get(adjustedDirIterator).getChildren().get(1)).getText());
        if(s.getText().toLowerCase().contains(App.resourceBundle.getString("key.back_right_turn"))) icon.setRotate(90);
        else if(s.getText().toLowerCase().contains(App.resourceBundle.getString("key.back_left_turn"))) icon.setRotate(270);
        else if(s.getText().toLowerCase().contains(App.resourceBundle.getString("key.backwards"))) icon.setRotate(180);
        currentDisplay.getChildren().setAll(icon,s);
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

    private void handleFindPath(ActionEvent e) {
        NodeInfo startNode = pathSelection.getSelectedStartNode();
        NodeInfo endNode = pathSelection.getSelectedEndNode();

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
    private void textualUnitsOnClick(MouseEvent e) {
        metric = !metric;
        if(metric) textualUnitsBtn.setText(App.resourceBundle.getString("key.units_metric"));
        else textualUnitsBtn.setText(App.resourceBundle.getString("key.units_us"));
        if(textualDirView.getItems().size() > 0 && calculatedPath != null) {
            textualDirView.getItems().remove(0, textualDirView.getItems().size() - 1);
            populateTextualView(calculatedPath);
            updateCurrentDisplay();
        }
    }

    private void populateTextualView(List<AlgoNode> path) {
        int index = 0, floorPathsIndex = -1;
        floorPaths.clear();
        viewableList.clear();
        String currentFloor = "";
        List<String> directionsRaw = TextDirManager.getTextualDirections(path, metric);
        for(String s : directionsRaw) {
            if(!currentFloor.equals(path.get(index).getFloor())) {
                floorPaths.add(new LinkedList<>());
                floorPathsIndex++;
                currentFloor = path.get(index).getFloor();
            }
            index++;
            if(s.toLowerCase().contains(App.resourceBundle.getString("key.slight_left_turn"))) {
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-left-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
            else if(s.toLowerCase().contains(App.resourceBundle.getString("key.slight_right_turn"))) {
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-right-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
            else if(s.toLowerCase().contains(App.resourceBundle.getString("key.back_left_turn"))) {
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-left-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setRotate(270);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
            else if(s.toLowerCase().contains(App.resourceBundle.getString("key.back_right_turn"))) {
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-right-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setRotate(90);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
            else if(s.toLowerCase().contains(App.resourceBundle.getString("key.left_turn"))) {
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-sharp-left-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
            else if(s.toLowerCase().contains(App.resourceBundle.getString("key.right_turn"))) {
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-sharp-right-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
            else if(s.toLowerCase().contains(App.resourceBundle.getString("key.backwards"))) {
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-arrow-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setRotate(180);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
            else{
                Image ico = new Image("edu/wpi/teamo/images/Icons/icons8-up-arrow-96.png");
                ImageView icon = new ImageView();
                icon.setImage(ico);
                icon.setFitWidth(20);
                icon.setFitHeight(20);
                HBox hbox = new HBox(icon, new Text(s));
                viewableList.add(hbox);
                floorPaths.get(floorPathsIndex).add(hbox);
            }
        }
        textualDirView.getItems().setAll(floorPaths.get(currentIndex));
        if(activeFloor == null) activeFloor = path.get(0).getFloor();
    }

    void findPath(String startID, String endID) {
        currentIndex = 0;
        adjustedDirIterator = 0;
        activeFloor = null;
        LinkedList<AlgoNode> path = new LinkedList<>();
        try {
            path = App.context.getPath(startID, endID);
            if (path != null && path.size() > 0) {
                floor = path.get(0).getFloor();
                focusMapOnNode(path.get(0));
                calculatedPath = path;
            }
        } catch (SQLException | NullPointerException throwables) {
            System.out.println("No path calculated");
        }

        textualWindow.setVisible(true);
        populateTextualView(path);

        List<String> floors = new LinkedList<>();
        for (AlgoNode node : path) {
            if (!floors.contains(node.getFloor())) {
                floors.add(node.getFloor());
            }
        }
        directionIterator = 0;
        directionMax = calculatedPath.size();
        setPathStepButtonVisibility(true);
        stepDirection(true);
        stepDirection(false);

        update();
    }

    void focusMapOnNode(AlgoNode node) {
        map.centerOnMapCoords(node.getX(), node.getY());
    }

    void handleFloorSwitch(ActionEvent e) {
        floor =  floorComboBox.getValue();
        update();
    }

    void update() {
        parkingSpots = new LinkedList<>();

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

        pathSelection.setLocations(nodes);
        floorComboBox.setValue(floor);

        if (pathSelection.getState() == PathSelection.SelectionState.IDLE) {
            map.hideNodes();
        }
    }

    public void displayPath(LinkedList<AlgoNode> path) {
        if (path != null) {
            map.drawPath(path, floor,directionIterator);
            StringBuilder text = new StringBuilder("Directions:\n");
            for (AlgoNode node : path) {
                text.append(node.getLongName()).append("\n");
            }
        }
    }
}