package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ManageRequestsNew implements Initializable {

    @FXML
    private JFXListView<VBox> reqDisplayListView;

    @FXML
    private JFXListView<JFXCheckBox> typeFilterSelection;

    @FXML
    private JFXCheckBox showCompleted;

    @FXML
    private StackPane stackPane;

    JFXCheckBox medCheck;
    JFXCheckBox sanCheck;
    JFXCheckBox secCheck;
    JFXCheckBox foodCheck;
    JFXCheckBox giftCheck;
    JFXCheckBox interpCheck;
    JFXCheckBox laundryCheck;
    JFXCheckBox maintCheck;
    JFXCheckBox religCheck;

    RequestDisplay requestDisplay;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        requestDisplay = new RequestDisplay(reqDisplayListView, false, stackPane);

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

        try {
            requestDisplay.update(selectedTypes);
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

        typeFilterSelection.getItems().add(medCheck);
        typeFilterSelection.getItems().add(sanCheck);
        typeFilterSelection.getItems().add(secCheck);
        typeFilterSelection.getItems().add(foodCheck);
        typeFilterSelection.getItems().add(giftCheck);
        typeFilterSelection.getItems().add(interpCheck);
        typeFilterSelection.getItems().add(laundryCheck);
        typeFilterSelection.getItems().add(maintCheck);
        typeFilterSelection.getItems().add(religCheck);
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

        requestDisplay.setShowComplete(showCompleted.isSelected());
        requestDisplay.update(selectedTypes);
    }
}
