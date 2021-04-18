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

import edu.wpi.teamo.database.map.EdgeInfo;
import edu.wpi.teamo.database.map.Node;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.stage.FileChooser;

public class MapEditorPage extends SubPageController implements Initializable{


    @FXML
    private JFXTreeTableView<Node> treeView;

    @FXML
    private JFXListView<String> nodeArea;

    @FXML
    private JFXTextField addNodeID;

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
    private JFXTextField editNodeID;

    @FXML
    private JFXTextField editNodeX;

    @FXML
    private JFXTextField editNodeY;

    @FXML
    private JFXButton editSubmit;

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

    boolean treeInit = false;

    /**
     * Set validators to insure that the x and y coordinate fields are numbers
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){


        nodeArea.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        NumberValidator numberValidator = new NumberValidator();

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

        editNodeX.getValidators().add(numberValidator);
        editNodeX.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    editNodeX.validate();
                }
            }
        });

        editNodeY.getValidators().add(numberValidator);
        editNodeY.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    editNodeY.validate();
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
                    deleteNodeID.setText(newValue.getValue().getNodeID());
                }
            }
        });

        //Update display at start so loaded database persists after switching pages
        updateDisplay();
        updateNodeTreeDisplay();
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

        try{
            if(newNodeX.equals("")){
                newNodeX = "0";
            }
            if(newNodeY.equals("")){
                newNodeY = "0";
            }
            App.mapService.addNode(newNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY),
                    "Default Floor", "Default Building", "Default Type",
                    newNodeID, newNodeID);
            updateDisplay();
            updateNodeTreeDisplay();

        }
        catch(SQLException e){
            return;
        }

        updateNodeTreeDisplay();
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
            updateDisplay();
            updateNodeTreeDisplay();
        }
        catch (SQLException e){
            return;
        }

        updateNodeTreeDisplay();
    }

    /**
     * Event handler for writing node edits to the database
     * @param event
     */
    @FXML
    void handleEditSubmitNode(ActionEvent event) {
        String currentNodeID = origNodeID.getText();
        String newNodeID = editNodeID.getText();
        String newNodeX = editNodeX.getText();
        String newNodeY = editNodeY.getText();

        try{
            if(newNodeX.equals("")){
                newNodeX = "0";
            }
            if(newNodeY.equals("")){
                newNodeY = "0";
            }
            App.mapService.setNodePosition(currentNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY));
            App.mapService.setNodeLongName(currentNodeID, newNodeID);

            updateDisplay();
            updateNodeTreeDisplay();
        }
        catch (SQLException e){
            return;
        }

