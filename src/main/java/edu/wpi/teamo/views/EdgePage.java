package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.map.EdgeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;

public class EdgePage extends SubPageController implements Initializable {

    @FXML
    private JFXTextField addEdgeID;

    @FXML
    private JFXTextField addNode1;

    @FXML
    private JFXTextField addNode2;


    @FXML
    private JFXTextField editingEdge;

    @FXML
    private JFXTextArea displayEdges;

    @FXML
    private JFXTextArea currentEdge;

    @FXML
    private JFXTextField editEdgeID;

    @FXML
    private JFXTextField editNode1;

    @FXML
    private JFXTextField editNode2;


    @FXML
    private JFXTextField deleteEdgeID;

    /**
     * keeps edges displayed when returning to the page
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        updateDisplay();
    }
    /**
     * function that goes through database and prints out all edges
     */
    void updateDisplay() {
        // list of all edges from db
        List<EdgeInfo> edgeList = null;
        // try/catch for calling getAllEdges from backend
        try {
            edgeList = App.dbService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String edgeString = "";
        // try/catch for looping through nodes
        try {
            // loops through the list and prints edge, start node, and end node
            for (int i = 0; i < edgeList.size(); i++) {
                if (!edgeList.get(i).getEdgeID().isEmpty()) {
                    edgeString = edgeString + edgeList.get(i).getEdgeID() + "                  ";
                    edgeString = edgeString + edgeList.get(i).getStartNodeID() + "                  ";
                    edgeString = edgeString + edgeList.get(i).getEndNodeID();
                    edgeString = edgeString + "\n";
                }
            }
        } catch (Exception NullPointerException) {
            return;
        }
        // changing displayed list of nodes
        displayEdges.setText(edgeString);
    }

    /**
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleAddSubmit(ActionEvent event) {
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
    void handleDeleteSubmit(ActionEvent event) {
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
    void handleEditSubmit(ActionEvent event) {
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

    @FXML
    void handleLookup(ActionEvent event) {
        String currentEdgeID = editingEdge.getText();
        String edgeString = "";

        // try/catch for SQL searching for an edge to delete
        try {
            EdgeInfo currentEdgeInfo = App.dbService.getEdge(currentEdgeID);
            edgeString = "ID: " + currentEdgeInfo.getEdgeID() + "\n";
            edgeString += "Start Node: " + currentEdgeInfo.getStartNodeID() + "\n";
            edgeString += "End Node: " + currentEdgeInfo.getEndNodeID();
        } catch (Exception SQLException) {
            return;
        }
        System.out.println(edgeString);
        currentEdge.setText(edgeString);
    }

    /**
     * handles reading in CSV from file
     *
     * @param event click load button
     */
    @FXML
    void handleLoadCSV(ActionEvent event) {
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
    void handleSave(ActionEvent event) {
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
}