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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
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
    public void initialize(URL location, ResourceBundle resources) {

        try {
            LinkedList<String> nodeShortNames = new LinkedList<String>();

            LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));

            for (NodeInfo i : nodes) {
                nodeShortNames.add(i.getNodeID());
            }

            for (String nodeName : nodeShortNames) {
                loc.getItems().add(nodeName);
            }

        } catch (Exception SQLException) {
            return;
        }

    }

    private void handleSubmission(ActionEvent e) {
        String serviceName = service.getText();
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
