package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.map.database.EdgeInfo;
import edu.wpi.teamo.map.database.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

public class EdgePage {
    @FXML
    private JFXTextField addEdgeID;

    @FXML
    private JFXTextField addNode1;

    @FXML
    private JFXTextField addNode2;

    @FXML
    private JFXButton addSubmit;

    @FXML
    private JFXTextField editingEdge;

    @FXML
    private JFXTextArea displayEdges;

    @FXML
    private Label currentEdge;

    @FXML
    private JFXTextField editEdgeID;

    @FXML
    private JFXTextField editNode1;

    @FXML
    private JFXTextField editNode2;

    @FXML
    private JFXButton editSubmit;

    @FXML
    private JFXTextField deleteEdgeID;

    @FXML
    private JFXButton deleteSubmit;

    /**
     * handles editing a edge
     * @param event clicking submit button
     */
    @FXML
    void handleAddSubmit(ActionEvent event) {
        String newEdgeID = addEdgeID.getText();
        String newEdgeNode1 = addNode1.getText();
        String newEdgeNode2 = addNode2.getText();

    }

    /**
     * handles editing a edge
     * @param event clicking submit button
     */
    @FXML
    void handleDeleteSubmit(ActionEvent event) {
        String deleteEdge = deleteEdgeID.getText();


    }

    /**
     * handles editing a edge
     * @param event clicking submit button
     */
    @FXML
    void handleEditSubmit(ActionEvent event) {
        String currentEdgeID = editingEdge.getText();
        String neweditEdgeID = editEdgeID.getText();
        String editEdgeNode1 = editNode1.getText();
        String newEdgeNode2 = editNode2.getText();
    }

    /**
     * handles back to main page
     * @param event click back button
     */
    @FXML
    void handleBacktoMain(ActionEvent event) {
        App.switchPage(Pages.MAIN);
    }

    /**
     * handles reading in CSV from file
     * @param event click load button
     * @throws FileNotFoundException
     * @throws SQLException
     */
    @FXML
    void handleLoadCSV(ActionEvent event) throws FileNotFoundException, SQLException {
        // reading in file
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));
        File f = fc.showOpenDialog(null);
        String x = f.getPath();

        // try catch for reading in file
        try {
            App.dbService.loadEdgesFromFile(x);
        } catch (Exception FileNotFoundException) {
            displayEdges.setText("File Nots Found");
        }

        // list of all edge from db
        List<EdgeInfo> edgeList = App.dbService.getAllEdges().collect(Collectors.toList());
        String edgeString = "";

        // loops through the list and prints edge, start node, and end node
        for(int i = 0; i < edgeList.size(); i++){
            if(!edgeList.get(i).getEdgeID().isEmpty()){
                edgeString = edgeString + edgeList.get(i).getEdgeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getStartNodeID()+ "                  ";
                edgeString = edgeString + edgeList.get(i).getEndNodeID();
                edgeString = edgeString + "\n";
            }
        }
        displayEdges.setText(edgeString);

    }

}