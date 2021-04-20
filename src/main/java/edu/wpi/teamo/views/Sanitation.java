package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Sanitation extends ServiceRequestPage implements Initializable {

    @FXML
    private JFXTextField service;

    @FXML
    private JFXComboBox<String> loc;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTimePicker time;

    @FXML
    private JFXTextField notes;

    @FXML
    private Text typeErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text assignedErrorText;

    private boolean validRequest;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        try {
            LinkedList<String> nodeIDs = new LinkedList<String>();

            LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));

            for (NodeInfo i : nodes) {
                nodeIDs.add(i.getNodeID());
            }

            for (String id : nodeIDs) {
                loc.getItems().add(id);
            }

        } catch (Exception SQLException) {
            return;
        }

    }

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String serviceName = service.getText();
        String room = loc.getValue();
        String assigned = assignee.getText();
        String details = notes.getText();

        validRequest = true;

        if (serviceName.equals("")) {
            typeErrorText.setText("Service type required");
            validRequest = false;
        }

        if (room == null) {
            roomErrorText.setText("No room/node selected");
            validRequest = false;
        }

        if (assigned.equals("")) {
            assignedErrorText.setText("Assignee name required");
            validRequest = false;
        }

        if (validRequest) {
            App.requestService.requestSanitation(room, assigned, serviceName + ", " + details);
            System.out.println("Sanitation request submitted");
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        Stage helpWindow = new Stage();

        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.setTitle("Help");
        helpWindow.setMinWidth(400);
        helpWindow.setMinHeight(200);

        Label label = new Label();
        label.setText("Fill out each field as follows: \n" +
                "Service Name: Which sanitation service is to be fulfilled \n" +
                "Time: When it should be done \n" +
                "Your Name: Your full name \n" +
                "Asignee Name: Full name of the person you want to fulfill the request");
        Button close = new Button("Close");

        close.setOnAction(g -> helpWindow.close());

        VBox layout = new VBox(13);
        layout.getChildren().addAll(label, close);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        helpWindow.setScene(scene);
        helpWindow.showAndWait();
    }


}
