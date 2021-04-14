package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.map.database.Node;
import edu.wpi.teamo.map.database.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class NodePage {

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
     * Event handler for adding a Node to the database
     * @param event
     */
    @FXML
    void handleAddSubmit(ActionEvent event) {
        String newNodeID = addNodeID.getText();
        String newNodeX = addNodeX.getText();
        String newNodeY = addNodeY.getText();

    }

    /**
     * Event handler for removing a Node from the database
     * @param event
     */
    @FXML
    void handleDeleteSubmit(ActionEvent event) {
        String deleteNode = deleteNodeID.getText();

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
    }
    /**
     * event hanlder for switching to main page
     * @param e Action Event parameter
     */
    @FXML
    private void goToMain(ActionEvent e) {
        App.switchPage(Pages.MAIN);
    }

    /**
     * Event handler to load in and display a Node csv file
     * @param event
     */
    @FXML
    void handleLoadCSV(ActionEvent event) {

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));
        File f = fc.showOpenDialog(null);
        String path = f.getPath();

        try{
            App.dbService.loadNodesFromFile(path);
        }
        catch (FileNotFoundException e){
            return;
        }
        catch (SQLException e){
            return;
        }

        List<NodeInfo> nodeList = App.dbService.getAllNodes().collect(Collectors.toList());
        String displayString = "";

        for(int i = 0; i < nodeList.size(); i++){
            if(!nodeList.get(i).getNodeID().isEmpty()){
                displayString = displayString + nodeList.get(i).getShortName();
                displayString = displayString + "\n";
            }
        }
        nodeArea.setText(displayString);
    }
}