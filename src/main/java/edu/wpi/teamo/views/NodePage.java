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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class NodePage extends SubPageController implements Initializable{


    @FXML
    private JFXTextArea nodeArea;

    @FXML
    private JFXTextField addNodeID;

    @FXML
    private JFXTextField addNodeX;

    @FXML
    private JFXTextField addNodeY;

    @FXML
    private JFXButton addSubmit;

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
    private JFXButton deleteSubmit;

    /**
     * Set validators to insure that the x and y coordinate fields are numbers
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
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
    void handleAddSubmit(ActionEvent event) {
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
    void handleDeleteSubmit(ActionEvent event) {
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
    void handleEditSubmit(ActionEvent event) {
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
     * Event handler to load in and display a Node csv file
     * @param event
     */
    @FXML
    void handleLoadCSV(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));


        try{
            File f = fc.showOpenDialog(null);
            String path = f.getPath();
            App.dbService.loadNodesFromFile(path);
            //App.dbService.loadNodesFromFile("src/test/resources/edu/wpi/teamo/map/database/testNodes.csv");
        }
        catch(NullPointerException e){
            return;
        }
        catch (FileNotFoundException e){
            return;
        }
        catch (SQLException e){
            return;
        }

        updateDisplay();
    }

    /**
     * Event handler for saving a csv file
     * @param event
     */
    @FXML
    void handleSaveCSV(ActionEvent event){
        FileChooser fc = new FileChooser();
        fc.setTitle("Save CSV");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILE", "*.csv"));

        try{
            File f = fc.showSaveDialog(null);
            String path = f.getPath();
            App.dbService.writeNodesToCSV(path);
        }
        catch(NullPointerException e){
            return;
        }
        catch (SQLException sqlException){
            return;
        }
        catch (IOException ioException){
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
        String displayString = "";

        //Display all Node ID, X, Y, Floor, Building, LongName, and ShortName
        for(int i = 0; i < nodeList.size(); i++){
            if(!nodeList.get(i).getNodeID().isEmpty()){
                displayString = displayString + nodeList.get(i).getNodeID() + " ";
                displayString = displayString + nodeList.get(i).getXPos() + " ";
                displayString = displayString + nodeList.get(i).getYPos() + " ";
                displayString = displayString + nodeList.get(i).getFloor() + " ";
                displayString = displayString + nodeList.get(i).getBuilding() + " ";
                displayString = displayString + nodeList.get(i).getLongName() + " ";
                displayString = displayString + nodeList.get(i).getShortName();
                displayString = displayString + "\n";
            }
        }
        nodeArea.setText(displayString);
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