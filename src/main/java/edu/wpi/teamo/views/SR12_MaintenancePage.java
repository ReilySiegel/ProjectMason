package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SR12_MaintenancePage extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextField notes;

    @FXML
    private MenuButton locationBox;

    @FXML
    private MenuButton typeMenuBox;

    private boolean validRequest;
    private SR12Type type;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        validRequest = true;
        try {
            resetLocationBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetLocationBox() throws SQLException {
        LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
        locationBox.getItems().removeAll(locationBox.getItems());

        for (NodeInfo node : nodes) {
            CheckMenuItem menuItem = new CheckMenuItem();
            menuItem.setText(node.getNodeID());

            menuItem.setOnAction(event -> locationBox.setText(locationBox.getItems().stream()
                    .map((MenuItem mI) -> (CheckMenuItem) mI)
                    .filter(CheckMenuItem::isSelected)
                    .map(CheckMenuItem::getText)
                    .collect(Collectors.joining(", "))));
            locationBox.getItems().add(menuItem);
        }

    }

    @FXML
    private void submitBtnOnClick(ActionEvent actionEvent) {
    //TODO
    }

    @FXML
    private void helpBtnOnClick(ActionEvent actionEvent) {
    //TODO
    }

    @FXML
    private void onElevatorOptClick(ActionEvent actionEvent) {
        type = SR12Type.ELEVATOR;
        typeMenuBox.textProperty().setValue("Elevator Maintenance");
    }

    @FXML
    private void onPowerOptClick(ActionEvent actionEvent) {
        type = SR12Type.POWER;
        typeMenuBox.textProperty().setValue("Power Maintenance");
    }

    private enum SR12Type {
        ELEVATOR,POWER
    }
}
