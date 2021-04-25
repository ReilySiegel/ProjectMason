package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class LaundryRequestPage extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackpane;

    @FXML
    private JFXTextField service;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextField notes;

    @FXML
    private Text typeErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text assignedErrorText;

    @FXML
    private MenuButton locationBox;







}
