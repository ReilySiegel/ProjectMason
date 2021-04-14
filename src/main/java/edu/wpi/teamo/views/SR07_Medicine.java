package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;

import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SR07_Medicine {

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextField room;

    @FXML
    private JFXTextField userName;

    @FXML
    private void handleSubmission(ActionEvent e) {
        String medicine = medName.getText();
        String roomNumber = room.getText().replaceAll("[^a-zA-Z0-9]", "");
        String name = userName.getText().replaceAll("[^a-zA-Z0-9]", "");
    }

    @FXML
    private void backToMain(ActionEvent e) {
        App.switchPage(Pages.MAIN);
    }

}