package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
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

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        HashMap<String, Boolean> selectedTypes = new HashMap<String, Boolean>();
        selectedTypes.put("Medicine", true);
        selectedTypes.put("Sanitation", true);
        selectedTypes.put("Security", true);
        selectedTypes.put("Food", true);
        selectedTypes.put("Gift", true);
        selectedTypes.put("Interpreter", true);
        selectedTypes.put("Laundry", true);
        selectedTypes.put("Maintenance", true);
        selectedTypes.put("Religious", true);
        selectedTypes.put("Transportation", true);
        selectedTypes.put("COVID Survey", true);

        usernameLabel.setText(App.resourceBundle.getString("key.username_semicolon") + Session.getAccount().getUsername());
        firstNameLabel.setText(App.resourceBundle.getString("key.firstname_semicolon") + Session.getAccount().getFirstName());
        lastNameLabel.setText(App.resourceBundle.getString("key.lastname_semicolon") + Session.getAccount().getLastName());

        requestDisplay = new RequestDisplay(reqDisplayListView, false, LocalDateTime.MAX, selectedTypes, Session.getAccount().getUsername());

        try {
            requestDisplay.update(selectedTypes, LocalDateTime.MAX);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        typeFilterSelection.getItems().clear();

        ArrayList<JFXCheckBox> filterCheckBoxes = new ArrayList<JFXCheckBox>();

        medCheck = new JFXCheckBox(App.resourceBundle.getString("key.medicine"));
        sanCheck = new JFXCheckBox(App.resourceBundle.getString("key.sanitation"));
        secCheck = new JFXCheckBox(App.resourceBundle.getString("key.security"));
        foodCheck = new JFXCheckBox(App.resourceBundle.getString("key.food"));
        giftCheck = new JFXCheckBox(App.resourceBundle.getString("key.gift"));
        interpCheck = new JFXCheckBox(App.resourceBundle.getString("key.interpreter"));
        laundryCheck = new JFXCheckBox(App.resourceBundle.getString("key.laundry"));
        maintCheck = new JFXCheckBox(App.resourceBundle.getString("key.maintenance"));
        religCheck = new JFXCheckBox(App.resourceBundle.getString("key.religious"));
        transCheck = new JFXCheckBox(App.resourceBundle.getString("key.trans"));
        covidCheck = new JFXCheckBox(App.resourceBundle.getString("key.covid_survey"));

        filterCheckBoxes.add(medCheck);
        filterCheckBoxes.add(sanCheck);
        filterCheckBoxes.add(secCheck);
        filterCheckBoxes.add(foodCheck);
        filterCheckBoxes.add(giftCheck);
        filterCheckBoxes.add(interpCheck);
        filterCheckBoxes.add(laundryCheck);
        filterCheckBoxes.add(maintCheck);
        filterCheckBoxes.add(religCheck);
        filterCheckBoxes.add(transCheck);
        filterCheckBoxes.add(covidCheck);

        EventHandler filterCheck = event -> {
            try {
                applyFilter();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        };

        for (JFXCheckBox checkBox : filterCheckBoxes) {
            checkBox.setSelected(true);
            checkBox.setOnAction(filterCheck);
            typeFilterSelection.getItems().add(checkBox);
        }

        showCompleted.setOnAction(filterCheck);
    }

    public void applyFilter() throws SQLException {

        HashMap<String, Boolean> selectedTypes =  new HashMap<String, Boolean>();
        selectedTypes.put("Medicine", medCheck.isSelected());
        selectedTypes.put("Sanitation", sanCheck.isSelected());
        selectedTypes.put("Security", secCheck.isSelected());
        selectedTypes.put("Food", foodCheck.isSelected());
        selectedTypes.put("Gift", giftCheck.isSelected());
        selectedTypes.put("Interpreter", interpCheck.isSelected());
        selectedTypes.put("Laundry", laundryCheck.isSelected());
        selectedTypes.put("Maintenance", maintCheck.isSelected());
        selectedTypes.put("Religious", religCheck.isSelected());
        selectedTypes.put("Transportation", transCheck.isSelected());
        selectedTypes.put("COVID Survey", covidCheck.isSelected());

        requestDisplay.setShowComplete(showCompleted.isSelected());

        if (filterTime.isSelected()) {
            if (timePicker.getValue() == null || datePicker.getValue() == null) {
                requestDisplay.update(selectedTypes, LocalDateTime.MAX);
            } else {
                LocalTime curTime = timePicker.getValue();
                LocalDateTime curDate = datePicker.getValue().atTime(curTime);
                requestDisplay.update(selectedTypes, curDate);
            }
        } else {
            requestDisplay.update(selectedTypes, LocalDateTime.MAX);
        }

    }

}
