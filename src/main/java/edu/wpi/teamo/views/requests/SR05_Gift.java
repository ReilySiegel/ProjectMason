package edu.wpi.teamo.views.requests;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.GiftRequest;
import edu.wpi.teamo.views.LocationSearcher;
import edu.wpi.teamo.views.SubPageContainer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class SR05_Gift extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField giftTrackingID;

    @FXML
    private JFXTextField giftDeliverTo;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextField notes;

    @FXML
    private JFXButton backButton;

    @FXML
    private HBox assignedBox;

    @FXML
    private JFXTextField roomSearch;

    @FXML
    private JFXListView<JFXCheckBox> locationList;

    @FXML
    private Text targetErrorText, idErrorText, roomErrorText;

    private boolean validRequest;

    LocationSearcher locationSearcher;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        if (Session.getAccount() == null || !Session.getAccount().hasEmployeeAccess()) {
            assignedBox.setVisible(false);
            assignedBox.setManaged(false);
        }

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));
        validRequest = true;

        locationSearcher = new LocationSearcher(roomSearch, locationList);
        updateLocations();
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
        String deliverToText = giftDeliverTo.getText();
        String trackingIDText = giftTrackingID.getText();
        String assignName = assignee.getText();

        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String>   locationIDs = locationSearcher.getSelectedLocationIDs();

        validRequest = true;

        System.out.println("requesting");

        if(deliverToText.equals("")){
            targetErrorText.setText(App.resourceBundle.getString("key.recipient_error"));
            validRequest = false;
        }
        if(trackingIDText.equals("")){
            idErrorText.setText(App.resourceBundle.getString("key.ID_error"));
            validRequest = false;
        }
        if (assignName.equals("")) {
            assignName = "Unassigned";
        }
        if(locations.size() == 0){
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }

        if (validRequest) {
            new GiftRequest(
                    deliverToText,
                    trackingIDText,
                    new BaseRequest(
                            UUID.randomUUID().toString(),
                            notes.getText(),
                            locationIDs.stream(),
                            assignName,
                            false))
                    .update();
            System.out.println("request successful");
            giftDeliverTo.setText("");
            targetErrorText.setText("");
            idErrorText.setText("");
            roomErrorText.setText("");
            giftTrackingID.setText("");
            assignee.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.medicine_request_submitted")));
            if(!Session.getAccount().hasEmployeeAccess()){
                content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                        App.resourceBundle.getString("key.deliver_to_semicolon")  + deliverToText + "\n" +
                        App.resourceBundle.getString("key.tracking_id_semicolon")  + trackingIDText + "\n" +
                        App.resourceBundle.getString("key.room_semicolon")  + String.join(", ", locationIDs)));
            }
            else{
                content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                        App.resourceBundle.getString("key.deliver_to_semicolon")  + deliverToText + "\n" +
                        App.resourceBundle.getString("key.tracking_id_semicolon")  + trackingIDText + "\n" +
                        App.resourceBundle.getString("key.room_semicolon")  + String.join(", ", locationIDs) + "\n" +
                        App.resourceBundle.getString("key.persons_assigned_semicolon")  + assignName));
            }

            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_menu"));

            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popup.close();
                }
            });

            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    SubPageContainer.switchPage(Pages.SERVICEREQUEST);
                }
            });

            content.setActions(closeButton, backButton);
            popup.show();
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_gift_request")));
        content.setBody(new Text(App.resourceBundle.getString("key.gift_tracking_id_help") + '\n' +
                App.resourceBundle.getString("key.amount_help") +
                App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
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