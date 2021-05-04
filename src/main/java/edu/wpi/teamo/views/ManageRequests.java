package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ManageRequests implements Initializable {

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

    RequestDisplay requestDisplay;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        requestDisplay = new RequestDisplay(reqDisplayListView, false, LocalDateTime.MAX);

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
        selectedTypes.put("COVID Survey", true);

        try {
            requestDisplay.update(selectedTypes, LocalDateTime.MAX);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        typeFilterSelection.getItems().clear();

        medCheck = new JFXCheckBox("Medicine");
        medCheck.setSelected(true);
        sanCheck = new JFXCheckBox("Sanitation");
        sanCheck.setSelected(true);
        secCheck = new JFXCheckBox("Security");
        secCheck.setSelected(true);
        foodCheck = new JFXCheckBox("Food");
        foodCheck.setSelected(true);
        giftCheck = new JFXCheckBox("Gift");
        giftCheck.setSelected(true);
        interpCheck = new JFXCheckBox("Interpreter");
        interpCheck.setSelected(true);
        laundryCheck = new JFXCheckBox("laundry");
        laundryCheck.setSelected(true);
        maintCheck = new JFXCheckBox("Maintenance");
        maintCheck.setSelected(true);
        religCheck = new JFXCheckBox("Religious");
        religCheck.setSelected(true);
        covidCheck = new JFXCheckBox("COVID Survey");
        covidCheck.setSelected(true);

        EventHandler filterCheck = new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    applyFilter();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        };

        medCheck.setOnAction(filterCheck);
        sanCheck.setOnAction(filterCheck);
        secCheck.setOnAction(filterCheck);
        foodCheck.setOnAction(filterCheck);
        giftCheck.setOnAction(filterCheck);
        interpCheck.setOnAction(filterCheck);
        laundryCheck.setOnAction(filterCheck);
        maintCheck.setOnAction(filterCheck);
        religCheck.setOnAction(filterCheck);
        covidCheck.setOnAction(filterCheck);
        showCompleted.setOnAction(filterCheck);


        typeFilterSelection.getItems().add(medCheck);
        typeFilterSelection.getItems().add(sanCheck);
        typeFilterSelection.getItems().add(secCheck);
        typeFilterSelection.getItems().add(foodCheck);
        typeFilterSelection.getItems().add(giftCheck);
        typeFilterSelection.getItems().add(interpCheck);
        typeFilterSelection.getItems().add(laundryCheck);
        typeFilterSelection.getItems().add(maintCheck);
        typeFilterSelection.getItems().add(religCheck);
        typeFilterSelection.getItems().add(covidCheck);
    }

    @FXML
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
