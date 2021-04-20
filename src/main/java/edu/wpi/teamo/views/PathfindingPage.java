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
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.awt.*;
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
    private JFXButton stepFloorButton;

    @FXML
    private JFXComboBox<String> endDropdown;

    @FXML
    private JFXComboBox<String> startDropdown;

    @FXML
    private JFXComboBox<String> switchFloor;
    String floor;

    @FXML
    private StackPane stackPane;

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
    private JFXTextArea pathText;

    @FXML
    private JFXTextArea mapText;

    LinkedList<AlgoNode> calculatedPath = null;

    boolean selectingStart = false;
    boolean selectingEnd = false;

    String selectedStartID = null;
    String selectedEndID = null;

    Map map;

    //    Consumer<NodeInfo> onClickNode = (NodeInfo node) -> System.out.println("Node " + node.getNodeID() + "was clicked");
    void onClickNode(NodeInfo node) {
        if (selectingStart) {
            startDropdown.setValue(node.getNodeID());
            selectedStartID = node.getNodeID();
            selectingStart = false;
        }
        if (selectingEnd) {
            endDropdown.setValue(node.getNodeID());
            selectedEndID = node.getNodeID();
            selectingEnd = false;
        }
        chooseStartButton.setDisable(selectingStart);
        chooseEndButton.setDisable(selectingEnd);
    }

    //    Consumer<EdgeInfo> onClickEdge = (EdgeInfo edge) -> System.out.println("Edge " + node.getEdgeID() + "was clicked");
    void onClickEdge(EdgeInfo edge) {
    }

    private void handleChooseStart() {
        if (selectingStart) {
            selectingStart = false;
        }
        else {
            mapText.setText("Select your start location.");
            selectingStart = true;
            selectingEnd = false;
        }

        chooseStartButton.setDisable(selectingStart);
        chooseEndButton.setDisable(selectingEnd);
    }

    private void handleChoseEnd() {
        if (selectingEnd) {
            selectingEnd = false;
        }
        else {
            mapText.setText("Select your destination.");
            selectingStart = false;
            selectingEnd = true;
        }
        System.out.println(selectingEnd);
        chooseEndButton.setDisable(selectingEnd);
        chooseStartButton.setDisable(selectingStart);
    }

    void handleStartDropdown() {
        selectedStartID = startDropdown.getValue();
    }

    void handleEndDropdown() {
        selectedEndID = endDropdown.getValue();
    }

    private void handleFindPath() {
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
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);


        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #F40F19");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        errorWindow.close();
                                    }
                                });

        content.setActions(closeButton);
        errorWindow.show();
    }

    void findPath(String startID, String endID) {

        LinkedList<AlgoNode> path = new LinkedList<>();
        try {
            path = App.aStarService.getPath(startID, endID);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (path != null) {
            floor = path.get(0).getFloor();
            calculatedPath = path;

            List<String> floors = new LinkedList<>();
            for (AlgoNode node : path) {
                if (!floors.contains(node.getFloor())) {
                    floors.add(node.getFloor());
                }
            }

        }

        updateMap();
    }

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        switchFloor.getItems().add("L2");
        switchFloor.getItems().add("L1");
        switchFloor.getItems().add("Ground Floor");
        switchFloor.getItems().add("1");
        switchFloor.getItems().add("2");
        switchFloor.getItems().add("3");
        switchFloor.setValue("1");
        floor = "1";

        pathText.setText("No path.");

        chooseStartButton.setOnAction(event -> handleChooseStart());
        chooseEndButton.setOnAction(event -> handleChoseEnd());
        findPathButton.setOnAction(event -> handleFindPath());
        startDropdown.setOnAction(event -> handleStartDropdown());
        endDropdown.setOnAction(event -> handleEndDropdown());
        backButton.setOnAction(this::backToMain);

        map = new Map(imageView, pathPane, mapText, this::onClickNode, this::onClickEdge);
        updateMap();

        try {
            LinkedList<String> nodeShortNames = new LinkedList<>();
            LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
            for (NodeInfo i : nodes) {
                nodeShortNames.add(i.getNodeID());
            }

            for (String nodeName : nodeShortNames) {
                startDropdown.getItems().add(nodeName);
                endDropdown.getItems().add(nodeName);
            }

        } catch (Exception SQLException) {
            return;
        }
    }

    @FXML
    void MapSwitch(ActionEvent event) {
        floor =  switchFloor.getValue();
        updateMap();
    }

    void updateMap() {
        LinkedList<NodeInfo> nodes = new LinkedList<>();
        try {
            nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        map.drawFloorNodes(nodes, floor);

        if (calculatedPath != null) {
            displayPath(calculatedPath);
        }
    }

    public void displayPath(LinkedList<AlgoNode> path) {
        if (path != null) {
            map.drawPath(path, floor);
            StringBuilder text = new StringBuilder("Directions:\n");
            for (AlgoNode node : path) {
                text.append(node.getLongName()).append("\n");
            }
            pathText.setText(text.toString());
        }
        else {
            pathText.setText("No path.");
        }
    }

}