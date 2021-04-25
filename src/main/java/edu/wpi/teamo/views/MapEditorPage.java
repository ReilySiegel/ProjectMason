package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.validation.NumberValidator;
import edu.wpi.teamo.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.teamo.database.map.Edge;
import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.Node;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;

import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

public class MapEditorPage extends SubPageController implements Initializable{

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTreeTableView<Node> nodeTree;
    private NodeTable nodeTable;

    @FXML
    private JFXTreeTableView<Edge> edgeTree;
    private EdgeTable edgeTable;

    @FXML
    private JFXTextField addNodeID;

    @FXML
    private JFXTextField addNodeBuilding;

    @FXML
    private JFXTextField addNodeFloor;

    @FXML
    private JFXTextField addNodeType;

    @FXML
    private JFXTextField addNodeLN;

    @FXML
    private JFXTextField addNodeSN;

    @FXML
    private JFXTextField addNodeX;

    @FXML
    private JFXTextField addNodeY;

    @FXML
    private JFXTextField origNodeID;

    @FXML
    private JFXTextField newNodeID;

    @FXML
    private JFXTextField origNodeX;

    @FXML
    private JFXTextField origNodeY;

    @FXML
    private JFXTextField origNodeBuilding;

    @FXML
    private JFXTextField origNodeFloor;

    @FXML
    private JFXTextField origNodeType;

    @FXML
    private JFXTextField origNodeLN;

    @FXML
    private JFXTextField origNodeSN;

    @FXML
    private JFXButton editNodeSubmit;

    @FXML
    private JFXTextField deleteNodeID;

    @FXML
    private JFXTextField addEdgeID;

    @FXML
    private JFXTextField addNode1;

    @FXML
    private JFXTextField addNode2;

    @FXML
    private JFXTextField editEdgeID;

    @FXML
    private JFXTextField editNode1;

    @FXML
    private JFXTextField editNode2;

    @FXML
    private JFXTextField deleteEdgeID;

    @FXML
    private JFXTextField editingEdge;

    @FXML
    private JFXTextField editingStart;

    @FXML
    private JFXTextField editingEnd;

    @FXML
    private JFXComboBox<String> floorSwitcher;
    String selectedFloor = "1";

    @FXML
    private ImageView mapImage;

    @FXML
    private AnchorPane nodePane;

    @FXML
    JFXButton chooseStartButton;
    boolean selectingStart;

    @FXML
    JFXButton chooseEndButton;
    boolean selectingEnd;

    Map map;
    boolean dragging = false;

    /* these hash maps are only for moving an associated edge when a node is being dragged */
    HashMap<String, List<Line>> associatedEdgeBeginnings;
    HashMap<String, List<Line>> associatedEdgeEndings;

