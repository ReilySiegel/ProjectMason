package edu.wpi.teamo.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.GiftRequest;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiftRequestPage extends ServiceRequestPage implements Initializable {

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
    private MenuButton locationBox;

    @FXML
    private JFXButton backButton;

    private boolean validRequest;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));
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

            menuItem.setOnAction(event -> {
                locationBox.setText(locationBox.getItems().stream()
                        .map((MenuItem mI) -> (CheckMenuItem) mI)
                        .filter(CheckMenuItem::isSelected)
                        .map(CheckMenuItem::getText)
                        .collect(Collectors.joining(", ")));
            });

            locationBox.getItems().add(menuItem);
        }

    }


    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String deliverToText = giftDeliverTo.getText();
        String trackingIDText = giftTrackingID.getText();
        String assignName = assignee.getText();

        List<MenuItem>        mItems    = locationBox.getItems();
        Stream<CheckMenuItem> cMItems   = mItems.stream().map((MenuItem mI) -> (CheckMenuItem) mI);
        Stream<CheckMenuItem> checked   = cMItems.filter(CheckMenuItem::isSelected);
        List<String>          locations = checked.map(CheckMenuItem::getText).collect(Collectors.toList());

        validRequest = true;

        System.out.println("requesting");

        if (validRequest) {
            new GiftRequest(
                    deliverToText,
                    trackingIDText,
                    new BaseRequest(
                            UUID.randomUUID().toString(),
                            notes.getText(),
                            locations.stream(),
                            assignName,
                            false))
                    .update();
            System.out.println("request successful");
            giftDeliverTo.setText("");
            giftTrackingID.setText("");
            assignee.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.medicine_request_submitted")));
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.deliver_to_semicolon")  + deliverToText + "\n" +
                    App.resourceBundle.getString("key.tracking_id_semicolon")  + trackingIDText + "\n" +
                    App.resourceBundle.getString("key.room_semicolon")  + String.join(", ", locations) + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon")  + assignName));
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