package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PathfindingPage extends SubPageController implements Initializable {

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXButton helpButton;

    @FXML
    private JFXComboBox<String> floorComboBox;
    String floor;

    @FXML
    private AnchorPane parentNode;

    @FXML
    private StackPane parentStackPane;

    @FXML
    JFXButton chooseStartButton;

    @FXML
    JFXButton chooseEndButton;

    @FXML
    JFXButton findPathButton;

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane pathPane;

    @FXML
    private AnchorPane searchWindow;

    @FXML
    private JFXTextField searchBar;

    @FXML
    private JFXListView<JFXCheckBox> searchResultsView;

    private LocationSearcher locationSearcher;

    LinkedList<AlgoNode> calculatedPath = null;

    boolean selectingStart = false;
    boolean selectingEnd = false;

    String selectedStartID = null;
    String selectedEndID = null;

    Map map;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        App.getPrimaryStage().getScene().getStylesheets().add(LocationSearcher.getStylePath());

        floorComboBox.getItems().add("L2");
        floorComboBox.getItems().add("L1");
        floorComboBox.getItems().add("G");
        floorComboBox.getItems().add("1");
        floorComboBox.getItems().add("2");
        floorComboBox.getItems().add("3");
        floorComboBox.setValue("1");
        floor = "1";

        chooseStartButton.setOnAction(this::handleChooseStart);
        floorComboBox.setOnAction(this::handleFloorSwitch);
        chooseEndButton.setOnAction(this::handleChoseEnd);
        findPathButton.setOnAction(this::handleFindPath);
        helpButton.setOnAction(this::handleHelpButton);
        backButton.setOnAction(this::backToMain);

        locationSearcher = new LocationSearcher(searchBar, searchResultsView);
        locationSearcher.setOnMakeCheckbox(this::onMakeCheckbox);
        locationSearcher.setOnCheckNode(this::onClickNode);
        setSearchWindowVisibility(false);

        map = new Map(imageView, pathPane);
        map.setOnDrawNode(this::onDrawNode);
        update();
    }

    void onDrawNode(Pair<Circle, NodeInfo> p) {
        Circle circle = p.getKey();
        NodeInfo node = p.getValue();

        circle.setOnMouseEntered(event -> {
            circle.setRadius(7);
            event.consume();
        });

        circle.setOnMouseExited(event -> {
            circle.setRadius(4);
            event.consume();
        });

        circle.setOnMousePressed(event -> {
            onClickNode(node);
            event.consume();
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
    }

    void onMakeCheckbox(JFXCheckBox checkBox) {
//        checkBox.getStylesheets().add();
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
    }

    private void handleFindPath(ActionEvent e) {
        if (selectedStartID != null && selectedEndID != null) {
            findPath(selectedStartID, selectedEndID);
        }
    }

    @FXML
    private void handleHelpButton(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Help"));
        content.setBody(new Text("Select a starting location and ending location to get directions.\n" +
                "1. Choose Start Location: Click on the button and then choose the node at or closest to your current location on the map.\n" +
                "2. Choose End Location: Click on the button and ten choose the node at or closest to destination.\n" +
                "3. Find Path: Click on the button to get directions on the map and text box on the right side of the map.\n"+
                "4. Switch Floor: Click on the button to switch between the floors that  are on your path.\n"));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);


        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #F40F19");
        closeButton.setOnAction(event -> errorWindow.close());

        content.setActions(closeButton);
        errorWindow.show();
    }

    void findPath(String startID, String endID) {

        LinkedList<AlgoNode> path = new LinkedList<>();
        try {
            path = App.IStrategyPathfinding.getPath(startID, endID);
            if (path != null) {
                floor = path.get(0).getFloor();
                calculatedPath = path;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException ignored) { }

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

}