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
import javafx.scene.layout.HBox;
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

public class SR03_Sanitation extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

    @FXML
    private VBox topVbox;

    @FXML
    private VBox midVbox;

    @FXML
    private HBox bottomHbox;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTimePicker time = new JFXTimePicker();

    @FXML
    private JFXDatePicker date = new JFXDatePicker();

    @FXML
    private JFXTextField notes;

    @FXML
    private Text typeErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text assignedErrorText;

    @FXML
    private JFXTextField locationLine;

    @FXML
    private JFXCheckBox recurCheck;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    LocationSearcher locationSearcher;

    private boolean validRequest;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        topVbox.getStyleClass().add("vbox");
        bottomHbox.getStyleClass().add("vbox");
        midVbox.getStyleClass().add("text-area");
        recurCheck.setStyle("-jfx-checked-color: #d8dee9");
        recurCheck.setStyle("-fx-text-fill: #d8dee9");



        App.getPrimaryStage().getScene().getStylesheets().add(LocationSearcher.getStylePath());

        locationSearcher = new LocationSearcher(locationLine, roomList);
        updateLocations();


        validRequest = true;
    }


    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException throwables) {
            locationSearcher.setLocations(new LinkedList<>());
            throwables.printStackTrace();
        }
    }

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String serviceName = service.getText();
        String assigned = assignee.getText();
        String details = notes.getText();

        List<NodeInfo>          locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();

        TextField curTime = time.getEditor();
        TextField curDate = date.getEditor();

        validRequest = true;

        if (serviceName.equals("")) {
            typeErrorText.setText(App.resourceBundle.getString("key.no_sanitation_type_specified"));
            validRequest = false;
        }

        if (locations.size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }

        if (assigned.equals("")) {
            assignedErrorText.setText(App.resourceBundle.getString("key.no_assignee_specified"));
            validRequest = false;
        }

        if (validRequest) {
            App.requestService.requestSanitation(locationIDs.stream(), assigned, serviceName + ", " + details);
            System.out.println("Sanitation request submitted");

            service.setText("");
            assignee.setText("");
            notes.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.sanitation_request_submitted")));
            content.setBody(new Text("Request submitted with: \n" +
                    App.resourceBundle.getString("key.type_of_sanitation") + serviceName + "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon") + assigned + "\n" +
                    App.resourceBundle.getString("key.additional_notes") + details));
            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_main"));

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
        content.setHeading(new Text(App.resourceBundle.getString("key.help_sanitation")));
        content.setBody(new Text(App.resourceBundle.getString("key.sanitation_type_help") +
                App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
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
