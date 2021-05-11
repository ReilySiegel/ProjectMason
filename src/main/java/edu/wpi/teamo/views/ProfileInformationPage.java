package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.views.requestmanager.RequestDisplay;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ProfileInformationPage implements Initializable {

    @FXML
    private JFXListView<VBox> reqDisplayListView;

    @FXML
    private Text usernameLabel;

    @FXML
    private Text firstNameLabel;

    @FXML
    private Text lastNameLabel;

    JFXCheckBox medCheck;
    JFXCheckBox sanCheck;
    JFXCheckBox secCheck;
    JFXCheckBox foodCheck;
    JFXCheckBox giftCheck;
    JFXCheckBox interpCheck;
    JFXCheckBox laundryCheck;
    JFXCheckBox maintCheck;
    JFXCheckBox religCheck;
    JFXCheckBox covidCheck;
    JFXCheckBox transCheck;

    RequestDisplay requestDisplay;

    HashMap<String, JFXCheckBox> typeCheckboxes;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        usernameLabel.setText(App.resourceBundle.getString("key.username_semicolon") + Session.getAccount().getUsername());
        firstNameLabel.setText(App.resourceBundle.getString("key.firstname_semicolon") + Session.getAccount().getFirstName());
        lastNameLabel.setText(App.resourceBundle.getString("key.lastname_semicolon") + Session.getAccount().getLastName());

        requestDisplay = new RequestDisplay(reqDisplayListView, false, LocalDateTime.MAX,Session.getAccount().getUsername());

    }

}
