package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;
import java.io.IOException;

import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;

public class PathfindingPage {

    @FXML
    private JFXButton getDirections;

    @FXML
    private JFXComboBox startLocation;

    @FXML
    private JFXComboBox endLocation;

    @FXML
    private JFXTextArea locationAvailable;

    @FXML
    private JFXTextArea directions;

    /**
     *
     * @param event
     */
    @FXML
    void handleGetDirections(ActionEvent event) {

        String startNodeId; //getIDfromLocation(startLocation);
        String endNodeId; //getIDfromLocation(endLocation);

        locationAvailable.setText("list of all the nodes");
        directions.setText("path");

    }
}