    /**
     * Set validators to insure that the x and y coordinate fields are numbers
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        floorSwitcher.getItems().add("L2");
        floorSwitcher.getItems().add("L1");
        floorSwitcher.getItems().add("G");
        floorSwitcher.getItems().add("1");
        floorSwitcher.getItems().add("2");
        floorSwitcher.getItems().add("3");
        floorSwitcher.setValue(selectedFloor);

        chooseStartButton.setOnAction(event -> handleChooseStart());
        chooseEndButton.setOnAction(event -> handleChoseEnd());

        map = new Map(mapImage, nodePane);
        map.setOnMapClicked(this::onMapClicked);
        map.setOnDrawNode(this::onDrawNode);
        map.setOnDrawEdge(this::onDrawEdge);

        NumberValidator numberValidator = new NumberValidator();

        //editNodeSubmit.setDisable(true);

        //Ensure that each X and Y field are numbers
        addNodeX.getValidators().add(numberValidator);
        //numberValidator.setMessage("Please enter a number");
        addNodeX.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    addNodeX.validate();
                }
            }
        });

        addNodeY.getValidators().add(numberValidator);
        addNodeY.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    addNodeY.validate();
                }
            }
        });

        origNodeX.getValidators().add(numberValidator);
        origNodeX.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    origNodeX.validate();
                }
            }
        });

        origNodeY.getValidators().add(numberValidator);
        origNodeY.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    origNodeY.validate();
                }
            }
        });

        nodeTable = new NodeTable(nodeTree);
        nodeTable.initClickListener(
                origNodeID,
                newNodeID,
                origNodeX,
                origNodeY,
                origNodeBuilding,
                origNodeFloor,
                origNodeType,
                origNodeLN,
                origNodeSN,
                deleteNodeID
        );

        edgeTable = new EdgeTable(edgeTree);
        edgeTable.initClickListener(
                editingEdge,
                editEdgeID,
                editNode1,
                editNode2,
                deleteEdgeID
        );

        update();
    }

    void update() {
        updateNodeTreeDisplay();
        updateEdgeTreeDisplay();
        updateMap();
    }

    void updateMap() {
        associatedEdgeBeginnings = new HashMap<>();
        associatedEdgeEndings = new HashMap<>();
        try {
            List<NodeInfo> nodeList = App.mapService.getAllNodes().collect(Collectors.toList());
            List<EdgeInfo> edgeList = App.mapService.getAllEdges().collect(Collectors.toList());
            map.clearShapes();
            map.drawEdges(nodeList, edgeList, selectedFloor);
            map.drawNodes(nodeList, selectedFloor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //TODO get rid of - add event listener
    @FXML
    void onFloorSwitch(ActionEvent event) {
        selectedFloor = floorSwitcher.getValue();
        updateMap();
    }

    //    Consumer<Pair<Integer, Integer>> onClickEdge = (Pair<Integer, Integer> coords) -> System.out.println("Edge " + node.getEdgeID() + "was clicked");
    void onMapClicked(Pair<Integer, Integer> coords) {
        addNodeX.setText(String.valueOf(coords.getKey()));
        addNodeY.setText(String.valueOf(coords.getValue()));

        String autoGenName = String.format("Floor_%s_%d_%d", selectedFloor, coords.getKey(), coords.getValue());
        addNodeFloor.setText(selectedFloor);
        if (addNodeID.getText().isEmpty()) {
            addNodeID.setText(autoGenName);
        }
        if (addNodeBuilding.getText().isEmpty()) {
            addNodeBuilding.setText(String.format("Floor_%s_default", selectedFloor));
        }
        if (addNodeType.getText().isEmpty()) {
            addNodeType.setText("PARK");
        }
        if (addNodeLN.getText().isEmpty()) {
            addNodeLN.setText(autoGenName);
        }
        if (addNodeSN.getText().isEmpty()) {
            addNodeSN.setText(autoGenName);
        }
    }

    void onDrawNode(Pair<Circle, NodeInfo> p) {
            Circle circle = p.getKey();
            NodeInfo node = p.getValue();
            double originalRadius = circle.getRadius();

            circle.setOnMouseEntered(event -> {
                circle.setRadius(7);
                event.consume();
            });

            circle.setOnMouseExited(event -> {
                circle.setRadius(originalRadius);
                event.consume();
            });

            circle.setOnMousePressed(event -> {
                onClickNode(node);
                event.consume();
            });

            circle.setOnMouseDragged((MouseEvent e) -> {
                onDraggingNode(node, circle, e);
                e.consume();
            });

            circle.setOnMouseReleased((MouseEvent e) -> {
                if (dragging) {
                    onDraggedNodeDrop(node, e);
                }
            });

    }


//    Consumer<NodeInfo> onClickNode = (NodeInfo node) -> System.out.println("Node " + node.getNodeID() + "was clicked");
    void onClickNode(NodeInfo node) {
        origNodeID.setText(node.getNodeID());
        newNodeID.setText(node.getNodeID());
        origNodeX.setText(Integer.toString(node.getXPos()));
        origNodeY.setText(Integer.toString(node.getYPos()));
        origNodeBuilding.setText(node.getBuilding());
        origNodeFloor.setText(node.getFloor());
        origNodeType.setText(node.getNodeType());
        origNodeLN.setText(node.getLongName());
        origNodeSN.setText(node.getShortName());
        deleteNodeID.setText(node.getNodeID());

        if (selectingStart) {
            chooseStartButton.setDisable(false);
            addNode1.setText(node.getNodeID());
            selectingStart = false;
            addEdgeID.setText(node.getNodeID()+"_"+addNode2.getText());
        }
        if (selectingEnd) {
            chooseEndButton.setDisable(false);
            addNode2.setText(node.getNodeID());
            selectingEnd = false;
            addEdgeID.setText(addNode1.getText()+"_"+node.getNodeID());
        }
    }

    private void onDrawEdge(Pair<Line, EdgeInfo> p) {
        EdgeInfo edge = p.getValue();
        Line line = p.getKey();

        double originalWidth = line.getStrokeWidth();

        line.setOnMouseClicked(event -> {
            onClickEdge(edge);
            event.consume();
        });

        line.setOnMouseEntered(event -> {
            line.setStrokeWidth(originalWidth * 2);
            event.consume();
        });

        line.setOnMouseExited(event -> {
            line.setStrokeWidth(originalWidth);
            event.consume();
        });

        /* add the line to node id hashmaps so that this line can be referenced when the node is dragged */
        try {
            associatedEdgeBeginnings.computeIfAbsent(edge.getStartNodeID(), k -> new LinkedList<>());
            associatedEdgeBeginnings.get(edge.getStartNodeID()).add(line);

            associatedEdgeEndings.computeIfAbsent(edge.getEndNodeID(), k -> new LinkedList<>());
            associatedEdgeEndings.get(edge.getEndNodeID()).add(line);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    void onDraggingNode(NodeInfo node, Circle circle, MouseEvent e) {
        dragging = true;

        /* move the circle */
        circle.setCenterX(e.getX());
        circle.setCenterY(e.getY());

        /* move connected edges */
        try {
            List<Line> lineBeginnings = associatedEdgeBeginnings.get(node.getNodeID());
            if (lineBeginnings != null) {
                for (Line line : lineBeginnings) {
                    line.setStartX(e.getX());
                    line.setStartY(e.getY());
                }
            }
            List<Line> lineEndings = associatedEdgeEndings.get(node.getNodeID());
            if (lineEndings != null) {
                for (Line line : lineEndings) {
                    line.setEndX(e.getX());
                    line.setEndY(e.getY());
                }
            }
        }
        catch (Exception err) {
            err.printStackTrace();
        }

    }

    void onDraggedNodeDrop(NodeInfo node, MouseEvent e) {
        int mapX = (int) Map.transform((int) e.getX(), nodePane.getPrefWidth(), Map.imageWidth);
        int mapY = (int) Map.transform((int) e.getY(), nodePane.getPrefHeight(), Map.imageHeight);
        try {
            App.mapService.setNodePosition(node.getNodeID(), mapX, mapY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dragging = false;
        update();
    }

    //    Consumer<EdgeInfo> onClickEdge = (EdgeInfo edge) -> System.out.println("Edge " + node.getEdgeID() + "was clicked");
    void onClickEdge(EdgeInfo edge) {
        editingEdge.setText(edge.getEdgeID());
        editEdgeID.setText(edge.getEdgeID());
        editNode1.setText(edge.getStartNodeID());
        editNode2.setText(edge.getEndNodeID());
        deleteEdgeID.setText(edge.getEdgeID());
    }

    private void handleChooseStart() {
        if (selectingStart) {
            selectingStart = false;
        }
        else {
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
            selectingStart = false;
            selectingEnd = true;
        }
        chooseEndButton.setDisable(selectingEnd);
        chooseStartButton.setDisable(selectingStart);
    }

    /**
     * Event handler for adding a Node to the database
     * @param event
     */
    @FXML
    void handleAddSubmitNode(ActionEvent event) {
        String newNodeID = addNodeID.getText();
        String newNodeX = addNodeX.getText();
        String newNodeY = addNodeY.getText();
        String newNodeBuilding = addNodeBuilding.getText();
        String newNodeFloor = addNodeFloor.getText();
        String newNodeType = addNodeType.getText();
        String newNodeLN = addNodeLN.getText();
        String newNodeSN = addNodeSN.getText();


        try{
            App.mapService.addNode(newNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY),
                    newNodeFloor, newNodeBuilding, newNodeType,
                    newNodeLN, newNodeSN);
            update();

            addNodeID.setText("");
            addNodeX.setText("");
            addNodeY.setText("");
            addNodeBuilding.setText("");
            addNodeFloor.setText("");
            addNodeType.setText("");
            addNodeLN.setText("");
            addNodeSN.setText("");

        }
        catch(SQLException | AssertionError | IllegalArgumentException e){
            showError("Please fill out all fields with valid arguments");
        }

    }

    /**
     * Event handler for removing a Node from the database
     * @param event
     */
    @FXML
    void handleDeleteSubmitNode(ActionEvent event) {
        String deleteNode= deleteNodeID.getText();

        try{
            App.mapService.deleteNode(deleteNode);
            update();
        }
        catch (SQLException | AssertionError | IllegalArgumentException e){
            showError("Please fill out all fields with valid arguments");
        }
    }

    /**
     * Event handler for writing node edits to the database
     * @param event
     */
    @FXML
    void handleEditSubmitNode(ActionEvent event) {
        String currentNodeID = origNodeID.getText();
        String editNodeID = newNodeID.getText();
        String newNodeX = origNodeX.getText();
        String newNodeY = origNodeY.getText();
        String newNodeBuild = origNodeBuilding.getText();
        String newNodeFloor = origNodeFloor.getText();
        String newNodeType = origNodeType.getText();
        String newNodeLN = origNodeLN.getText();
        String newNodeSN = origNodeSN.getText();


        try{
            if(App.mapService.nodeExists(editNodeID) && !currentNodeID.equals(editNodeID)){
                showError("The Node ID already exists.");
                return;
            }
            App.mapService.setNodePosition(currentNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY));
            App.mapService.setNodeBuilding(currentNodeID, newNodeBuild);
            App.mapService.setNodeFloor(currentNodeID, newNodeFloor);
            App.mapService.setNodeType(currentNodeID, newNodeType);
            App.mapService.setNodeLongName(currentNodeID, newNodeLN);
            App.mapService.setNodeShortName(currentNodeID, newNodeSN);
            App.mapService.setNodeID(currentNodeID, editNodeID);

            update();
        }
        catch (SQLException | IllegalArgumentException | AssertionError e){
            showError("Please fill out all fields with valid arguments");
        }
    }



    /**
     * handles editing an edge
     * @param event clicking submit button
     */
    @FXML
    void handleAddSubmitEdge(ActionEvent event) {
        String newEdgeID = addEdgeID.getText();
        String newEdgeNode1 = addNode1.getText();
        String newEdgeNode2 = addNode2.getText();

        // try/catch for SQL and adding an edge
        try {
            App.mapService.addEdge(newEdgeID, newEdgeNode1, newEdgeNode2);
        } catch (SQLException | IllegalArgumentException e) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        update();
    }

    /**
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleDeleteSubmitEdge(ActionEvent event) {
        String EdgetoDelete = deleteEdgeID.getText();

        // try/catch for SQL and deleting an edge
        try {
            App.mapService.deleteEdge(EdgetoDelete);
        } catch (SQLException | IllegalArgumentException e) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        update();
    }

    /**
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleEditSubmitEdge(ActionEvent event) {
        String newEditEdgeID = editEdgeID.getText();
        String editEdgeNode1 = editNode1.getText();
        String editEdgeNode2 = editNode2.getText();

        // try catch for editing edge
        try {

            if(App.mapService.edgeExists(newEditEdgeID) && !newEditEdgeID.equals(editingEdge.getText())){
                showError("This Edge ID already exists.");
                return;
            }
            App.mapService.setEdgeID(editingEdge.getText(), newEditEdgeID);
            App.mapService.setEdgeStartID(newEditEdgeID, editEdgeNode1);
            App.mapService.setEdgeEndID(newEditEdgeID, editEdgeNode2);
        } catch (SQLException | IllegalArgumentException e) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        update();
    }

    /**
     * Event handler to load in and display a Node csv file
     * @param event
     */
    @FXML
    void handleLoadNodeCSV(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));


