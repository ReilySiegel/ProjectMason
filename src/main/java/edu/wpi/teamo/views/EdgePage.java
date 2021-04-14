package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.map.database.EdgeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class EdgePage {
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
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleAddSubmit(ActionEvent event) {
        String newEdgeID = addEdgeID.getText();
        String newEdgeNode1 = addNode1.getText();
        String newEdgeNode2 = addNode2.getText();

        try {
            App.dbService.addEdge(newEdgeID, newEdgeNode1, newEdgeNode2);
        } catch (Exception SQLException) {
            return;
        }

        // list of all edge from db
        List<EdgeInfo> edgeList = null;
        try {
            edgeList = App.dbService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String edgeString = "";

        // loops through the list and prints edge, start node, and end node
        for (int i = 0; i < edgeList.size(); i++) {
            if (!edgeList.get(i).getEdgeID().isEmpty()) {
                edgeString = edgeString + edgeList.get(i).getEdgeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getStartNodeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getEndNodeID();
                edgeString = edgeString + "\n";
            }
        }
        displayEdges.setText(edgeString);

    }

    /**
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleDeleteSubmit(ActionEvent event) {
        String EdgetoDelete = deleteEdgeID.getText();
        try {
            App.dbService.deleteEdge(EdgetoDelete);
        } catch (Exception SQLException) {
            return;
        }

        // list of all edge from db
        List<EdgeInfo> edgeList = null;
        try {
            edgeList = App.dbService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String edgeString = "";

        // loops through the list and prints edge, start node, and end node
        for (int i = 0; i < edgeList.size(); i++) {
            if (!edgeList.get(i).getEdgeID().isEmpty()) {
                edgeString = edgeString + edgeList.get(i).getEdgeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getStartNodeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getEndNodeID();
                edgeString = edgeString + "\n";
            }
        }
        displayEdges.setText(edgeString);

    }

    /**
     * handles editing a edge
     *
     * @param event clicking submit button
     */
    @FXML
    void handleEditSubmit(ActionEvent event) {
        String neweditEdgeID = editEdgeID.getText();
        String editEdgeNode1 = editNode1.getText();
        String newEdgeNode2 = editNode2.getText();

        // try catch for reading in file
        try {
            // App.dbService.);
        } catch (Exception FileNotFoundException) {
            return;
        }

        // list of all edge from db
        List<EdgeInfo> edgeList = null;
        try {
            edgeList = App.dbService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String edgeString = "";

        // loops through the list and prints edge, start node, and end node
        for (int i = 0; i < edgeList.size(); i++) {
            if (!edgeList.get(i).getEdgeID().isEmpty()) {
                edgeString = edgeString + edgeList.get(i).getEdgeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getStartNodeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getEndNodeID();
                edgeString = edgeString + "\n";
            }
        }
        displayEdges.setText(edgeString);
    }

    @FXML
    void handleLookup(ActionEvent event) {
        String currentEdgeID = editingEdge.getText();
        String edgeString = "";
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
     * handles back to main page
     *
     * @param event click back button
     */
    @FXML
    void handleBacktoMain(ActionEvent event) {
        App.switchPage(Pages.MAIN);
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
        File f = fc.showOpenDialog(null);
        String x = f.getPath();

        // try catch for reading in file
        try {
            App.dbService.loadEdgesFromFile(x);
        } catch (Exception FileNotFoundException) {
            return;
        }

        // list of all edge from db
        List<EdgeInfo> edgeList = null;
        try {
            edgeList = App.dbService.getAllEdges().collect(Collectors.toList());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        String edgeString = "";

        // loops through the list and prints edge, start node, and end node
        for (int i = 0; i < edgeList.size(); i++) {
            if (!edgeList.get(i).getEdgeID().isEmpty()) {
                edgeString = edgeString + edgeList.get(i).getEdgeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getStartNodeID() + "                  ";
                edgeString = edgeString + edgeList.get(i).getEndNodeID();
                edgeString = edgeString + "\n";
            }
        }
        displayEdges.setText(edgeString);

    }
}