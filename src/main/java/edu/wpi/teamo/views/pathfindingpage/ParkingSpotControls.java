package edu.wpi.teamo.views.pathfindingpage;

import com.jfoenix.controls.JFXButton;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.algos.AlgoNode;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class ParkingSpotControls {


    private JFXButton selectSavedParkingSpotButton;
    private JFXButton saveNewParkingSpotButton;
    private StackPane parentStackPane;
    private HBox parkingWindow;

    enum SelectionState {
        IDLE,
        CHOOSING
    }
    SelectionState state = SelectionState.IDLE;

    public List<Circle> parkingSpots;
    Consumer<NodeInfo> onPickParkingSpot;

    public ParkingSpotControls(JFXButton selectSavedParkingSpotButton,
                               JFXButton saveNewParkingSpotButton,
                               HBox parkingWindow,
                               StackPane parentStackPane,
                               Consumer<NodeInfo> onPickParkingSpot) {
        this.selectSavedParkingSpotButton = selectSavedParkingSpotButton;
        this.saveNewParkingSpotButton = saveNewParkingSpotButton;
        this.parkingWindow = parkingWindow;
        this.parentStackPane = parentStackPane;
        this.onPickParkingSpot = onPickParkingSpot;

        parkingSpots = new LinkedList<>();

        selectSavedParkingSpotButton.setOnAction(this::handleSelectParkingSpot);
        saveNewParkingSpotButton.setOnAction(this::handleSaveNewParkingSpot);
    }

    public void onClickNode(NodeInfo node) {
        if (state == SelectionState.CHOOSING) {
            assignParkingSpot(node);
            switchToIdle();
        }
    }

    private void assignParkingSpot(NodeInfo node){
        try {
            if (Session.isLoggedIn()) {
                Session.getAccount().setParkingSpot(node);
            }
        }
        catch (InvalidParameterException e) {
            App.showError(App.resourceBundle.getString("key.parking_spot_error"), parentStackPane);
        }
    }

    private void handleSelectParkingSpot(ActionEvent actionEvent) {
        if (hasParkingSpot()) {
            String spotID = getSavedParkingSpotID();
            try {
                NodeInfo parkingSpot = App.mapService.getNode(spotID);
                onPickParkingSpot.accept(parkingSpot);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void handleSaveNewParkingSpot(ActionEvent actionEvent) {
        switchToChoosing();
    }

    public void switchToChoosing() {
        saveNewParkingSpotButton.setDisable(true);
        state = SelectionState.CHOOSING;
        showParkingSpots();
    }

    private void switchToIdle() {
        saveNewParkingSpotButton.setDisable(false);
        state = SelectionState.IDLE;
        hideParkingSpots();
    }

    public boolean hasParkingSpot() {
        return Session.isLoggedIn()
            && Session.getAccount() != null
            && Session.getAccount().getParkingSpot() != null;
    }

    public String getSavedParkingSpotID() {
        if (!hasParkingSpot()) return null;
        return Session.getAccount().getParkingSpot();
    }

    public void show() {
        parkingWindow.setManaged(true);
        parkingWindow.setVisible(true);
    }

    public void hide() {
        parkingWindow.setManaged(false);
        parkingWindow.setVisible(false);
    }

    public void addParkingSpotCircle(Circle circle) {
        parkingSpots.add(circle);
    }

    public void clearParkingSpotCircles() {
        parkingSpots.clear();
    }

    public void showParkingSpots() {
        for (Circle circle : parkingSpots) {
            circle.setVisible(true);
        }
    }

    public void hideParkingSpots() {
        for (Circle circle : parkingSpots) {
            circle.setVisible(false);
        }
    }
}
