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
    private Text usernameLabel;

    @FXML
    private Text firstNameLabel;

    @FXML
    private Text lastNameLabel;

    @FXML
    private JFXButton applyFilterButton;

    RequestDisplay requestDisplay;

    HashMap<String, JFXCheckBox> typeCheckboxes;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        usernameLabel.setText(App.resourceBundle.getString("key.username_semicolon") + Session.getAccount().getUsername());
        firstNameLabel.setText(App.resourceBundle.getString("key.firstname_semicolon") + Session.getAccount().getFirstName());
        lastNameLabel.setText(App.resourceBundle.getString("key.lastname_semicolon") + Session.getAccount().getLastName());

        typeCheckboxes = new HashMap<>();
        typeCheckboxes.put("Medicine",       new JFXCheckBox(App.resourceBundle.getString("key.medicine")));
        typeCheckboxes.put("Sanitation",     new JFXCheckBox(App.resourceBundle.getString("key.sanitation")));
        typeCheckboxes.put("Security",       new JFXCheckBox(App.resourceBundle.getString("key.security")));
        typeCheckboxes.put("Food",           new JFXCheckBox(App.resourceBundle.getString("key.food")));
        typeCheckboxes.put("Gift",           new JFXCheckBox(App.resourceBundle.getString("key.gift")));
        typeCheckboxes.put("Interpreter",    new JFXCheckBox(App.resourceBundle.getString("key.interpreter")));
        typeCheckboxes.put("Laundry",        new JFXCheckBox(App.resourceBundle.getString("key.laundry")));
        typeCheckboxes.put("Maintenance",    new JFXCheckBox(App.resourceBundle.getString("key.maintenance")));
        typeCheckboxes.put("Religious",      new JFXCheckBox(App.resourceBundle.getString("key.religious")));
        typeCheckboxes.put("Transportation", new JFXCheckBox(App.resourceBundle.getString("key.trans")));
        typeCheckboxes.put("COVID Survey",   new JFXCheckBox(App.resourceBundle.getString("key.covid_survey")));

        requestDisplay = new RequestDisplay(reqDisplayListView,
                                            typeCheckboxes,
                                            showCompleted,
                                            timePicker,
                                            datePicker,
                                            filterTime,
                                            Session.getAccount().getUsername());
        requestDisplay.update();


        for (JFXCheckBox box : typeCheckboxes.values()) {
            box.setOnAction(event -> requestDisplay.update());
            typeFilterSelection.getItems().add(box);
            box.setSelected(true);
        }

        applyFilterButton.setOnAction(event -> requestDisplay.update());
        showCompleted.setOnAction(event -> requestDisplay.update());
    }
}
