package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Sanitation extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

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

    @FXML
    private MenuButton locationBox;

    private boolean validRequest;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {



        validRequest = true;
        try {
            resetLocationBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetLocationBox() throws SQLException {
        locationBox.getItems().removeAll(locationBox.getItems());

        LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));

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
    private void handleSubmission(ActionEvent e) throws SQLException {
        String serviceName = service.getText();
        String assigned = assignee.getText();
        String details = notes.getText();

        List<MenuItem> mItems    = locationBox.getItems();
        Stream<CheckMenuItem> cMItems   = mItems.stream().map((MenuItem mI) -> (CheckMenuItem) mI);
        Stream<CheckMenuItem> checked   = cMItems.filter(CheckMenuItem::isSelected);
        List<String>          locations = checked.map(CheckMenuItem::getText).collect(Collectors.toList());

        validRequest = true;

        if (serviceName.equals("")) {
            typeErrorText.setText("Service type required");
            validRequest = false;
        }

        if (locations.size() == 0) {
            roomErrorText.setText("No room/node selected");
            validRequest = false;
        }

        if (assigned.equals("")) {
            assignedErrorText.setText("Assignee name required");
            validRequest = false;
        }

        if (validRequest) {
            App.requestService.requestSanitation(locations.stream(), assigned, serviceName + ", " + details);
            System.out.println("Sanitation request submitted");

            service.setText("");
            assignee.setText("");
            notes.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Sanitation Request Submitted"));
            content.setBody(new Text("Request submitted with: \n" +
                    "Type: " + serviceName + "\n" +
                    "Room: " + String.join(", ", locations) + "\n" +
                    "Person(s) assigned: " + assigned + "\n" +
                    "Additional notes: " + details));
            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton("Close");
            JFXButton backButton = new JFXButton("Back to Menu");

            closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
            backButton.setStyle("-fx-background-color: #333333; -fx-text-fill: #fff");

            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popup.close();
                }
            });

            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    App.switchPage(Pages.SERVICEREQUEST);
                }
            });

            content.setActions(closeButton, backButton);
            popup.show();
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Help - Sanitation Service Request"));
        content.setBody(new Text("Service Name: Type of service required\n" +
                "Room: The node/room where the service is needed\n" +
                "Assignee Name: The person to be assigned to this service\n"));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();
    }


}
