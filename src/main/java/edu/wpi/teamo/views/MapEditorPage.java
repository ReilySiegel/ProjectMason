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
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class MapEditorPage extends SubPageController implements Initializable{

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTreeTableView<Node> treeView;

    @FXML
    private JFXTreeTableView<Edge> treeViewEdge;

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
    private Canvas mapCanvas;

    @FXML
    private ImageView mapImage;

    Map map;


    boolean treeInit = false;

    boolean treeEdgeInit = false;

    //TODO get rid of - add event listener
    @FXML
    void onFloorSwitch(ActionEvent event) {
        selectedFloor = floorSwitcher.getValue();
        updateMap();
    }

    /**
     * Set validators to insure that the x and y coordinate fields are numbers
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){

        floorSwitcher.getItems().add("L1");
        floorSwitcher.getItems().add("L2");
        floorSwitcher.getItems().add("1");
        floorSwitcher.getItems().add("2");
        floorSwitcher.getItems().add("3");
        floorSwitcher.getItems().add("4");

        map = new Map(mapCanvas, mapImage);

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


        //Set original node ID, X, and Y in the Edit and Delete box to selected value.
        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Node>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Node>> observable, TreeItem<Node> oldValue, TreeItem<Node> newValue) {
                if(newValue != null){
                    origNodeID.setText(newValue.getValue().getNodeID());
                    origNodeX.setText(Integer.toString(newValue.getValue().getXPos()));
                    origNodeY.setText(Integer.toString(newValue.getValue().getYPos()));
                    origNodeBuilding.setText(newValue.getValue().getBuilding());
                    origNodeFloor.setText(newValue.getValue().getFloor());
                    origNodeType.setText(newValue.getValue().getNodeType());
                    origNodeLN.setText(newValue.getValue().getLongName());
                    origNodeSN.setText(newValue.getValue().getShortName());
                    deleteNodeID.setText(newValue.getValue().getNodeID());
                    //editNodeSubmit.setDisable(false);
                }
            }
        });

        //Set original node ID, X, and Y in the Edit and Delete box to selected value.
        treeViewEdge.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Edge>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Edge>> observable, TreeItem<Edge> oldValue, TreeItem<Edge> newValue) {
                if(newValue != null){
                    editingEdge.setText(newValue.getValue().getEdgeID());
                    editEdgeID.setText(newValue.getValue().getEdgeID());
                    editNode1.setText(newValue.getValue().getStartNodeID());
                    editNode2.setText(newValue.getValue().getEndNodeID());
                    deleteEdgeID.setText(newValue.getValue().getEdgeID());
                }
            }
        });


        //Update display at start so loaded database persists after switching pages
        updateNodeTreeDisplay();
        updateEdgeTreeDisplay();
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
            updateNodeTreeDisplay();

        }
        catch(SQLException | NumberFormatException | AssertionError e){
            showError("Please fill out all fields with valid arguments");
            return;
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
            updateNodeTreeDisplay();
        }
        catch (SQLException | AssertionError e){
            showError("Please fill out all fields with valid arguments");
            return;
        }
    }

    /**
     * Event handler for writing node edits to the database
     * @param event
     */
    @FXML
    void handleEditSubmitNode(ActionEvent event) {
        String currentNodeID = origNodeID.getText();
        String newNodeX = origNodeX.getText();
        String newNodeY = origNodeY.getText();
        String newNodeBuild = origNodeBuilding.getText();
        String newNodeFloor = origNodeFloor.getText();
        String newNodeType = origNodeType.getText();
        String newNodeLN = origNodeLN.getText();
        String newNodeSN = origNodeSN.getText();

        try{
            App.mapService.setNodePosition(currentNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY));
            App.mapService.setNodeBuilding(currentNodeID, newNodeBuild);
            App.mapService.setNodeFloor(currentNodeID, newNodeFloor);
            App.mapService.setNodeType(currentNodeID, newNodeType);
            App.mapService.setNodeLongName(currentNodeID, newNodeLN);
            App.mapService.setNodeShortName(currentNodeID, newNodeSN);

            updateNodeTreeDisplay();
        }
        catch (SQLException | NumberFormatException | AssertionError e){
            showError("Please fill out all fields with valid arguments");
            return;
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
        } catch (Exception SQLException) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        updateEdgeTreeDisplay();
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
        } catch (Exception SQLException) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        // updating the edge display
        updateEdgeTreeDisplay();
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

        // try catch for reading in file
        try {
            App.mapService.setEdgeID(editingEdge.getText(), newEditEdgeID);
            App.mapService.setEdgeStartID(newEditEdgeID, editEdgeNode1);
            App.mapService.setEdgeEndID(newEditEdgeID, editEdgeNode2);
        } catch (Exception FileNotFoundException) {
            showError("Please fill out all fields with valid arguments");
            return;
        }
        updateEdgeTreeDisplay();
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
        catch(NullPointerException | FileNotFoundException | SQLException e){
            showError("Please select a valid file");
            return;
        }

        updateNodeTreeDisplay();
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
            return;
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
        } catch (FileNotFoundException | SQLException | NullPointerException e)  {
            showError("Please select a valid file");
            return;
        }
        // updating display of all edges
        //updateDisplay();
        updateEdgeTreeDisplay();
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
            return;
        }
    }

    @FXML
    void updateNodeTreeDisplay() {

        JFXTreeTableColumn<Node, String> nID = new JFXTreeTableColumn<>("Node ID");
        nID.setPrefWidth(125);
        nID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getNodeID());
            return var;
        });

        JFXTreeTableColumn<Node, String> nX = new JFXTreeTableColumn<>("X");
        nX.setPrefWidth(125);
        nX.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(String.valueOf(param.getValue().getValue().getXPos()));
            return var;
        });

        JFXTreeTableColumn<Node, String> nY = new JFXTreeTableColumn<>("Y");
        nY.setPrefWidth(125);
        nY.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(String.valueOf(param.getValue().getValue().getYPos()));
            return var;
        });

        JFXTreeTableColumn<Node, String> nFloor = new JFXTreeTableColumn<>("Floor");
        nFloor.setPrefWidth(125);
        nFloor.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getFloor());
            return var;
        });

        JFXTreeTableColumn<Node, String> nBuilding = new JFXTreeTableColumn<>("Building");
        nBuilding.setPrefWidth(125);
        nBuilding.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getBuilding());
            return var;
        });

        JFXTreeTableColumn<Node, String> nType = new JFXTreeTableColumn<>("Node Type");
        nType.setPrefWidth(125);
        nType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getNodeType());
            return var;
        });

        JFXTreeTableColumn<Node, String> nLongName = new JFXTreeTableColumn<>("Long Name");
        nLongName.setPrefWidth(125);
        nLongName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getLongName());
            return var;
        });

        JFXTreeTableColumn<Node, String> nShortName = new JFXTreeTableColumn<>("Short Name");
        nShortName.setPrefWidth(125);
        nShortName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getShortName());
            return var;
        });

        List<NodeInfo> nodeList = null;
        try {
            nodeList = App.mapService.getAllNodes().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<Node> data = FXCollections.observableArrayList();

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(int i = 0; i < nodeList.size(); i++){
            if(!nodeList.get(i).getNodeID().isEmpty()){
                data.add(new Node(nodeList.get(i).getNodeID(),nodeList.get(i).getXPos(),nodeList.get(i).getYPos(),
                        nodeList.get(i).getFloor(), nodeList.get(i).getBuilding(), nodeList.get(i).getNodeType(), nodeList.get(i).getLongName(),
                        nodeList.get(i).getShortName()));

            }
        }

        // checks to see if tree has been initialized and wont make dupe columns
        TreeItem<Node> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        if(!treeInit){
            treeView.getColumns().addAll(nID,nX,nY,nFloor,nBuilding,nType,nLongName,nShortName);
            treeInit = true;
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
        updateMap();
    }

    @FXML
    void updateEdgeTreeDisplay() {

        JFXTreeTableColumn<Edge, String> eID = new JFXTreeTableColumn<>("Edge ID");
        eID.setPrefWidth(125);
        eID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Edge, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getEdgeID());
            return var;
        });

        JFXTreeTableColumn<Edge, String> eStart = new JFXTreeTableColumn<>("Start Node");
        eStart.setPrefWidth(125);
        eStart.setCellValueFactory((TreeTableColumn.CellDataFeatures<Edge, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getStartNodeID());
            return var;
        });

        JFXTreeTableColumn<Edge, String> eEnd = new JFXTreeTableColumn<>("End Node");
        eEnd.setPrefWidth(125);
        eEnd.setCellValueFactory((TreeTableColumn.CellDataFeatures<Edge, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getEndNodeID());
            return var;
        });

        List<EdgeInfo> edgeList = null;

        try {
            edgeList = App.mapService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        ObservableList<Edge> data = FXCollections.observableArrayList();

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(int i = 0; i < edgeList.size(); i++){
            if(!edgeList.get(i).getEdgeID().isEmpty()){
                data.add(new Edge(edgeList.get(i).getEdgeID(),edgeList.get(i).getStartNodeID(),edgeList.get(i).getEndNodeID()));
            }
        }

        // checks to see if tree has been initialized and wont make dupe columns
        TreeItem<Edge> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        if(!treeEdgeInit){
            treeViewEdge.getColumns().addAll(eID,eStart, eEnd);
            treeEdgeInit = true;
        }
        treeViewEdge.setRoot(root);
        treeViewEdge.setShowRoot(false);
        updateMap();
    }

    @FXML
    void showError(String message){
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

    void updateMap() {
        try {
            List<NodeInfo> nodeList = App.mapService.getAllNodes().collect(Collectors.toList());
            List<EdgeInfo> edgeList = App.mapService.getAllEdges().collect(Collectors.toList());
            map.drawFloor(nodeList, edgeList, selectedFloor);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}