        updateNodeTreeDisplay();
    }



    /**
     * handles editing a edge
     *
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
            return;
        }
        //updateDisplay();
        updateNodeTreeDisplay();
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
            return;
        }
        // updating the edge display
        //updateDisplay();
        updateNodeTreeDisplay();
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
            return;
        }
        // updating the edge display
        updateDisplay();
        updateNodeTreeDisplay();
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
            return;
        }

        updateDisplay();
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
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));

        // try catch for reading in file
        try {
            File f = fc.showOpenDialog(null);
            String path = f.getPath();
            App.mapService.loadEdgesFromFile(path);
        } catch (FileNotFoundException | SQLException | NullPointerException e)  {
            return;
        }
        // updating display of all edges
        //updateDisplay();
        updateNodeTreeDisplay();
    }

    @FXML
    void handleSaveEdgeCSV(ActionEvent event) {
        // choosing where to save
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));

        // try catch for SQL/IO errors
        try {
            File f = fc.showSaveDialog(null);
            String path = f.getPath();
            App.mapService.writeEdgesToCSV(path);
        } catch (IOException | SQLException | NullPointerException e ) {
            return;
        }
    }

    /**
     * Display all nodes in database in the textarea
     */
    @FXML
    void updateDisplay(){
        List<NodeInfo> nodeList = null;
        try {
            nodeList = App.mapService.getAllNodes().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // list of all edges from db
        List<EdgeInfo> edgeList = null;
        // try/catch for calling getAllEdges from backend
        try {
            edgeList = App.mapService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        String displayString = "";

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(int i = 0; i < nodeList.size(); i++){
            displayString = "";
            if(!nodeList.get(i).getNodeID().isEmpty()){
                displayString = displayString + nodeList.get(i).getNodeID() + " ";
                displayString = displayString + nodeList.get(i).getXPos() + " ";
                displayString = displayString + nodeList.get(i).getYPos() + " ";
                displayString = displayString + nodeList.get(i).getFloor() + " ";
                displayString = displayString + nodeList.get(i).getBuilding() + " ";
                displayString = displayString + nodeList.get(i).getLongName() + " ";
                displayString = displayString + nodeList.get(i).getShortName();
                nodeArea.getItems().add(displayString);
            }
        }


        String edgeString = "";
        // try/catch for looping through nodes
        try {
            // loops through the list and prints edge, start node, and end node
            for (int i = 0; i < edgeList.size(); i++) {
                edgeString = "";
                if (!edgeList.get(i).getEdgeID().isEmpty()) {
                    edgeString = edgeString + edgeList.get(i).getEdgeID() + "                  ";
                    edgeString = edgeString + edgeList.get(i).getStartNodeID() + "                  ";
                    edgeString = edgeString + edgeList.get(i).getEndNodeID();
                    edgeString = edgeString + "\n";
                    nodeArea.getItems().add(edgeString);
                }
            }
        } catch (Exception NullPointerException) {
            return;
        }
    }

    @FXML
    private JFXTextField editingEdge;

    @FXML
    private JFXTextArea displayEdges;

    @FXML
    private JFXTextArea currentEdge;


    @FXML
    void handleLookup(ActionEvent event) {

    }

    @FXML
    void updateNodeTreeDisplay() {

        JFXTreeTableColumn<Node, String> nID = new JFXTreeTableColumn<>("NodeID");
        nID.setPrefWidth(125);
        nID.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getNodeID());
            return var;
        });

        JFXTreeTableColumn<Node, String> nX = new JFXTreeTableColumn<>("xPos");
        nX.setPrefWidth(125);
        nX.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(String.valueOf(param.getValue().getValue().getXPos()));
            return var;
        });

        JFXTreeTableColumn<Node, String> nY = new JFXTreeTableColumn<>("yPos");
        nY.setPrefWidth(125);
        nY.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(String.valueOf(param.getValue().getValue().getYPos()));
            return var;
        });

        JFXTreeTableColumn<Node, String> nFloor = new JFXTreeTableColumn<>("floor");
        nFloor.setPrefWidth(125);
        nFloor.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getFloor());
            return var;
        });

        JFXTreeTableColumn<Node, String> nBuilding = new JFXTreeTableColumn<>("building");
        nBuilding.setPrefWidth(125);
        nBuilding.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getBuilding());
            return var;
        });

        JFXTreeTableColumn<Node, String> nType = new JFXTreeTableColumn<>("nodeType");
        nType.setPrefWidth(125);
        nType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getNodeType());
            return var;
        });

        JFXTreeTableColumn<Node, String> nLongName = new JFXTreeTableColumn<>("longName");
        nLongName.setPrefWidth(125);
        nLongName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> param) -> {
            StringProperty var = new SimpleStringProperty(param.getValue().getValue().getLongName());
            return var;
        });

        JFXTreeTableColumn<Node, String> nShortName = new JFXTreeTableColumn<>("longName");
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
        TreeItem<Node> root = new RecursiveTreeItem<>(data, RecursiveTreeObject::getChildren);
        if(!treeInit){
            treeView.getColumns().addAll(nID,nX,nY,nFloor,nBuilding,nType,nLongName,nShortName);
            treeInit = true;
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }
}