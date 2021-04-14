package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;

import java.io.File;
import java.io.IOException;

import edu.wpi.teamo.Pages;
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

    @FXML
    void handleAddSubmit(ActionEvent event) {
        String newEdgeID = addEdgeID.getText();
        String newEdgeNode1 = addNode1.getText();
        String newEdgeNode2 = addNode2.getText();

    }

    @FXML
    void handleDeleteSubmit(ActionEvent event) {
        String deleteEdge = deleteEdgeID.getText();

    }

    @FXML
    void handleEditSubmit(ActionEvent event) {
        String currentEdgeID = editingEdge.getText();
        String neweditEdgeID = editEdgeID.getText();
        String editEdgeNode1 = editNode1.getText();
        String newEdgeNode2 = editNode2.getText();
    }

    @FXML
    void handleBacktoMain(ActionEvent event) {
        App.switchPage(Pages.MAIN);
    }

    @FXML
    void handleLoadCSV(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV FILES", "*.csv"));
        File f = fc.showOpenDialog(null);
    }

}