        try{
            File f = fc.showOpenDialog(null);
            String path = f.getPath();
            App.mapService.loadNodesFromFile(path);
            //App.mapService.loadNodesFromFile("src/test/resources/edu/wpi/teamo/map/database/testNodes.csv");
        }
        catch(NullPointerException | FileNotFoundException | SQLException | IllegalArgumentException e){
            showError("Please select a valid file");
            return;
        }

        update();
    }

    /**
     * Event handler for saving a csv file
     * @param event
     */
    @FXML
    void handleSaveNodeCSV(ActionEvent event){
        FileChooser fc = new FileChooser();
        fc.setTitle("Save CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        try{
            File f = fc.showSaveDialog(null);
            String path = f.getPath();
            App.mapService.writeNodesToCSV(path);
        }
        catch(NullPointerException | SQLException | IOException e){
            showError("Save aborted!");
        }
    }

    /**
     * handles reading in CSV from file
     *
     * @param event click load button
     */
    @FXML
    void handleLoadEdgeCSV(ActionEvent event) {
        // reading in file
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        // try catch for reading in file
        try {
            File f = fc.showOpenDialog(null);
            String path = f.getPath();
            App.mapService.loadEdgesFromFile(path);
        } catch (FileNotFoundException | SQLException | NullPointerException | IllegalArgumentException e)  {
            showError("Please select a valid file");
            return;
        }
        update();
    }

    @FXML
    void handleSaveEdgeCSV(ActionEvent event) {
        // choosing where to save
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        // try catch for SQL/IO errors
        try {
            File f = fc.showSaveDialog(null);
            String path = f.getPath();
            App.mapService.writeEdgesToCSV(path);
        } catch (IOException | SQLException | NullPointerException e ) {
            showError("Save aborted!");
        }
    }

    @FXML
    void updateNodeTreeDisplay() {
        Stream<NodeInfo> nodeStream = null;
        try {
            nodeStream = App.mapService.getAllNodes();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        nodeTable.update(nodeStream);
    }

    @FXML
    void updateEdgeTreeDisplay() {
        Stream<EdgeInfo> edgeStream = null;
        try {
            edgeStream = App.mapService.getAllEdges();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        edgeTable.update(edgeStream);
    }

    @FXML
    void showError(String message) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Error"));
        content.setBody(new Text(message));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
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

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Help - Map Editor"));
        content.setBody(new Text("*ADDING NODES*\n" +
                "To add a node to the map, switch to the \"Add\" tab on the right.\n" +
                "The Node ID field must be unique.\n" +
                "Clicking on an unoccupied area of the map will automatically fill in the necessary inputs.\n" +
                "In order for the node to be displayed, the floor field must be one of: L2, L1, G, 1, 2, 3\n" +
                "The X and Y fields must be numeric, the best way to set them is clicking the map.\n" +
                "Click \"Submit\" to add the node to the database.\n" +
                "\n*ADDING EDGES*:\n" +
                "To add a path (edge) between two nodes, first click the \"Choose Start\" button under the edge fields. \n" +
                "The button will become dark.  Click the desired starting node on the map, and it's ID will automatically be written to the start node field. \n" +
                "Repeat this process with the \"Choose End\" button to select the end node. An ID will be automatically generated. The ID can be changed, but must be unique." +
                "Click \"Submit\", and the edge will be created.\n" +
                "\n*EDITING NODES AND EDGES*\n" +
                "To edit a node or edge, switch to the \"edit\" tab on the upper right sidebar.\n" +
                "Clicking a node or edge on the map will fill it's information into the editing fields.\n" +
                "Change the fields you would like to be changed, and click the submit button under those fields.\n" +
                "If you change the ID, it must be unique.\n" +
                "\n*DELETING NODES AND EDGES*\n" +
                "To delete a node or edge, switch to the \"Delete\" tab on the upper right. \n" +
                "Click the node or edge on the map that you wish to delete, and its ID will be filled into the input.\n" +
                "Click the delete button under that field, and it will be permanently deleted from the database."));


        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #ffffff");
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