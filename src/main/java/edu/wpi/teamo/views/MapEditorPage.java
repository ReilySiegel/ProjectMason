package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;

import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;

public class MapEditorPage extends SubPageController implements Initializable{

    @FXML
    private JFXListView<String> nodeArea;

    @FXML
    private JFXTreeView<NodeInfo> nodeTree;

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
        //Update display at start so loaded database persists after switching pages
        updateDisplay();
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
            App.dbService.addNode(newNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY),
                    "Default Floor", "Default Building", "Default Type",
                    newNodeID, newNodeID);
            updateDisplay();

        }
        catch(SQLException e){
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
            App.dbService.deleteNode(deleteNode);
            updateDisplay();
        }
        catch (SQLException e){
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
        String newNodeID = editNodeID.getText();
        String newNodeX = editNodeX.getText();
        String newNodeY = editNodeY.getText();

        try{
            App.dbService.setNodePosition(currentNodeID, Integer.parseInt(newNodeX), Integer.parseInt(newNodeY));
            App.dbService.setNodeLongName(currentNodeID, newNodeID);

            updateDisplay();
        }
        catch (SQLException e){
            return;
        }
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
            App.dbService.addEdge(newEdgeID, newEdgeNode1, newEdgeNode2);
        } catch (Exception SQLException) {
            return;
        }
        updateDisplay();
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
            App.dbService.deleteEdge(EdgetoDelete);
        } catch (Exception SQLException) {
            return;
        }
        // updating the edge display
        updateDisplay();
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
            App.dbService.setEdgeID(editingEdge.getText(), newEditEdgeID);
            App.dbService.setEdgeStartID(newEditEdgeID, editEdgeNode1);
            App.dbService.setEdgeEndID(newEditEdgeID, editEdgeNode2);
        } catch (Exception FileNotFoundException) {
            return;
        }
        // updating the edge display
        updateDisplay();
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
            App.dbService.loadNodesFromFile(path);
            //App.dbService.loadNodesFromFile("src/test/resources/edu/wpi/teamo/map/database/testNodes.csv");
        }
        catch(NullPointerException | FileNotFoundException | SQLException e){
            return;
        }

        updateDisplay();
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
            App.dbService.writeNodesToCSV(path);
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
            App.dbService.loadEdgesFromFile(path);
        } catch (FileNotFoundException | SQLException | NullPointerException e)  {
            return;
        }
        // updating display of all edges
        updateDisplay();
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
            App.dbService.writeEdgesToCSV(path);
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
            nodeList = App.dbService.getAllNodes().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        // list of all edges from db
        List<EdgeInfo> edgeList = null;
        // try/catch for calling getAllEdges from backend
        try {
            edgeList = App.dbService.getAllEdges().collect(Collectors.toList());
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
}