package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PatientTransportation extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTextField locationLine;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    LocationSearcher locationSearcher;

    @FXML
    public void initialize(URL location, ResourceBundle resources){

        locationSearcher = new LocationSearcher(locationLine, roomList);
        updateLocations();

    }

    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException e) {
            locationSearcher.setLocations(new LinkedList<>());
            e.printStackTrace();
        }
    }

}
