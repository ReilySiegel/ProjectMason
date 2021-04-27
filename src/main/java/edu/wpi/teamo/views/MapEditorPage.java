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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
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

    NodeInfo addingEdgeStartNode = null;
    Line addingEdgeLine = null;
    boolean addingEdge = false;

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
        map.setOnMouseMoved(this::onMouseMoved);

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
    void onMapClicked(MouseEvent e) {
        int mapX = (int) Map.transform((int) e.getX(), nodePane.getPrefWidth(), Map.imageWidth);
        int mapY = (int) Map.transform((int) e.getY(), nodePane.getPrefWidth(), Map.imageWidth);

        addNodeX.setText(String.valueOf(mapX));
        addNodeY.setText(String.valueOf(mapY));

        String autoGenName = String.format("Floor_%s_%d_%d", selectedFloor, mapX, mapY);
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

        if (addingEdge) {
            nodePane.getChildren().remove(addingEdgeLine);
            addingEdgeStartNode = null;
            addingEdgeLine = null;
            addingEdge = false;
        }
        else {
            mapContextMenu(e, mapX, mapY);
        }

    }

    void onMouseMoved(Pair<Double, Double> p) {
        double x = p.getKey();
        double y = p.getValue();
        if (addingEdgeLine != null) {
            addingEdgeLine.setEndX(x);
            addingEdgeLine.setEndY(y);
        }
    }

    void onDrawNode(Pair<Circle, NodeInfo> p) {
            Circle circle = p.getKey();
            NodeInfo node = p.getValue();

            circle.setOnMouseEntered(event -> {
                circle.setRadius(circle.getRadius() * 2);
                event.consume();
            });

            circle.setOnMouseExited(event -> {
                circle.setRadius(circle.getRadius() / 2);
                event.consume();
            });

            circle.setOnMouseClicked((MouseEvent e) -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    onClickNode(e, circle, node);
                    e.consume();
                }
            });

            circle.setOnMouseDragged((MouseEvent e) -> {
                onDraggingNode(node, circle, e);
                e.consume();
            });

            circle.setOnMouseReleased((MouseEvent e) -> {
                if (dragging) {
                    onDraggedNodeDrop(node, e);
                }
                e.consume();
            });
    }

    private void nodeContextMenu(MouseEvent e, Circle circle, NodeInfo node) {
        ContextMenu menu = new ContextMenu();

        MenuItem deleteItem = new MenuItem(App.resourceBundle.getString("key.delete"));
        deleteItem.setOnAction(event -> confirmDeleteNode(node));

        MenuItem addEdgeItem = new MenuItem(App.resourceBundle.getString("key.add_edge"));
        addEdgeItem.setOnAction(event -> handleAddingEdge(circle, node));

        menu.getItems().add(addEdgeItem);
        menu.getItems().add(deleteItem);
        menu.show(nodePane.getScene().getWindow(), e.getScreenX(), e.getScreenY());
    }

    private void edgeContextMenu(MouseEvent e, EdgeInfo edge) {
        ContextMenu menu = new ContextMenu();

        MenuItem deleteItem = new MenuItem(App.resourceBundle.getString("key.delete"));
        deleteItem.setOnAction(event -> confirmDeleteEdge(edge));

        menu.getItems().add(deleteItem);
        menu.show(nodePane.getScene().getWindow(), e.getScreenX(), e.getScreenY());
    }

    private void mapContextMenu(MouseEvent e, Integer x, Integer y) {
        ContextMenu menu = new ContextMenu();

        MenuItem addNodeItem = new MenuItem(App.resourceBundle.getString("key.add_node"));
        addNodeItem.setOnAction(event -> handleAddingNode(x, y));

        menu.getItems().add(addNodeItem);
        menu.show(
                nodePane.getScene().getWindow(),
                e.getScreenX(),
                e.getScreenY()
        );
    }

    //    Consumer<NodeInfo> onClickNode = (NodeInfo node) -> System.out.println("Node " + node.getNodeID() + "was clicked");
    void onClickNode(MouseEvent e, Circle circle, NodeInfo node) {
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
        else if (selectingEnd) {
            chooseEndButton.setDisable(false);
            addNode2.setText(node.getNodeID());
            selectingEnd = false;
            addEdgeID.setText(addNode1.getText()+"_"+node.getNodeID());
        }
        else if (addingEdge && addingEdgeStartNode != null) {
            addEdge(addingEdgeStartNode, node);
            addingEdge = false;
            addingEdgeStartNode = null;
            nodePane.getChildren().remove(addingEdgeLine);
        }
        else {
            nodeContextMenu(e, circle, node);
        }
    }



    private void onDrawEdge(Pair<Line, EdgeInfo> p) {
        EdgeInfo edge = p.getValue();
        Line line = p.getKey();

        line.setOnMouseClicked(event -> {
            onClickEdge(event, edge);
            event.consume();
        });

        line.setOnMouseEntered(event -> {
            line.setStrokeWidth(line.getStrokeWidth() * 2);
            event.consume();
        });

        line.setOnMouseExited(event -> {
            line.setStrokeWidth(line.getStrokeWidth() / 2);
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
    void onClickEdge(MouseEvent e, EdgeInfo edge) {
        editingEdge.setText(edge.getEdgeID());
        editEdgeID.setText(edge.getEdgeID());
        editNode1.setText(edge.getStartNodeID());
        editNode2.setText(edge.getEndNodeID());
        deleteEdgeID.setText(edge.getEdgeID());

        edgeContextMenu(e, edge);
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
     * @param  event
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
            addNode(
                newNodeID,
                Integer.parseInt(newNodeX),
                Integer.parseInt(newNodeY),
                newNodeFloor,
                newNodeBuilding,
                newNodeType,
                newNodeLN,
                newNodeSN
            );

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
            showError(App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments"));
        }

    }

    private void addNode(String id, int x, int y, String fl, String bu, String ty, String ln, String sn) throws SQLException {
        App.mapService.addNode(id, x, y, fl, bu, ty, ln, sn);
        update();
    }

    /**
     * Event handler for removing a Node from the database
     * @param event
     */
    @FXML
    void handleDeleteSubmitNode(ActionEvent event) {
        String deleteNode= deleteNodeID.getText();

        try{
            deleteNode(deleteNode);
        }
        catch (SQLException | AssertionError | IllegalArgumentException e){
            showError(App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments"));
        }
    }

    private void confirmDeleteNode(NodeInfo node) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.confirm_node_deletion")));
        content.setBody(new Text(App.resourceBundle.getString("key.deleting_a_node") +
                App.resourceBundle.getString("key.ID_semicolon")+ node.getNodeID() + "\n" +
                App.resourceBundle.getString("key.name_semicolon") + node.getLongName() + "\n"));
        JFXDialog deleteConfirmationWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton cancelButton = new JFXButton(App.resourceBundle.getString("key.cancel"));
        cancelButton.setStyle("-fx-background-color: #004cff; -fx-text-fill: #ffffff");
        cancelButton.setOnAction(event -> deleteConfirmationWindow.close());

        JFXButton deleteButton = new JFXButton(App.resourceBundle.getString("key.delete"));
        deleteButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #ffffff");
        deleteButton.setOnAction(event -> {
            try {
                deleteNode(node.getNodeID());
            } catch (SQLException throwables) {
                showError(App.resourceBundle.getString("key.there_was_a_delete_error"));
                throwables.printStackTrace();
            }
            deleteConfirmationWindow.close();
        });

        content.setActions(cancelButton);
        content.setActions(deleteButton);
        deleteConfirmationWindow.show();
    }

    private void confirmDeleteEdge(EdgeInfo edge) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.confirm_edge_deletion")));
        content.setBody(new Text(App.resourceBundle.getString("key.deleting_an_edge") +
                App.resourceBundle.getString("key.ID_semicolon") + edge.getEdgeID() + "\n"));
        JFXDialog deleteConfirmationWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton cancelButton = new JFXButton(App.resourceBundle.getString("key.cancel"));
        cancelButton.setStyle("-fx-background-color: #004cff; -fx-text-fill: #ffffff");
        cancelButton.setOnAction(event -> deleteConfirmationWindow.close());

        JFXButton deleteButton = new JFXButton(App.resourceBundle.getString("key.delete"));
        deleteButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #ffffff");
        deleteButton.setOnAction(event -> {
            try {
                deleteEdge(edge.getEdgeID());
            } catch (SQLException throwables) {
                showError(App.resourceBundle.getString("key.there_was_a_delete_error"));
                throwables.printStackTrace();
            }
            deleteConfirmationWindow.close();
        });

        content.setActions(cancelButton);
        content.setActions(deleteButton);
        deleteConfirmationWindow.show();
    }

    private void handleAddingNode(Integer x, Integer y) {
        String defaultBulding = "building";
        String defaultType = "DEFAULT";
        String id = selectedFloor + "_" + x + "_" + y + "_" + "default";

        int count = 0;
        String numberedID = id;
        while (App.mapService.nodeExists(numberedID) && count < 1000) {
            numberedID = id + count;
            ++count;
        }
        id = numberedID;

        try {
            addNode(id, x, y, selectedFloor, defaultBulding, defaultType, id, id);
        } catch (SQLException throwables) {
            showError(App.resourceBundle.getString("key.error_saving_node"));
            throwables.printStackTrace();
        }
    }

    void handleAddingEdge(Circle circle, NodeInfo node) {
        addingEdgeLine = new Line(circle.getCenterX(), circle.getCenterY(), circle.getCenterX(), circle.getCenterY());
        addingEdgeLine.setStroke(Color.RED);
        addingEdgeLine.setStrokeWidth(2);
        addingEdgeLine.setMouseTransparent(true);
        addingEdgeStartNode = node;
        addingEdge = true;
        nodePane.getChildren().add(addingEdgeLine);
    }

    void addEdge(NodeInfo startNode, NodeInfo endNode) {
        String startID = startNode.getNodeID();
        String endID = endNode.getNodeID();
        String edgeID = startID + "_" + endID;
        addEdge(edgeID, startID, endID);
    }

    void addEdge(String edgeID, String startNodeID, String endNodeID) {
        try {
            App.mapService.addEdge(edgeID, startNodeID, endNodeID);
        } catch (SQLException | IllegalArgumentException e) {
            showError(App.resourceBundle.getString("key.error_saving_edge"));
        }
        update();
    }

    void deleteNode(String nodeID) throws SQLException {
        App.mapService.deleteNode(nodeID);
        update();
    }

    void deleteEdge(String edgeID) throws SQLException {
        App.mapService.deleteEdge(edgeID);
        update();
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
                showError(App.resourceBundle.getString("key.node_id_exists"));
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
            showError(App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments"));
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

        addEdge(newEdgeID, newEdgeNode1, newEdgeNode2);
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
            showError(App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments"));
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
                showError(App.resourceBundle.getString("key.edge_id_exists"));
                return;
            }
            App.mapService.setEdgeID(editingEdge.getText(), newEditEdgeID);
            App.mapService.setEdgeStartID(newEditEdgeID, editEdgeNode1);
            App.mapService.setEdgeEndID(newEditEdgeID, editEdgeNode2);
        } catch (SQLException | IllegalArgumentException e) {
            showError(App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments"));
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
            showError(App.resourceBundle.getString("key.select_a_valid_file"));
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
            showError(App.resourceBundle.getString("key.save_aborted"));
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
            showError(App.resourceBundle.getString("key.select_a_valid_file"));
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
            showError(App.resourceBundle.getString("key.save_aborted"));
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
        content.setHeading(new Text(App.resourceBundle.getString("key.error")));
        content.setBody(new Text(message));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

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

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_map_editor")));
        content.setBody(new Text(App.resourceBundle.getString("key.help_map_editor_text")));


        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
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