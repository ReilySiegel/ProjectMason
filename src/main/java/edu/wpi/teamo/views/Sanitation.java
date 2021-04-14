package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Sanitation {

    @FXML
    private JFXTextField service;

    @FXML
    private JFXTimePicker serviceTime;

    @FXML
    private JFXTimePicker estJobTime;

    @FXML
    private JFXComboBox serviceLocation;

    @FXML
    private JFXTextField priority;

    @FXML
    private JFXTextArea notes;

    @FXML
    private void backToMain(ActionEvent e) {
        App.switchPage(Pages.MAIN);
    }

}
