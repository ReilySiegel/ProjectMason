package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
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
    private MenuButton startLocation;

    @FXML
    private MenuButton endLocation;



    @FXML
    void handleGetDirections(ActionEvent event) {

        String startNodeId;
        String endNodeId;


    }
}