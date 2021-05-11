package edu.wpi.teamo.views.profilepage;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.views.requestmanager.RequestDisplay;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private JFXListView<JFXCheckBox> typeFilterSelection;

    @FXML
    private JFXCheckBox showCompleted;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private JFXTimePicker timePicker;

    @FXML
    private JFXCheckBox filterTime;

    @FXML
    private JFXCheckBox showCompletedRequestsCheckbox;

    @FXML
    private Text usernameLabel;

    @FXML
    private Text firstNameLabel;

    @FXML
    private Text lastNameLabel;

    @FXML
    private ImageView profilePicture;

    @FXML
    private JFXButton applyFilterButton;

    RequestDisplay requestDisplay;

    HashMap<String, JFXCheckBox> typeCheckboxes;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        usernameLabel.setText(App.resourceBundle.getString("key.username_semicolon") + Session.getAccount().getUsername());
        firstNameLabel.setText(App.resourceBundle.getString("key.firstname_semicolon") + Session.getAccount().getFirstName());
        lastNameLabel.setText(App.resourceBundle.getString("key.lastname_semicolon") + Session.getAccount().getLastName());

        ProfileRequestDisplay requestDisplay = new ProfileRequestDisplay(reqDisplayListView, showCompletedRequestsCheckbox);
        requestDisplay.update();
    }
}
